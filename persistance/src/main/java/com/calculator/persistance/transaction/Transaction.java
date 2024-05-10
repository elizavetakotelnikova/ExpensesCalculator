package com.calculator.persistance.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.List;

@Entity
@Table(name="transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float value;
    @Enumerated(EnumType.STRING)
    @Convert(converter = MonthConverter.class)
    private Month month;
    private Integer mccCode;

    public Transaction(String name, Float amount, Month month, Integer mccCode) {
        this.name = name;
        this.value = amount;
        this.month = month;
        this.mccCode = mccCode;
    }
}
