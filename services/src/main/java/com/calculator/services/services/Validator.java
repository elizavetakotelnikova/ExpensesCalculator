package com.calculator.services.services;

public interface Validator {
    public boolean validateCategoryName(String name);
    public boolean validateMccCode(String code);
    public boolean validateMonth(String month);
    public boolean validateTransactionName(String name);
    public boolean validateAmount(String amount);
}
