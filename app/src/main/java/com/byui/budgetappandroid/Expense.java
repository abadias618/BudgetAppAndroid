package com.byui.budgetappandroid;

public class Expense {
    private String user;
    private String date;
    private String name;
    private double amount;
    private int id;

    public Expense(String user, String date, String name, double amount, int id) {
        this.user = user;
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.id = id;
    }
}
