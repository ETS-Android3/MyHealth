package com.RobX.MyHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends LangSupportAppCompatActivity {

    // region Declare Global Variables
    private static int SPLASH_TIME_OUT = 5000;
    Handler handler = new Handler();
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the colour of status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        // region Calling All the Database Helpers to Initialize
        StatisticsHelper statisticsHelper = new StatisticsHelper(this);
        statisticsHelper.fetchStatistics();

        HospitalDatabaseHelper hospitalDatabaseHelper = new HospitalDatabaseHelper(this);
        try {
            hospitalDatabaseHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NewsDatabaseHelper newsDatabaseHelper = new NewsDatabaseHelper(this);
        try {
            newsDatabaseHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // endregion

        // If timeout,
        // directly go to the next activity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.network_error_toast), Toast.LENGTH_SHORT).show();
                gotoNext();
            }
        }, SPLASH_TIME_OUT);
    }

    // Go to next activity
    public void gotoNext() {
        handler.removeCallbacksAndMessages(null);

        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        boolean isLogged = sp.contains("phone");
        Intent intent;

        // If user has logged in,
        // go to Home Activity
        if (isLogged) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        }
        // If not,
        // go to Sign In Activity
        else {
            intent = new Intent(MainActivity.this, SignInActivity.class);
        }

        startActivity(intent);
        finish();
    }
}