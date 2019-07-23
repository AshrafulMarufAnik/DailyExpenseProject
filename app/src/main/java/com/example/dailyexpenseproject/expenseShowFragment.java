package com.example.dailyexpenseproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class expenseShowFragment extends Fragment {
    private View view;
    private Spinner expenseTypeSpinner;
    private LinearLayout fromDatePicker,toDatePicker;
    private FloatingActionButton addExpenseFAB;

    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<expense> expenseList;
    private RecyclerView expenseRV;

    public expenseShowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_expense_show,container,false);

        init();
        getDataFromDB();
        configExpenseRV();

        addExpenseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),addExpenseActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getDataFromDB() {

    }

    private void configExpenseRV() {

    }

    private void init() {
        expenseTypeSpinner = view.findViewById(R.id.expenseTypeSPNR);
        addExpenseFAB = view.findViewById(R.id.addExpenseActivityBTN);
        fromDatePicker = view.findViewById(R.id.fromDateClickTV);
        toDatePicker = view.findViewById(R.id.toDateClickTV);

        expenseList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());
        dataAdapter = new DataAdapter(databaseHelper,expenseList,getContext());
    }


}
