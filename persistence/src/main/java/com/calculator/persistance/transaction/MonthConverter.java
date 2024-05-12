package com.calculator.persistance.transaction;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Month;

@Converter(autoApply = true)
public class MonthConverter implements AttributeConverter<Month, String> {
    @Override
    public String convertToDatabaseColumn(Month month) {
        return month.name().toLowerCase();
    }

    @Override
    public Month convertToEntityAttribute(String dbData) {
        return Month.valueOf(dbData.toUpperCase());
    }
}
