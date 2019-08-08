package com.example.dailyexpenseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Expense.db";
    public static String TABLE_NAME = "Expense";
    public static String COL_id = "Id";
    public static String COL_type = "expenseType";
    public static String COL_date = "expenseDate";
    public static String COL_time = "expenseTime";
    public static String COL_amount = "expenseAmount";
    public static String COL_receipt = "expenseReceipt";
    public static String COL_receipt_type = "receiptType";
    public static int DB_VERSION = 1;
    public String type;

    //Sql Queries
    public String create_table = "create table "+ TABLE_NAME +"(Id integer primary key,expenseType String,expenseDate String,expenseTime String,expenseAmount String,expenseReceipt String,receiptType int)";
    public String show_all_data_table1 = "select * from "+ TABLE_NAME +"";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor showAllData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(show_all_data_table1,null);

        return cursor;
    }

    public Cursor showDataTypeWise(String type){
        String show_type_wise_data = "select * from Expense where expenseType= '"+type+"'";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor1 = sqLiteDatabase.rawQuery(show_type_wise_data,null);

        return cursor1;
    }

    public Cursor showDataDateWise(long fromDate,long toDate){
        String show_date_wise_data = "select * from Expense where expenseDate between '"+fromDate+"' and '"+toDate+"'";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor2 = sqLiteDatabase.rawQuery(show_date_wise_data,null);

        return cursor2;
    }

    public void insert(String type,long date,String time,double amount,String receipt,int receiptType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_type,type);
        contentValues.put(COL_date,date);
        contentValues.put(COL_time,time);
        contentValues.put(COL_amount,amount);
        contentValues.put(COL_receipt,receipt);
        contentValues.put(COL_receipt_type,receiptType);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();

    }

    public void update(int id,String type,long date,String time,double amount,String receipt,int receiptType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_id,id);
        contentValues.put(COL_type,type);
        contentValues.put(COL_date,date);
        contentValues.put(COL_time,time);
        contentValues.put(COL_amount,amount);
        contentValues.put(COL_receipt,receipt);
        contentValues.put(COL_receipt_type,receiptType);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(TABLE_NAME,contentValues,"Id=?",new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public void delete(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,"Id=?",new String[]{String.valueOf(id)});
    }
}
