package com.example.dailyexpenseproject;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseShowFragment extends Fragment {
    private View view;
    private Spinner expenseTypeSpinner;
    private LinearLayout fromDatePicker,toDatePicker;
    private FloatingActionButton addExpenseFAB;
    private TextView fromDateSetTV,toDateSetTV;
    private long fromDateInMS,toDateInMS;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<Expense> expenseList;
    private RecyclerView expenseRV;

    public ExpenseShowFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_expense_show,container,false);

        init();
        spinnerLoad();

        expenseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String expenseType[] = getResources().getStringArray(R.array.expenseType);
                if(position==0){
                    getDataFromDB();
                }
                else {
                    String type = expenseType[position];
                    typeWiseGetDataFromDB(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getDataFromDB();
        configExpenseRV();

        addExpenseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        fromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFromDate();
            }
        });

        toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleToDate();
            }
        });

        return view;
    }

    private void spinnerLoad() {
        ArrayAdapter expenseTypeArrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.expenseType,R.layout.single_spinner_item);
        expenseTypeSpinner.setAdapter(expenseTypeArrayAdapter);
    }

    private void handleToDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String toDate = date+" "+month+" "+year;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,date);
                CharSequence charSequence = DateFormat.format("EEEE, dd MMM yyyy",calendar1);
                toDateSetTV.setText(charSequence);
            }
        },year,month,date);
        datePickerDialog.show();

        String currentDate = year + "/" + month + "/" + date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        toDateInMS = date1.getTime();
    }

    private void handleFromDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String toDate = date+" "+month+" "+year;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,date);
                CharSequence charSequence = DateFormat.format("EEEE, dd MMM yyyy",calendar1);
                fromDateSetTV.setText(charSequence);
            }
        },year,month,date);
        datePickerDialog.show();

        String currentDate = year + "/" + month + "/" + date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fromDateInMS = date1.getTime();
    }

    private void getDataFromDB() {
        Cursor currentCursor = databaseHelper.showAllData();
        expenseList.clear();

        while(currentCursor.moveToNext())
        {
           int id = Integer.parseInt(currentCursor.getString(currentCursor.getColumnIndex(databaseHelper.COL_id)));
           String type = currentCursor.getString(currentCursor.getColumnIndex(databaseHelper.COL_type));
           long date = currentCursor.getLong(currentCursor.getColumnIndex(databaseHelper.COL_date));
           String time = currentCursor.getString(currentCursor.getColumnIndex(databaseHelper.COL_time));
           double amount = currentCursor.getDouble(currentCursor.getColumnIndex(databaseHelper.COL_amount));
           String receipt = currentCursor.getString(currentCursor.getColumnIndex(databaseHelper.COL_receipt));
           int receiptType = currentCursor.getInt(currentCursor.getColumnIndex(databaseHelper.COL_receipt_type));

           Expense expenses = new Expense(id,type,time,date,amount,receipt,receiptType);
           expenseList.add(expenses);
           dataAdapter.notifyDataSetChanged();
        }
    }

    private void typeWiseGetDataFromDB(String type){
        Cursor cursor = databaseHelper.showDataTypeWise(type);
        expenseList.clear();

        while(cursor.moveToNext())
        {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(databaseHelper.COL_id)));
            String expenseType = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_type));
            long date = cursor.getLong(cursor.getColumnIndex(databaseHelper.COL_date));
            String time = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_time));
            double amount = cursor.getDouble(cursor.getColumnIndex(databaseHelper.COL_amount));
            String receipt = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_receipt));
            int receiptType = cursor.getInt(cursor.getColumnIndex(databaseHelper.COL_receipt_type));

            Expense expenses = new Expense(id,expenseType,time,date,amount,receipt,receiptType);
            expenseList.add(expenses);
            dataAdapter.notifyDataSetChanged();
        }
    }

    private void configExpenseRV() {
        expenseRV.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRV.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void init() {
        expenseTypeSpinner = view.findViewById(R.id.expenseTypeSPNR);
        addExpenseFAB = view.findViewById(R.id.addExpenseActivityBTN);
        fromDatePicker = view.findViewById(R.id.fromDateClickTV);
        toDatePicker = view.findViewById(R.id.toDateClickTV);
        fromDateSetTV = view.findViewById(R.id.fromDateSetTV);
        toDateSetTV = view.findViewById(R.id.toDateSetTV);
        expenseRV = view.findViewById(R.id.expenseListRV);

        expenseList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());
        dataAdapter = new DataAdapter(databaseHelper,expenseList,getContext());
    }

}
