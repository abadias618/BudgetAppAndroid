package com.byui.budgetappandroid;

import java.io.Serializable;
// implements serializable because it's later passed to an activity as a serialized object
public class Expense implements Serializable {
    private int id;
    private String date;
    private String name;
    private double amount;
    private String category;

    public Expense() {
    }
    public Expense(double amount) {
        id = 0;
        date = "";
        name = "";
        this.amount = amount;
        category = "";
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //for debugging
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                '}';
    }
}
