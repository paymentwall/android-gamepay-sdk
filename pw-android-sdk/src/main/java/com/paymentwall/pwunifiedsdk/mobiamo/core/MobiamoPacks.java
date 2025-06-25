package com.paymentwall.pwunifiedsdk.mobiamo.core;

/**
 * Created by nguyen.anh on 11/9/2016.
 */

public class MobiamoPacks {
    private int quantity;
    private double amount;
    private String name;
    private String currency;

    public MobiamoPacks(int quantity, double amount, String currency, String name) {
        this.quantity = quantity;
        this.amount = amount;
        this.name = name;
        this.currency = currency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
