package com.example.dailyexpenseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

public class addExpenseActivity extends AppCompatActivity {
    private Spinner expenseTypeSpinner;
    private DatePicker currentDatePicker;
    private TimePicker currentTimePicker;
    private LinearLayout datePicker,timePicker;
    private ImageView receiptIV;
    private EditText expenseAmountET;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        init();
        getIntentData();
        setIntentData();

    }

    public void addReceiptImage(View view) {

    }

    public void insertNewExpenseIntoDB(View view) {

    }

    private void setIntentData() {
    }

    private void getIntentData() {
    }

    private void init() {
        expenseTypeSpinner = findViewById(R.id.addNewExpenseTypeSPNR);
        datePicker = findViewById(R.id.addNewExpenseDateClickTV);
        timePicker = findViewById(R.id.addNewExpenseTimeClickTV);
        expenseAmountET = findViewById(R.id.addNewExpenseAmountET);
        receiptIV = findViewById(R.id.addNewExpenseReceiptIV);

        databaseHelper = new DatabaseHelper(this);
    }

}
