package com.example.nrip.td_ml_project.models;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class UserAcount implements Serializable {
    private String Name;
    private BigDecimal savingGoalAmt;
    private BigDecimal autoInvestment;
    private  Boolean FirstTimeUser;
    private List<Transaction> mapTransactions;

    public List<Transaction> getMapTransactions() {
        return mapTransactions;
    }

    public void setMapTransactions(List<Transaction> mapTransactions) {
        this.mapTransactions = mapTransactions;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setSavingGoalAmt(BigDecimal savingGoalAmt) {
        this.savingGoalAmt = savingGoalAmt;
    }

    public void setAutoInvestment(BigDecimal autoInvestment) {
        this.autoInvestment = autoInvestment;
    }

    public void setFirstTimeUser(Boolean firstTimeUser) {
        FirstTimeUser = firstTimeUser;
    }



    public String getName() {
        return Name;
    }

    public BigDecimal getSavingGoalAmt() {
        return savingGoalAmt;
    }

    public BigDecimal getAutoInvestment() {
        return autoInvestment;
    }

    public Boolean getFirstTimeUser() {
        return FirstTimeUser;
    }


}
