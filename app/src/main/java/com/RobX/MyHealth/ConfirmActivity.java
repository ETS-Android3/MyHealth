package com.RobX.MyHealth;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ConfirmActivity extends AppCompatActivity {

    // region Declare Global Variables
    private TextView dateText;
    private TextView timeText;


    HospitalDatabaseHelper hospitalDatabaseHelper = new HospitalDatabaseHelper(this);
    AppointmentDatabaseHelper appointmentDatabaseHelper = new AppointmentDatabaseHelper(this);

    private String MCIndex;
    private boolean isTest;
    private boolean hasDate = false;
    private boolean hasTime = false;

    private Calendar selectedDate = Calendar.getInstance();

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        // Set the colour of status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.health_green));

        // region Set Up the Back Button at the top left corner
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // endregion

        // Get the index of selected medical centre and type of appointment from calling Intent
        Intent callingIntent = getIntent();
        MCIndex = callingIntent.getStringExtra("MCIndex");
        isTest = callingIntent.getBooleanExtra("isTest", false);

        // region Declare and Link Variables
        dateText = findViewById(R.id.datePicker);
        timeText = findViewById(R.id.timePicker);
        Button confirmBtn = findViewById(R.id.btn_confirmButton);
        ImageView image = findViewById(R.id.mcImage);
        TextView nameText = findViewById(R.id.mcName);
        TextView addressText = findViewById(R.id.mcAddress);
        TextView phoneText = findViewById(R.id.mcPhone);
        TextView typeText = findViewById(R.id.type);
        // endregion

        // region Instantiate classes and other tools as preparation
        MaterialDatePicker.Builder<Long> dateBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.now();

        constraintsBuilder.setValidator(dateValidatorMin);
        dateBuilder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker datePicker = dateBuilder.build();

        MaterialTimePicker.Builder timeBuilder = new MaterialTimePicker.Builder();
        MaterialTimePicker timePicker = timeBuilder.build();
        // endregion

        // Set Information and Picture
        byte[] byteArray0 = hospitalDatabaseHelper.getImage(MCIndex);
        Bitmap bitmap0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
        image.setImageBitmap(bitmap0);
        nameText.setText(hospitalDatabaseHelper.getName(MCIndex));
        addressText.setText(hospitalDatabaseHelper.getAddress(MCIndex));
        phoneText.setText(hospitalDatabaseHelper.getPhone(MCIndex));
        typeText.setText(isTest ? getString(R.string.pcr_test) : getString(R.string.vaccine));

        // region When users select a time or date, save it
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateText.setText(simpleFormat.format(selection));
                selectedDate.setTimeInMillis((Long)selection);
                hasDate = true;
            }
        });

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeText.setText(pad(timePicker.getHour()) + ":" + pad(timePicker.getMinute()));
                selectedDate.set(Calendar.HOUR, timePicker.getHour());
                selectedDate.set(Calendar.MINUTE, timePicker.getMinute());
                hasTime = true;
            }
        });

        //endregion

        // region Set OnClickListeners for the buttons
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), datePicker.getTag());
            }
        });

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getSupportFragmentManager(), timePicker.getTag());
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String phone;
                phone = sp.getString("phone",null);

                // Check whether both time and date have been selected
                if (hasTime && hasDate) {
                    if (isTest)
                    {
                        if (appointmentDatabaseHelper.insertData(phone, MCIndex, Long.toString(selectedDate.getTimeInMillis()), Integer.toString(AppointmentStatus.PCR_TEST), null))
                        {
                            Toast.makeText(ConfirmActivity.this, getString(R.string.submit_toast), Toast.LENGTH_SHORT).show();
                            // Tell calling function the result is ok
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    }
                    else
                    {
                        if (appointmentDatabaseHelper.insertData(phone, MCIndex, Long.toString(selectedDate.getTimeInMillis()), Integer.toString(AppointmentStatus.VACCINATION), null))
                        {
                            Toast.makeText(ConfirmActivity.this, getString(R.string.submit_toast), Toast.LENGTH_SHORT).show();
                            // Tell calling function the result is ok
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    }
                }
                else if (!hasDate) {
                    Toast.makeText(ConfirmActivity.this, getString(R.string.select_date_toast), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ConfirmActivity.this, getString(R.string.select_time_toast), Toast.LENGTH_SHORT).show();
                }

            }
        });
        // endregion
    }

    // Add leading zeros
    public String pad(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }
}