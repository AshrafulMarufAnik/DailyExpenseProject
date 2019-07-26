package com.example.dailyexpenseproject;

public class Expense {
    private int Id;
    private String type;
    private String date,time;
    private double amount;
    private String receipt;

    public Expense() {
    }

    public Expense(String type, String date, String time, double amount, String receipt) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.receipt = receipt;
    }

    public Expense(int id, String type, String date, String time, double amount, String receipt) {
        Id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.receipt = receipt;
    }

    public int getId() {
        return Id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }

    public String getReceipt() {
        return receipt;
    }
}
