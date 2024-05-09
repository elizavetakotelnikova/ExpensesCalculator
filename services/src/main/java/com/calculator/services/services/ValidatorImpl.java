package com.calculator.services.services;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import org.springframework.stereotype.Component;

import java.time.Month;

@Component
public class ValidatorImpl implements Validator {
    @Override
    public boolean validateCategoryName(String name) {
        return name != null && !name.matches("[0-9]+$");
    }

    @Override
    public boolean validateMccCode(String code) {
        return code.matches("[0-9]{4}");
    }

    @Override
    public boolean validateMonth(String month) {
        boolean flag = false;
        for (var each : Month.values()) {
            if (each.name().equals(month.toUpperCase())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    // название транзакции не валидируем (нет указаний в тз, считаем что название может содержать любые символы)
    @Override
    public boolean validateTransactionName(String name) {
        return name != null;
    }

    @Override
    public boolean validateAmount(String amount) {
        if (amount == null) return false;
        try {
            var value = Float.parseFloat(amount);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
