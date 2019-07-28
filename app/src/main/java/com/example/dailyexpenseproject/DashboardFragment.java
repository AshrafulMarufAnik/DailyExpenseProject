package com.example.dailyexpenseproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class DashboardFragment extends Fragment {
    private View view;
    private Spinner expenseTypeSpinner;
    private LinearLayout fromDatePicker,toDatePicker;
    private TextView totalExpenseTV,fromDateSetTV,toDateSetTV;
    private FloatingActionButton addNewFAB;
    private DatabaseHelper databaseHelper;
    private DataAdapter dataAdapter;
    private ArrayList<Expense> expenseList;
    private long dateInMS;
    private double sumAmount=0;
    private long fromDateInMS,toDateInMS;
    private double typeWiseSumAmount=0;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard,container,false);

        init();
        spinnerLoad();
        totalExpenseAmount();

        /*
        expenseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String expenseType[] = getResources().getStringArray(R.array.expenseType);
                switch (i)
                {
                    case 0:
                        totalExpenseAmount();
                        break;
                    case 1:
                        //typeWiseTotalAmount(expenseType[i]);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); */


        addNewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
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
        ArrayAdapter expenseTypeArrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.expenseType,android.R.layout.simple_spinner_item);
        expenseTypeSpinner.setAdapter(expenseTypeArrayAdapter);
    }

    private void totalExpenseAmount() {
        Cursor currentCursor = databaseHelper.showAllData();

        while(currentCursor.moveToNext())
        {
            double amount = currentCursor.getDouble(currentCursor.getColumnIndex(databaseHelper.COL_amount));
            sumAmount = sumAmount+amount;
        }
        totalExpenseTV.setText(sumAmount+" Tk.");
        sumAmount=0;
    }

    private void typeWiseTotalAmount(String type){
        Cursor cursor = databaseHelper.showDataTypeWise(type);

        while(cursor.moveToNext())
        {
            double typeWiseAmount = cursor.getDouble(cursor.getColumnIndex(databaseHelper.COL_amount));
            typeWiseSumAmount = typeWiseSumAmount+typeWiseAmount;
        }
        totalExpenseTV.setText(typeWiseSumAmount+" Tk.");
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

    private void handleFromDatePicker() {
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

    private void init() {
        expenseTypeSpinner = view.findViewById(R.id.dashBoardExpenseTypeSPNR);
        totalExpenseTV = view.findViewById(R.id.dashBoardTotalExpenseTV);
        addNewFAB = view.findViewById(R.id.dashBoardAddExpenseBTN);
        fromDatePicker = view.findViewById(R.id.dashBoardFromDateClickTV);
        toDatePicker = view.findViewById(R.id.dashBoardToDateClickTV);
        fromDateSetTV = view.findViewById(R.id.fromDateSetTV);
        toDateSetTV = view.findViewById(R.id.toDateSetTV);

        databaseHelper = new DatabaseHelper(getContext());
        dataAdapter = new DataAdapter(databaseHelper,expenseList,getContext());
        expenseList = new ArrayList<>();
    }

}
