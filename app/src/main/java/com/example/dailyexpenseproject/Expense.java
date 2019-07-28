package com.example.dailyexpenseproject;

public class Expense {
    private int Id;
    private String type;
    private String time;
    private long date;
    private double amount;
    private String receipt;

    public Expense() {
    }

    public Expense(String type, long date, String time, double amount, String receipt) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.receipt = receipt;
    }

    public Expense(int id, String type, long date, String time, double amount, String receipt) {
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

    public long getDate() {
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
