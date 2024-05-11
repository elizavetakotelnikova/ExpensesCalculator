package com.calculator.expensescalculator;

import com.calculator.expensescalculator.exceptions.ParsingException;
import com.calculator.services.CommandInvoker;
import com.calculator.services.CommandInvokerImpl;
import com.calculator.services.commands.Command;
import com.calculator.services.commands.CommandsDictionary;
import com.calculator.services.exceptions.CommandExecutionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.calculator.services")
@ComponentScan("com.calculator.persistance")
@ComponentScan("com.calculator.expensescalculator")
public class ExpensesCalculatorApplication {
    public static void main(String[] args) {
        var applicationContext = SpringApplication.run(ExpensesCalculatorApplication.class, args);
        var commandsDictionary = new CommandsDictionary(applicationContext);
        var parser = new ConsoleCommandParser(commandsDictionary);
        var invoker = new CommandInvokerImpl();
        // commands parsing
        while (true) {
            try {
                Command command = parser.parseCommand();
                invoker.consume(command);
            }
            catch (ParsingException exc) {
                System.out.println("Error in parsing command: " + exc.getMessage());
            }
            catch (CommandExecutionException exc) {
                System.out.println("Error in command execution: " + exc.getMessage());
            }
            catch (Exception exc) {
                System.out.println("Error during runtime: " + exc.getMessage() + " " + exc.getCause() + " " + exc.getClass());
            }
        }
    }

}
