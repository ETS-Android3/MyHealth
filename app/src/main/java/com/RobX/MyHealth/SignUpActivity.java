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
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    // region Declare Global Variables
    private EditText phone, password, repassword, name, age, idnumber;
    private DatabaseHelper DB;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set the colour of status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.health_green));

        // region Declare and Link Variables
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        idnumber = findViewById(R.id.idnumber);
        Button signup = findViewById(R.id.btnsignup);
        // endregion

        // region Instantiate classes and other tools as preparation
        DB = new DatabaseHelper(this);
        // endregion

        // region Set OnClickListeners for the buttons
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // region Declare and Link Variables
                String phoneNo = phone.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String n = name.getText().toString();
                String a = age.getText().toString();
                String id = idnumber.getText().toString();
                // endregion

                // Check whether all the fields have been filled
                if (phoneNo.equals("") || pass.equals("") || repass.equals("") || n.equals("") ||  a.equals("") || id.equals(""))
                    Toast.makeText(SignUpActivity.this, getString(R.string.enter_all_fields_toast), Toast.LENGTH_SHORT).show();
                else {
                    // Check whether the passwords are the same
                    if (pass.equals(repass)) {
                        // Check whether phone number exists
                        if (!DB.checkPhone(phoneNo)) {
                            // Save to both Database and SharedPreferences
                            if (DB.insertData(phoneNo, pass, n, a, id)) {
                                Toast.makeText(SignUpActivity.this, getString(R.string.sign_up_toast), Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("phone", phoneNo);
                                editor.putString("name", n);
                                editor.putString("age", a);
                                editor.putString("ID", id);
                                editor.putString("health", Integer.toString(HealthStatus.STATUS_UNKNOWN));

                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("phone", phone.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                            else
                                Toast.makeText(SignUpActivity.this, getString(R.string.register_fail_toast), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SignUpActivity.this, getString(R.string.user_exist_toast), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignUpActivity.this, getString(R.string.password_not_match_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}