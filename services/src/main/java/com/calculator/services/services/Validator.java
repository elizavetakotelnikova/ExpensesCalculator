package com.calculator.services.services;

public interface Validator {
    boolean validateCategoryName(String name);
    boolean validateMccCode(String code);
    boolean validateMonth(String month);
    boolean validateTransactionName(String name);
    boolean validateAmount(String amount);
}
