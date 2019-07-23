package com.example.dailyexpenseproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class dashboardFragment extends Fragment {
    private View view;
    private Spinner expenseTypeSpinner;
    private LinearLayout fromDatePicker,toDatePicker;
    private TextView totalExpenseTV;
    private FloatingActionButton addNewFAB;
    private DatabaseHelper databaseHelper;

    public dashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        init();

        addNewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),addExpenseActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void init() {
        expenseTypeSpinner = view.findViewById(R.id.dashBoardExpenseTypeSPNR);
        totalExpenseTV = view.findViewById(R.id.dashBoardTotalExpenseTV);
        addNewFAB = view.findViewById(R.id.dashBoardAddExpenseBTN);
        fromDatePicker = view.findViewById(R.id.dashBoardFromDateClickTV);
        toDatePicker = view.findViewById(R.id.dashBoardToDateClickTV);

        databaseHelper = new DatabaseHelper(getContext());
    }

}
