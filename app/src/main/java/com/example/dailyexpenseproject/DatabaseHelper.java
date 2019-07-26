package com.example.dailyexpenseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Expense.db";
    public static String TABLE1_NAME = "Expense";
    public static String table1_COL_id = "Id";
    public static String table1_COL_type = "expenseType";
    public static String table1_COL_date = "expenseDate";
    public static String table1_COL_time = "expenseTime";
    public static String table1_COL_amount = "expenseAmount";
    public static String table1_COL_receipt = "expenseReceipt";
    public static int DB_VERSION = 1;

    //Sql Queries
    public String create_table1 = "create table "+TABLE1_NAME+"(Id integer primary key,expenseType String,expenseDate String,expenseTime String,expenseAmount String,expenseReceipt String)";
    public String show_all_data_table1 = "select * from "+TABLE1_NAME+"";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_table1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor showAllData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(show_all_data_table1,null);

        return cursor;
    }

    public void insert(String type,String date,String time,double amount,String receipt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(table1_COL_type,type);
        contentValues.put(table1_COL_date,date);
        contentValues.put(table1_COL_time,time);
        contentValues.put(table1_COL_amount,amount);
        contentValues.put(table1_COL_receipt,receipt);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE1_NAME,null,contentValues);
        sqLiteDatabase.close();

    }

    public void insert(String type,String date,String time,double amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(table1_COL_type,type);
        contentValues.put(table1_COL_date,date);
        contentValues.put(table1_COL_time,time);
        contentValues.put(table1_COL_amount,amount);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE1_NAME,null,contentValues);
        sqLiteDatabase.close();

    }

    public void update(int id) {

    }

    public void delete(int id){

    }
}
