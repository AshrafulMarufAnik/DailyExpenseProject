package com.example.dailyexpenseproject;

public class Expense {
    private int Id;
    private String type;
    private String time;
    private long date;
    private double amount;
    private String receipt;
    private int receiptType;

    public Expense() {
    }

    public Expense(String type, String time, long date, double amount, String receipt, int receiptType) {
        this.type = type;
        this.time = time;
        this.date = date;
        this.amount = amount;
        this.receipt = receipt;
        this.receiptType = receiptType;
    }

    public Expense(int id, String type, String time, long date, double amount, String receipt, int receiptType) {
        Id = id;
        this.type = type;
        this.time = time;
        this.date = date;
        this.amount = amount;
        this.receipt = receipt;
        this.receiptType = receiptType;
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

    public int getReceiptType() {
        return receiptType;
    }
}
