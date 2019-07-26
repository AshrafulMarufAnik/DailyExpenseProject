package com.example.dailyexpenseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class addExpenseActivity extends AppCompatActivity {
    private Spinner expenseTypeSpinner;
    private LinearLayout datePicker,timePicker;
    private ImageView receiptIV;
    private EditText expenseAmountET;
    private TextView datePickerTV,timePickerTV;

    private String expenseType,expenseDate,expenseTime;
    private double expenseAmount;
    private Image expenseReceipt;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        init();
        getIntentData();
        setIntentData();

        String expenseTypes [] = getResources().getStringArray(R.array.expenseType);
        ArrayAdapter expenseTypeArrayAdapter = ArrayAdapter.createFromResource(this,R.array.expenseType,android.R.layout.simple_spinner_item);
        expenseTypeSpinner.setAdapter(expenseTypeArrayAdapter);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDatePicker();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTimePicker();
            }
        });

    }

    private void currentTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(calendar.HOUR);
        int minute = calendar.get(calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
               Calendar calendar1 = Calendar.getInstance();
               calendar1.set(Calendar.HOUR,hour);
               calendar1.set(Calendar.MINUTE,minute);
               CharSequence charSequence = DateFormat.format("hh:mm a",calendar1);
               timePickerTV.setText(charSequence);
            }
        },hour,minute,is24HourFormat);
        timePickerDialog.show();
    }

    private void currentDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,date);
                CharSequence charSequence = DateFormat.format("EEEE, dd MMM yyyy",calendar1);
                datePickerTV.setText(charSequence);

            }
        },year,month,date);
        datePickerDialog.show();
    }

    public void addReceiptImage(View view) {
    }

    public void insertNewExpenseIntoDB(View view) {
        expenseType = expenseTypeSpinner.getSelectedItem().toString();
        expenseDate = datePickerTV.getText().toString();
        expenseTime = timePickerTV.getText().toString();
        expenseAmount = Double.parseDouble(expenseAmountET.getText().toString());

        databaseHelper.insert(expenseType,expenseDate,expenseTime,expenseAmount);

        Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

    private void setIntentData() {
    }

    private void getIntentData() {
    }

    private void init() {
        expenseTypeSpinner = findViewById(R.id.addNewExpenseTypeSPNR);
        datePicker = findViewById(R.id.addNewExpenseDateClickTV);
        timePicker = findViewById(R.id.addNewExpenseTimeClickTV);
        datePickerTV = findViewById(R.id.datePickerTV);
        timePickerTV = findViewById(R.id.timePickerTV);
        expenseAmountET = findViewById(R.id.addNewExpenseAmountET);
        receiptIV = findViewById(R.id.addNewExpenseReceiptIV);

        databaseHelper = new DatabaseHelper(this);
    }

}
