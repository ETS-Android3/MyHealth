package com.RobX.MyHealth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class SelectMCActivity extends AppCompatActivity implements LocationListener {

    // region Declare Global Variables
    LocationManager locationManager;
    Location currentLocation = new Location("currentLocation");
    float[][] distance;
    HospitalDatabaseHelper hospitalDatabaseHelper = new HospitalDatabaseHelper(this);
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mc);

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

        // This is to get whether user has successfully make an appointment
        // If so, it will call tell its own calling function the result is ok
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    }
                });

        // Set up the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            finish();
        }
        else
        {
            List<String> providers = locationManager.getProviders(true);
            String locationProvider;
            if(providers.contains(LocationManager.GPS_PROVIDER)){
                locationProvider = LocationManager.GPS_PROVIDER;
            }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                Toast.makeText(this, getString(R.string.location_not_available_toast), Toast.LENGTH_SHORT).show();
                return;
            }
            locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null)
        {
            currentLocation.setLatitude(location.getLatitude());
            currentLocation.setLongitude(location.getLongitude());
        }
        //Toast.makeText(SelectMCActivity.this,"Latitude:" + currentLocation.getLatitude() + "\n Longitude:" + currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
        locationManager.removeUpdates(this);

        getHospitalDistance();
    }

    // Get all the distances between user and the medical centre
    public void getHospitalDistance() {

        Vector<Location> locations = hospitalDatabaseHelper.getLocation();
        distance = new float[2][locations.size()];

        for (int i = 0; i < locations.size(); i ++)
        {
            float dist = (currentLocation.distanceTo(locations.get(i)) / 1000);
            String distString = String.format("%.2f", dist);
            dist = Float.parseFloat(distString);
            distance[0][i] = dist;
            distance[1][i] = (float)i;
        }
        sortDistance(locations.size());
    }

    // Sort all the distances
    public void sortDistance(int length) {
        for (int i = 1; i < length; i++) {
            for (int j = 0; j < length - i; j++) {
                if (distance[0][j] > distance[0][j + 1]) {
                    float temp0, temp1;
                    temp0 = distance[0][j];
                    temp1 = distance[1][j];
                    distance[0][j] = distance[0][j + 1];
                    distance[1][j] = distance[1][j + 1];
                    distance[0][j + 1] = temp0;
                    distance[1][j + 1] = temp1;
                }
            }
        }
        setView(length);
    }

    // Set the views of all the medical centres
    public void setView(int length) {

        // Use a for loop to create the exact number of views
        for (int i = length - 1; i >= 0; i --) {
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.mc_tab, null);

            // region Declare and Link Variables
            LinearLayout mcLayout = v.findViewById(R.id.mc);
            ImageView mcI = v.findViewById(R.id.mcImage);
            TextView mcN = v.findViewById(R.id.mcName);
            TextView mcA = v.findViewById(R.id.mcAddress);
            TextView mcD = v.findViewById(R.id.mcDistance);
            // endregion

            // Calling function to set information and picture
            setMCData(i, mcI, mcN, mcA, mcD);
            int finalI = i;
            mcLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callingIntent = getIntent();
                    boolean isTest = callingIntent.getBooleanExtra("isTest", false);

                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                    intent.putExtra("MCIndex", Integer.toString(Math.round(distance[1][finalI])));
                    intent.putExtra("isTest", isTest);
                    //startActivity(intent);
                    someActivityResultLauncher.launch(intent);
                }
            });

            // Insert the newly-created view to the fragment
            ViewGroup insertPoint = findViewById(R.id.insert_point);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    // Set information and picture
    public void setMCData(int index, ImageView image, TextView name, TextView address, TextView dist) {
        byte[] byteArray0 = hospitalDatabaseHelper.getImage(Integer.toString(Math.round(distance[1][index])));
        Bitmap bitmap0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
        image.setImageBitmap(bitmap0);
        name.setText(hospitalDatabaseHelper.getName(Integer.toString((int)distance[1][index])));
        address.setText(hospitalDatabaseHelper.getAddress(Integer.toString((int)distance[1][index])));
        dist.setText(getString(R.string.distance, Float.toString(distance[0][index])));
    }
}