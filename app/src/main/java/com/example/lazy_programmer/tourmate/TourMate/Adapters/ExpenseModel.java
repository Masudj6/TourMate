package com.example.lazy_programmer.tourmate.TourMate.Adapters;

/**
 * Created by Lazy-Programmer on 10/24/2017.
 */

public class ExpenseModel {

    private String time,expenseName,expenseCost;

    public ExpenseModel(String time, String expenseName, String expenseCost) {
        this.time = time;
        this.expenseName = expenseName;
        this.expenseCost = expenseCost;
    }

    public ExpenseModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseCost() {
        return expenseCost;
    }

    public void setExpenseCost(String expenseCost) {
        this.expenseCost = expenseCost;
    }
}
