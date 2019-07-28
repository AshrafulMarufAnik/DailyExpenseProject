package com.example.dailyexpenseproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private DatabaseHelper databaseHelper;
    private ArrayList<Expense> expenseList;
    private Context context;
    private AddExpenseActivity helper = new AddExpenseActivity();

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Expense currentExpense = expenseList.get(position);

        String receipt = currentExpense.getReceipt();

        Bitmap bmpImage = decodeBase64(receipt);
        holder.receiptImageIV.setImageBitmap(bmpImage);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy");
        String dateString = formatter.format(new Date(currentExpense.getDate()));

        holder.showExpenseTypeTV.setText(currentExpense.getType());
        holder.showExpenseDateTV.setText(dateString);
        holder.showExpenseTimeTV.setText(currentExpense.getTime());
        holder.showExpenseAmountTV.setText(currentExpense.getAmount()+" Tk");

        //Uri uriImage = Uri.parse(currentExpense.getReceipt());
        //holder.receiptImageIV.setImageURI(uriImage);



        holder.optionMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.optionMenuIV);
                popupMenu.inflate(R.menu.item_option_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.updateMenu){
                            Intent intent = new Intent(context, AddExpenseActivity.class);
                            intent.putExtra("id",String.valueOf(currentExpense.getId()));
                            intent.putExtra("type",currentExpense.getType());
                            intent.putExtra("date",currentExpense.getDate());
                            intent.putExtra("time",currentExpense.getTime());
                            intent.putExtra("amount",String.valueOf(currentExpense.getAmount()));
                            intent.putExtra("receipt",currentExpense.getReceipt());
                            context.startActivity(intent);
                        }
                        else if(menuItem.getItemId()==R.id.detailsMenu)
                        {
                            Intent intent = new Intent(context, DetailsActivity.class);
                            intent.putExtra("id",String.valueOf(currentExpense.getId()));
                            intent.putExtra("type",currentExpense.getType());
                            intent.putExtra("date",currentExpense.getDate());
                            intent.putExtra("time",currentExpense.getTime());
                            intent.putExtra("amount",String.valueOf(currentExpense.getAmount()));
                            intent.putExtra("receipt",currentExpense.getReceipt());
                            context.startActivity(intent);
                        }
                        else if(menuItem.getItemId()==R.id.deleteMenu){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setCancelable(false);
                            builder.setTitle("Delete");
                            builder.setMessage("Are you sure?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseHelper.delete(currentExpense.getId());
                                    expenseList.remove(position);
                                    Toast.makeText(context, "Expense Deleted", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.create().show();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
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

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
