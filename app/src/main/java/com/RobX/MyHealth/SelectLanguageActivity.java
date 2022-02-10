package com.RobX.MyHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.internal.ContextUtils;

import java.util.Locale;

public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

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

        // region Declare and Link Variables
        CardView china = findViewById(R.id.zh_cn);
        CardView uk = findViewById(R.id.english);
        CardView malaysia = findViewById(R.id.bm);
        // endregion

        // region Instantiate classes and other tools as preparation
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String currLanguage = sp.getString("Language", "en");
        // endregion

        // Hide current language from the list
        if (currLanguage.equals("zh")) {
            china.setVisibility(View.GONE);
        }

        if (currLanguage.equals("en")) {
            uk.setVisibility(View.GONE);
        }

        if (currLanguage.equals("ms")) {
            malaysia.setVisibility(View.GONE);
        }

        // region Set OnClickListeners for the buttons
        china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Language", "zh");
                editor.commit();

                // Tell calling function the result is ok
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        uk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Language", "en");
                editor.commit();

                // Tell calling function the result is ok
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        malaysia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Language", "ms");
                editor.commit();

                // Tell calling function the result is ok
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        // endregion
    }
}