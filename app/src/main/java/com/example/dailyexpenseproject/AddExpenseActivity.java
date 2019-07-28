package com.example.dailyexpenseproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
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
    private long dateInMS;
    private String expenseReceiptImage;
    private int request_camera=1,select_file=0,imageType;

    private DatabaseHelper databaseHelper;
    private long date;
    private String receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        init();
        spinnerLoad();

        if(getIntent().getExtras()!=null){ //update expense if intent data exists
            setTitle("Update Expense");
            addReceiptBTN.setText("Update Image");
            saveExpenseBTN.setText("Update");

            getAndSetIntentData();

            saveExpenseBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(expenseAmountET.getText().toString().isEmpty() || datePickerTV.getText().toString().isEmpty() || datePickerTV.getText().toString().isEmpty()){
                        Toast.makeText(AddExpenseActivity.this, "Enter New data", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        expenseType = expenseTypeSpinner.getSelectedItem().toString();
                        expenseTime = timePickerTV.getText().toString();
                        expenseAmount = Double.parseDouble(expenseAmountET.getText().toString());

                        databaseHelper.update(Integer.parseInt(id),expenseType,dateInMS,expenseTime,expenseAmount,expenseReceiptImage);
                        Toast.makeText(AddExpenseActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddExpenseActivity.this,MainActivity.class));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                builder.setItems(optionItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(optionItems[i].equals("Remove Image")){
                            receiptIV.setImageResource(R.drawable.ic_image_black_24dp);
                            Toast.makeText(AddExpenseActivity.this, "Image Removed", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
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
                    startActivityForResult(Intent.createChooser(intent,"Select Source"),select_file);
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
                expenseReceiptImage = encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100);

            }
            else if(requestCode == select_file){
                Uri selectImageUri = data.getData();
                receiptIV.setImageURI(selectImageUri);
                expenseReceiptImage = selectImageUri.toString();
            }
        }
    }

    private void spinnerLoad() {
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
               Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR,hour);
                time.set(Calendar.MINUTE,minute);
               CharSequence charSequence = DateFormat.format("hh:mm a",time);
               timePickerTV.setText(charSequence);
            }
        },hour,minute,is24HourFormat);
        timePickerDialog.show();
    }

    private void currentDatePicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;

                String currentDate = year + "/" + month + "/" + day;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

                Date date = null;

                try {
                    date = simpleDateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateInMS = date.getTime();
                datePickerTV.setText(simpleDateFormat.format(date));
            }
        };

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }

    public void insertNewExpenseIntoDB(View view) {
        if(expenseTypeSpinner.getSelectedItem()==null || datePickerTV.getText().toString().isEmpty() || timePickerTV.getText().toString().isEmpty() || expenseAmountET.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Expense type, Date&Time and Amount", Toast.LENGTH_SHORT).show();
        }
        else {
            expenseType = expenseTypeSpinner.getSelectedItem().toString();
            expenseDate = datePickerTV.getText().toString();
            expenseTime = timePickerTV.getText().toString();
            expenseAmount = Double.parseDouble(expenseAmountET.getText().toString());


            databaseHelper.insert(expenseType,dateInMS,expenseTime,expenseAmount,expenseReceiptImage);

            Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);

        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void getAndSetIntentData() {
        id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");
        date = getIntent().getLongExtra("date",0);
        String time = getIntent().getStringExtra("time");
        String amount = getIntent().getStringExtra("amount");
        receipt = getIntent().getStringExtra("receipt");

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy");
        String dateString = formatter.format(new Date(date));

        Bitmap bmpImage = decodeBase64(receipt);
        receiptIV.setImageBitmap(bmpImage);

        //problem for spinner
        datePickerTV.setText(dateString);
        timePickerTV.setText(time);
        expenseAmountET.setText(amount);

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
