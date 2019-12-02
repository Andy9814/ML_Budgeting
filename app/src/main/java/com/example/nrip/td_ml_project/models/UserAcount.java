package com.example.nrip.td_ml_project.models;

import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserAcount implements Serializable {
    private String     userName;
    private BigDecimal userImagePrice;
    private BigDecimal userAutoInvestment;
    private BigDecimal userTotalAmount;
    private boolean    userWantToBuyCheck;  // change name
    private BigDecimal userBudget;
    // constructor
    public  UserAcount(BigDecimal imagePrice,BigDecimal autoInvest){
        this.userImagePrice = imagePrice;
        this.userAutoInvestment= autoInvest;
    }

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
