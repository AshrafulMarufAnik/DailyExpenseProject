package com.example.dailyexpenseproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.net.URI;
import java.util.Calendar;

public class addExpenseActivity extends AppCompatActivity {
    private Spinner expenseTypeSpinner;
    private LinearLayout datePicker,timePicker;
    private ImageView receiptIV,deleteReceiptIV;
    private Button addReceiptBTN;
    private Button saveExpenseBTN;
    private EditText expenseAmountET;
    private TextView datePickerTV,timePickerTV;
    private String expenseType,expenseDate,expenseTime;
    private double expenseAmount;
    private String id;
    private String expenseReceiptImage;
    private int request_camera=1,select_file=0;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        init();
        spinnerLoad();

        if(getIntent().getExtras()!=null){
            setTitle("Update Expense");
            addReceiptBTN.setText("Update Image");
            saveExpenseBTN.setText("Update");

            getAndSetIntentData();

            saveExpenseBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(expenseAmountET.getText().toString().isEmpty() || datePickerTV.getText().toString().isEmpty() || datePickerTV.getText().toString().isEmpty()){
                        Toast.makeText(addExpenseActivity.this, "Enter New data", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        expenseType = expenseTypeSpinner.getSelectedItem().toString();
                        expenseDate = datePickerTV.getText().toString();
                        expenseTime = timePickerTV.getText().toString();
                        expenseAmount = Double.parseDouble(expenseAmountET.getText().toString());

                        databaseHelper.update(Integer.parseInt(id),expenseType,expenseDate,expenseTime,expenseAmount);
                        Toast.makeText(addExpenseActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(addExpenseActivity.this,MainActivity.class));
                        finish();
                    }
                }
            });
        }


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

        addReceiptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReceiptImage();
            }
        });

        deleteReceiptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence [] optionItems = {"Remove Image","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(addExpenseActivity.this);
                builder.setItems(optionItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(optionItems[i].equals("Remove Image")){
                            receiptIV.setImageResource(R.drawable.ic_image_black_24dp);
                            Toast.makeText(addExpenseActivity.this, "Image Removed", Toast.LENGTH_SHORT).show();
                        }
                        else if(optionItems[i].equals("Cancel")){
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

    }

    private void selectReceiptImage() {
        final CharSequence [] optionItems = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(addExpenseActivity.this);
        builder.setTitle("Choose Image Source");
        builder.setItems(optionItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionItems[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,request_camera);

                }
                else if(optionItems[i].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Select File"),select_file);

                }
                else if(optionItems[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == request_camera){
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                receiptIV.setImageBitmap(bmp);
                expenseReceiptImage = String.valueOf(bmp);
            }
            else if(requestCode == select_file){
                Uri selectImageUri = data.getData();
                receiptIV.setImageURI(selectImageUri);
                expenseReceiptImage = String.valueOf(selectImageUri);
            }
        }
    }

    private void spinnerLoad() {
        String expenseTypes [] = getResources().getStringArray(R.array.expenseType);
        ArrayAdapter expenseTypeArrayAdapter = ArrayAdapter.createFromResource(this,R.array.expenseType,android.R.layout.simple_spinner_item);
        expenseTypeSpinner.setAdapter(expenseTypeArrayAdapter);
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


    public void insertNewExpenseIntoDB(View view) {
        if(expenseTypeSpinner.getSelectedItem()==null || datePickerTV.getText().toString().isEmpty() || timePickerTV.getText().toString().isEmpty() || expenseAmountET.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Expense type, Date, Time and Amount", Toast.LENGTH_SHORT).show();
        }
        else {
            expenseType = expenseTypeSpinner.getSelectedItem().toString();
            expenseDate = datePickerTV.getText().toString();
            expenseTime = timePickerTV.getText().toString();
            expenseAmount = Double.parseDouble(expenseAmountET.getText().toString());
            expenseReceiptImage = String.valueOf(receiptIV.getImageAlpha());

            databaseHelper.insert(expenseType,expenseDate,expenseTime,expenseAmount,expenseReceiptImage);

            Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private void getAndSetIntentData() {
        id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String amount = getIntent().getStringExtra("amount");

        //problem for spinner
        datePickerTV.setText(date);
        timePickerTV.setText(time);
        expenseAmountET.setText(amount);

    }

    private void getIntentData() {

    }

    private void init() {
        expenseTypeSpinner = findViewById(R.id.addNewExpenseTypeSPNR);
        datePicker = findViewById(R.id.addNewExpenseDateClickTV);
        timePicker = findViewById(R.id.addNewExpenseTimeClickTV);
        datePickerTV = findViewById(R.id.datePickerTV);
        timePickerTV = findViewById(R.id.timePickerTV);
        saveExpenseBTN = findViewById(R.id.saveExpenseBTN);
        expenseAmountET = findViewById(R.id.addNewExpenseAmountET);
        receiptIV = findViewById(R.id.addNewExpenseReceiptIV);
        deleteReceiptIV = findViewById(R.id.deleteReceiptIV);
        addReceiptBTN = findViewById(R.id.addNewExpenseReceiptBTN);

        databaseHelper = new DatabaseHelper(this);
    }

}
