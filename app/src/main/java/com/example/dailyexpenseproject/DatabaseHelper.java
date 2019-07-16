package com.example.dailyexpenseproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Expense.db";
    public static String TABLE1_NAME = "expense";
    public static String TABLE2_NAME = "";
    public static String table1_COL_id = "Id";
    public static String table2_COL_id = "Id";
    public static int DB_VERSION = 1;

    //Sql Queries
    public String create_table1 = "create table "+TABLE1_NAME+"(Id integer primary key)";
    public String create_table2 = "create table "+TABLE2_NAME+"(Id integer primary key)";

    public String show_all_data_table1 = "select * from "+TABLE1_NAME+"";
    public String show_all_data_table2 = "select * from "+TABLE2_NAME+"";

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

    public void insert() {

    }

    public void update() {

    }

    public void delete(){

    }
}
