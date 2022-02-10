package com.RobX.MyHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    // region Declare Global Variables
    private EditText phone, password;
    private DatabaseHelper DB;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Set the colour of status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.health_green));

        // region Declare and Link Variables
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        Button signin = findViewById(R.id.btnsignin);
        TextView signup = findViewById(R.id.btnsignup);
        // endregion

        // region Instantiate classes and other tools as preparation
        DB = new DatabaseHelper(this);
        // endregion

        // region Set OnClickListeners for the buttons
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // region Declare and Link Variables
                String pho = phone.getText().toString();
                String pass = password.getText().toString();
                // endregion

                // Check whether all the fields have been filled
                if (pho.equals("") || pass.equals(""))
                    Toast.makeText(SignInActivity.this, getString(R.string.enter_all_fields_toast), Toast.LENGTH_SHORT).show();
                else {
                    // Check whether phone number exists
                    if (DB.checkPhone(pho)){
                        // Check whether the password matches
                        if (DB.checkPassword(pho, pass)) {
                            Toast.makeText(SignInActivity.this, getString(R.string.sign_in_toast), Toast.LENGTH_SHORT).show();

                            // Save to SharedPreferences
                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("phone", pho);
                            editor.putString("name", DB.getUserName(pho));
                            editor.putString("age", DB.getAge(pho));
                            editor.putString("ID", DB.getID(pho));
                            editor.putString("health", DB.getHealth(pho));

                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(SignInActivity.this, getString(R.string.wrong_password_toast), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignInActivity.this, getString(R.string.user_not_exist_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // endregion
    }
}