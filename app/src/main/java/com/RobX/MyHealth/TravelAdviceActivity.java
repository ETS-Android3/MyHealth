package com.RobX.MyHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class TravelAdviceActivity extends LangSupportAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_advice);

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
        EditText editText = findViewById(R.id.etSearch);
        ImageButton searchButton = findViewById(R.id.btnSearch);
        CardView australia = findViewById(R.id.australia);
        CardView canada = findViewById(R.id.canada);
        CardView china = findViewById(R.id.china);
        CardView france = findViewById(R.id.france);
        CardView germany = findViewById(R.id.germany);
        CardView india = findViewById(R.id.india);
        CardView mexico = findViewById(R.id.mexico);
        CardView thailand = findViewById(R.id.thailand);
        CardView UK = findViewById(R.id.uk);
        // endregion

        // region Set OnClickListeners for the buttons
        australia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "australia");
                startActivity(intent);
            }
        });

        canada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "canada");
                startActivity(intent);
            }
        });

        china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "china");
                startActivity(intent);
            }
        });

        france.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "france");
                startActivity(intent);
            }
        });

        germany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "germany");
                startActivity(intent);
            }
        });

        india.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "india");
                startActivity(intent);
            }
        });

        mexico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "mexico");
                startActivity(intent);
            }
        });

        thailand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "thailand");
                startActivity(intent);
            }
        });

        UK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the country name to the next activity
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", "united-kingdom");
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read from the editText and pass to the next activity
                String input = editText.getText().toString();
                Intent intent = new Intent(getApplicationContext(), TravelAdviceWebViewActivity.class);
                intent.putExtra("country", input);
                startActivity(intent);
            }
        });
        // endregion
    }
}