package com.calculator.expensescalculator;

import com.calculator.expensescalculator.exceptions.ParsingException;
import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.persistance.transaction.TransactionRepository;
import com.calculator.services.CommandInvoker;
import com.calculator.services.commands.Command;
import com.calculator.services.exceptions.CommandExecutionException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Month;

//@ContextConfiguration(classes = TestExpensesCalculatorApplication.class)
@SpringBootTest
class TransactionsTests {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    Category testCategory;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private Parser parser;
    @Autowired
    private CommandInvoker commandInvoker;
    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() throws SQLException {
        Connection connectionProvider = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        var flyway = setupFlyway(postgres);
        //flyway.clean();
        flyway.migrate();
        categoryRepository.deleteAll();
        transactionRepository.deleteAll();
    }
    private Flyway setupFlyway(PostgreSQLContainer container) {
        return new Flyway(
                Flyway.configure()
                        .locations("/db.migration")
                        .dataSource(container.getJdbcUrl(), container.getUsername(),
                                container.getPassword())
        );
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    private void readCommand() throws ParsingException, CommandExecutionException {
        Command currentCommand = parser.parseCommand();
        commandInvoker.consume(currentCommand);
    }

    @Test
    void testAddTransaction() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction firstOne 1000 april 1457".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        var foundTransactions = transactionRepository.findTransactionsByNameAndValueAndMonth("firstOne", 1000, Month.APRIL);
        Assertions.assertEquals(1, foundTransactions.size());
    }

    @Test
    void testRemovingTransaction() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction firstOne 1000 april 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction firstOne 1000 april 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("remove transaction firstOne 1000 april 1457".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        var foundTransactions = transactionRepository.findTransactionsByNameAndValueAndMonth("firstOne", 1000, Month.APRIL);
        Assertions.assertEquals(1, foundTransactions.size());
    }

    @Test
    void testRemovingNonexistentTransaction() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("remove transaction firstOne 1000 april 1457".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.assertSame(e.getClass(), CommandExecutionException.class);
            Assertions.assertEquals("No such transaction", e.getMessage());
        }
    }
}
