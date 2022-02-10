package com.RobX.MyHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends LangSupportAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // region Set the Navigation Bar
        BottomNavigationView navView = findViewById(R.id.bottomNav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.appointmentFragment, R.id.codeFragment, R.id.articleFragment, R.id.meFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);
        // endregion

        // Since Health Code in the navigation bar is manually added,
        // we have to manually change the colour to show whether it is selected
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, Bundle arguments) {
                if(destination.getId() == R.id.codeFragment)
                {
                    selectCode(true);
                }
                else
                {
                    selectCode(false);
                }
            }
        });
    }

    // Change the colour of the Health Code image
    public void selectCode(Boolean isSelected) {
        ImageView codeImage = findViewById(R.id.codeIcon);
        if (isSelected)
        {
            codeImage.setBackground(this.getDrawable(R.drawable.ic_baseline_qr_code_24_selected));
        }
        else
        {
            codeImage.setBackground(this.getDrawable(R.drawable.ic_baseline_qr_code_24));
        }
    }
}