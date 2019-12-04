package com.example.nrip.td_ml_project.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {
    private String     userName;
    private BigDecimal userImagePrice;
    private BigDecimal userAutoInvestment;
    private BigDecimal userTotalAmount;
    private boolean    userWantToBuyCheck;  // change name
    private BigDecimal userBudget;
    private BigDecimal userSpentBudget;
    private BigDecimal userGoalsAmt;
    public  List<BigDecimal>    testx =  new ArrayList<BigDecimal>();


    public BigDecimal getUserGoalsAmt() {
        return userGoalsAmt;
    }

    public void setUserGoalsAmt(BigDecimal userGoalsAmt) {
        this.userGoalsAmt = userGoalsAmt;
    }


    public BigDecimal getSpentBudget() {
        return userSpentBudget;
    }

    public void setSpentBudget(BigDecimal spentBudget) {
        this.userSpentBudget = spentBudget;
    }

//    // constructor
//    public  Transaction(BigDecimal imagePrice,BigDecimal autoInvest){
//        this.userImagePrice = imagePrice;
//        this.userAutoInvestment= autoInvest;
//    }

    public BigDecimal getUserBudget() {
        return userBudget;
    }

    public void setUserBudget(BigDecimal userBudget) {
        this.userBudget = userBudget;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserImagePrice(BigDecimal userImagePrice) {
        this.userImagePrice = userImagePrice;
    }

    public void setUserAutoInvestment(BigDecimal userAutoInvestment) {
        this.userAutoInvestment = userAutoInvestment;
    }

    public void setUserTotalAmount(BigDecimal userTotalAmount) {
        this.userTotalAmount = userTotalAmount;
    }

    public void setUserWantToBuyCheck(boolean userWantToBuyCheck) {
        this.userWantToBuyCheck = userWantToBuyCheck;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getUserImagePrice() {
        return userImagePrice;
    }

    public BigDecimal getUserAutoInvestment() {
        return userAutoInvestment;
    }

    public BigDecimal getUserTotalAmount() {
        return userTotalAmount;
    }

    public boolean isUserWantToBuyCheck() {
        return userWantToBuyCheck;
    }

}
