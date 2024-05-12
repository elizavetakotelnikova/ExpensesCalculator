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
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Month;
import java.util.List;

//@ContextConfiguration(classes = TestExpensesCalculatorApplication.class)
@SpringBootTest
class ExpensesCalculatorApplicationTests {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
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
    private void readCommand() throws ParsingException, CommandExecutionException {
        Command currentCommand = parser.parseCommand();
        commandInvoker.consume(currentCommand);
    }
    @Test
    void testAddingCategory() {
        System.setIn(new ByteArrayInputStream("add category testCategory 1235".getBytes()));
        try {
            Command currentCommand = parser.parseCommand();
            commandInvoker.consume(currentCommand);
        }
        catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertEquals(1, categoryRepository.findCategoriesByName("testCategory").size());
    }
    @Test
    void testDeletingNonexistentCategory() {
        System.setIn(new ByteArrayInputStream("remove category dontexist".getBytes()));
        try {
            readCommand();
        }
        catch (Exception e) {
            Assertions.assertSame(e.getClass(), CommandExecutionException.class);
            Assertions.assertEquals("No such category", e.getMessage());
        }
    }
    @Test
    void testDeletingExistingCategory() {
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn( new ByteArrayInputStream("remove category testCategory".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertEquals(0, categoryRepository.findCategoriesByName("testCategory").size());
    }

    @Test
    void testAddingMccCategory() {
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add mcc to category testCategory 5674 9874".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        var foundCategory = categoryRepository.findCategoriesByName("testCategory").getFirst();
        var addedCodes = List.of(1457, 5674, 9874);
        Assertions.assertEquals(addedCodes, foundCategory.getMccCode());
    }

    @Test
    void testAddingGroupToCategory() {
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add category secondCategory 8941 8765".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add group to category testCategory secondCategory".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        var foundCategory = categoryRepository.findCategoriesByName("secondCategory").getFirst();
        var foundParentCategory = categoryRepository.findCategoriesByName("testCategory").getFirst();
        var subcategoriesIds = foundParentCategory.getSubcategories().stream().map(Category::getId).toList();
        Assertions.assertTrue(subcategoriesIds.contains(foundCategory.getId()));
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
    void testShowExpensesByMonth() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add category secondCategory 8941 8765".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add group to category testCategory secondCategory".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add category thirdCategory 1111".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add group to category secondCategory thirdCategory".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction firstOne 1000 april 8765".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("show expenses in april".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertTrue(out.toString().contains("secondCategory 1000.0 рублей"));
        Assertions.assertTrue(out.toString().contains("testCategory 1000.0 рублей"));
        Assertions.assertTrue(out.toString().contains("thirdCategory 0.0 рублей"));
    }

    @Test
    void testShowExpensesByTestCategory() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            System.setIn(new ByteArrayInputStream("add category testCategory 1457".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add category secondCategory 8941 8765".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add group to category testCategory secondCategory".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add category thirdCategory 1111".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add group to category secondCategory thirdCategory".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction firstOne 1000 april 8765".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction secondOne 1200 april 1111".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("add transaction secondOne 1200 may 1111".getBytes()));
            readCommand();
            System.setIn(new ByteArrayInputStream("show monthly expenses in testCategory".getBytes()));
            readCommand();
        }
        catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertTrue(out.toString().contains("май 1200.0 рублей"));
        Assertions.assertTrue(out.toString().contains("апрель 2200.0 рублей"));
        Assertions.assertTrue(out.toString().contains("июнь 0.0 рублей"));
    }
}
