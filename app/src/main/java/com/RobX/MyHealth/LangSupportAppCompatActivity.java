package com.RobX.MyHealth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LangSupportAppCompatActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {

        // Read Language Setting from SharedPreferences
        SharedPreferences sp = newBase.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String lang_code = sp.getString("Language", null);
        if (lang_code == null)
        {
            lang_code = "en";
        }

        // Change the language of activity
        Context context = LangUtils.changeLang(newBase, lang_code);
        super.attachBaseContext(context);
    }
}
