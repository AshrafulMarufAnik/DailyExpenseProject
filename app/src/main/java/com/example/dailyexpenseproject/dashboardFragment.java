package com.example.dailyexpenseproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class dashboardFragment extends Fragment {
    private View view;
    private Spinner expenseTypeSpinner;
    private LinearLayout fromDatePicker,toDatePicker;
    private TextView totalExpenseTV,fromDateSetTV,toDateSetTV;
    private FloatingActionButton addNewFAB;
    private DatabaseHelper databaseHelper;
    private double sumAmount=0;

    public dashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard,container,false);

        init();
        spinnerLoad();
        totalExpenseAmount();

        addNewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),addExpenseActivity.class);
                startActivity(intent);
            }
        });

        fromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFromDatePicker();
            }
        });

        toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleToDatePicker();
            }
        });


        return view;
    }

    private void spinnerLoad() {
        String expenseTypes [] = getResources().getStringArray(R.array.expenseType);
        ArrayAdapter expenseTypeArrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.expenseType,android.R.layout.simple_spinner_item);
        expenseTypeSpinner.setAdapter(expenseTypeArrayAdapter);
    }

    private void totalExpenseAmount() {
        Cursor currentCursor = databaseHelper.showAllData();

        while(currentCursor.moveToNext())
        {
            double amount = currentCursor.getDouble(currentCursor.getColumnIndex(databaseHelper.table1_COL_amount));
            sumAmount = sumAmount+amount;
        }
        totalExpenseTV.setText(sumAmount+" Tk.");
    }

    private void handleToDatePicker() {
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
    }

    private void handleFromDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String fromDate = date+" "+month+" "+year;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,date);
                CharSequence charSequence = DateFormat.format("EEEE, dd MMM yyyy",calendar1);
                fromDateSetTV.setText(charSequence);
            }
        },year,month,date);
        datePickerDialog.show();
    }

    private void init() {
        expenseTypeSpinner = view.findViewById(R.id.dashBoardExpenseTypeSPNR);
        totalExpenseTV = view.findViewById(R.id.dashBoardTotalExpenseTV);
        addNewFAB = view.findViewById(R.id.dashBoardAddExpenseBTN);
        fromDatePicker = view.findViewById(R.id.dashBoardFromDateClickTV);
        toDatePicker = view.findViewById(R.id.dashBoardToDateClickTV);
        fromDateSetTV = view.findViewById(R.id.fromDateSetTV);
        toDateSetTV = view.findViewById(R.id.toDateSetTV);

        databaseHelper = new DatabaseHelper(getContext());
    }

}
