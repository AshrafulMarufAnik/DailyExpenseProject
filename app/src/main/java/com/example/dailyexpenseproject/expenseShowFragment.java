package com.example.dailyexpenseproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * A simple {@link Fragment} subclass.
 */
public class expenseShowFragment extends Fragment {

    private FloatingActionButton addExpenseFABtn;
    private Context context;

    public expenseShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_show, container, false);

    }

    private void gotoAddExpenseActivity(){

        addExpenseFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,addExpenseActivity.class);
                startActivity(intent);
                intent.putExtra("add","Add new Expense here");

            }
        });
    }


}
