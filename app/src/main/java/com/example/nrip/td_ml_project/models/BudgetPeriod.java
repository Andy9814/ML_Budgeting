package com.example.nrip.td_ml_project.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class BudgetPeriod {
    public int Year;
    public int WeekNumber;
    public ArrayList<BudgetEntry> Entries;
    public Date StartDate;
    public Date EndDate;

    public BudgetPeriod (int y, int w) {
        Entries = new ArrayList<>();
        Year = y;
        WeekNumber = w;
    }

    public BigDecimal CalcDeposits () {
        BigDecimal result = BigDecimal.ZERO;

        for (int i = 0; i < Entries.size(); i++) {
            if (Entries.get(i).Amount.compareTo(BigDecimal.ZERO) > 0) {
                result = result.add(Entries.get(i).Amount);
            }
        }

        return  result;
    }

    public BigDecimal CalcWithdrawals () {
        BigDecimal result = BigDecimal.ZERO;

        for (int i = 0; i < Entries.size(); i++) {
            if (Entries.get(i).Amount.compareTo(BigDecimal.ZERO) < 0) {
                result = result.add(Entries.get(i).Amount);
            }
        }

        return  result;
    }

    public BigDecimal CalcNet() {
        BigDecimal result = BigDecimal.ZERO;

        for (int i = 0; i < Entries.size(); i++) {
            result = result.add(Entries.get(i).Amount);
        }

        return  result;
    }
}
