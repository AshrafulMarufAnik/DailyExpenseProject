package com.example.dailyexpenseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    private TextView typeTV,dateTV,timeTV,amountTV;
    private ImageView imageIV;
    private DatabaseHelper databaseHelper;
    private DataAdapter dataAdapter;
    private ArrayList<Expense> expenseList;
    private int id;
    private String type;
    private String time;
    private String amount;
    private String receipt;
    private long date;
    private int receiptType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setTitle("Expense Details");
        init();
        getAndSetIntentData();

        imageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(DetailsActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                });

                ImageView imageView = new ImageView(DetailsActivity.this);
                Bitmap bmpImage = decodeBase64(receipt);
                imageView.setImageBitmap(bmpImage);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(700, 700));
                builder.show();
            }
        });

    }

    private void getAndSetIntentData() {
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        type = getIntent().getStringExtra("type");
        date = getIntent().getLongExtra("date",0);
        time = getIntent().getStringExtra("time");
        amount = getIntent().getStringExtra("amount");
        receipt = getIntent().getStringExtra("receipt");
        receiptType = getIntent().getIntExtra("receiptType",0);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy");
        String dateString = formatter.format(new Date(date));

        if(receiptType==1){
            Bitmap bmpImage = decodeBase64(receipt);
            imageIV.setImageBitmap(bmpImage);
            typeTV.setText(type);
            dateTV.setText(dateString);
            timeTV.setText(time);
            amountTV.setText(amount);
        }
        else if(receiptType==2){
            Uri uriImage = Uri.parse(receipt);
            imageIV.setImageURI(uriImage);
            typeTV.setText(type);
            dateTV.setText(dateString);
            timeTV.setText(time);
            amountTV.setText(amount);
        }
        else{
            typeTV.setText(type);
            dateTV.setText(dateString);
            timeTV.setText(time);
            amountTV.setText(amount);

        }

    }

    private void init() {
        typeTV = findViewById(R.id.typeTV);
        timeTV = findViewById(R.id.timeTV);
        dateTV = findViewById(R.id.dateTV);
        amountTV = findViewById(R.id.amountTV);
        imageIV = findViewById(R.id.imageIV);
        databaseHelper = new DatabaseHelper(this);
        expenseList = new ArrayList<>();
        dataAdapter = new DataAdapter(databaseHelper,expenseList,this);
    }

    public void updateNewData(View view) {
        Intent intent = new Intent(DetailsActivity.this, AddExpenseActivity.class);
        intent.putExtra("id",String.valueOf(id));
        intent.putExtra("type",type);
        intent.putExtra("date",date);
        intent.putExtra("time",time);
        intent.putExtra("amount",String.valueOf(amount));
        intent.putExtra("receipt",receipt);
        intent.putExtra("receiptType",receiptType);
        startActivity(intent);
    }

    public void deleteData(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.delete(id);
                updateDataList();
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailsActivity.this,MainActivity.class));
                finish();

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

    private void updateDataList() {
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

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
