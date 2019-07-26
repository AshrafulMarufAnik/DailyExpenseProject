package com.example.dailyexpenseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private DatabaseHelper databaseHelper;
    private ArrayList<Expense> expenseList;
    private Context context;

    public DataAdapter(DatabaseHelper databaseHelper, ArrayList<Expense> expenseList, Context context) {
        this.databaseHelper = databaseHelper;
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_expense_item_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense currentExpense = expenseList.get(position);
        holder.showExpenseTypeTV.setText(currentExpense.getType());
        holder.showExpenseDateTV.setText(currentExpense.getDate());
        holder.showExpenseTimeTV.setText(currentExpense.getTime());
        holder.showExpenseAmountTV.setText(currentExpense.getAmount()+"Taka");


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView showExpenseTypeTV,showExpenseDateTV,showExpenseTimeTV,showExpenseAmountTV;
        private ImageView optionMenuIV,receiptImageIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showExpenseTypeTV = itemView.findViewById(R.id.showSingleExpenseTypeTV);
            showExpenseDateTV = itemView.findViewById(R.id.showSingleExpenseDateTV);
            showExpenseTimeTV = itemView.findViewById(R.id.showSingleExpenseTimeTV);
            showExpenseAmountTV = itemView.findViewById(R.id.showSingleExpenseAmountTV);
            receiptImageIV = itemView.findViewById(R.id.showSingleExpenseReceiptPreviewIV);
            optionMenuIV = itemView.findViewById(R.id.showExpenseOptionMenuIV);
        }
    }
}
