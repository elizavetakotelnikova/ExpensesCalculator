package com.calculator.services.services;

import org.springframework.stereotype.Component;

@Component
public class ConsoleDisplay implements Displayable {
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
