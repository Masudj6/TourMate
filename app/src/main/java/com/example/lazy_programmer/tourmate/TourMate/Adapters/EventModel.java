package com.example.lazy_programmer.tourmate.TourMate.Adapters;

/**
 * Created by Lazy-Programmer on 10/22/2017.
 */

public class EventModel {

    private String key,fromDate,toDate,budget,locationName,balanceRemaining;

    public EventModel(String key, String fromDate, String toDate, String budget, String locationName,String balanceRemaining) {
        this.key = key;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.budget = budget;
        this.locationName = locationName;
        this.balanceRemaining=balanceRemaining;
    }

    public EventModel() {
    }

    public String getKey() {
        return key;
    }

    public String getBalanceRemaining() {
        return balanceRemaining;
    }

    public void setBalanceRemaining(String balanceRemaining) {
        this.balanceRemaining = balanceRemaining;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
