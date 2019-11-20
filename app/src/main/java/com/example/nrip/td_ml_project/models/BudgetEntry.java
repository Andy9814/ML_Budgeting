package com.example.nrip.td_ml_project.models;

import java.math.BigDecimal;
import java.util.Date;

public class BudgetEntry {
    public String Type;
    public Date Date;
    public BigDecimal Amount;

    public BudgetEntry(String type, Date date, BigDecimal amount) {
        Type = type;
        Date = date;
        Amount = amount;
    }
}
