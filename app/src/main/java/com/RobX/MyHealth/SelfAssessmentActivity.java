package com.RobX.MyHealth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SelfAssessmentActivity extends AppCompatActivity {

    // region Declare Global Variables
    private TextView nameText;
    private TextView addressText;
    private TextView cityText;
    private TextView stateText;
    private EditText temText;
    private CheckBox agree;
    private RadioGroup radioGroup_q1;
    private RadioGroup radioGroup_q2;
    private RadioGroup radioGroup_q3;
    private Button confirm;

    private int stateIndex=-1;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assessment);

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
        nameText = findViewById(R.id.name);
        addressText = findViewById(R.id.addressChosen);
        cityText = findViewById(R.id.cityChosen);
        stateText = findViewById(R.id.stateChosen);
        temText = findViewById(R.id.tem);
        agree = findViewById(R.id.id_checkbox_confirm);
        radioGroup_q1 = findViewById(R.id.radioGroup_q1);
        radioGroup_q2 = findViewById(R.id.radioGroup_q2);
        radioGroup_q3 = findViewById(R.id.radioGroup_q3);
        confirm = findViewById(R.id.btn_confirmButton);
        // endregion

        // region Instantiate classes and other tools as preparation
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        nameText.setText(sp.getString("name",null));
        SharedPreferences.Editor editor = sp.edit();
        DatabaseHelper DB = new DatabaseHelper(this);
        // endregion

        // region Set OnClickListeners for the buttons
        stateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectState();
            }
        });

        cityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCity();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check whether the agree checkbox has been checked
                if (agree.isChecked()) {
                    // Check whether all the fields have been filled
                    if (temText.getText().toString().equals("") || nameText.getText().toString().equals("") || addressText.getText().toString().equals("") || cityText.getText().toString().equals("") || stateText.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.enter_all_fields_toast), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String status = checkHealth();
                        DB.setHealth(sp.getString("phone", null), status);
                        editor.putString("health", status);
                        editor.commit();
                        finish();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.agree_terms_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // endregion
    }

    // Build the state selection window
    public void selectState()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the State");
        final String[] items = {"Johor", "Kedah", "Kelantan", "Malacca", "Sembilan", "Pahang","Penang","Perak","Perlis","Selangor","Terengganu","Sabah","Sarawak"};

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = items[which];
                stateIndex = which;
                stateText.setText(item);
                cityText.setText("");
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Build the city selection window
    public void selectCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the City");
        String [] items;

        if(stateIndex == 0){
            items = new String[]{ "Batu Pahat", "Endau", "Gelang Patah","Iskandar Puteri", "Johor Bahru", "Pasir Gudang", "Bakri", "Batu Pahat", "Buloh Kasap", "Chaah"};
        }
        else if(stateIndex == 1){
            items = new String[]{"Langkawi", "Alor Setar", "Kuah", "Kulim", "Sintok", "Jitra","Kampung Kok","Kampung Lubuk Buaya","Sungai Petani","Sungai Petani"};
        }
        else if(stateIndex == 2){
            items = new String[] {"Kota Baharu", "Bachok", "Tumpat", "Jelawat", "Kampong Guntong", "Gua Musang","Kampong Cham Tangga","Kampong Keldong","Kampong Badang","Pasir Mas"};
        }
        else if(stateIndex == 3){
            items = new String[] {"Alor Gajah","Asahan","Ayer Keroh","Bemban","Durian Tunggal","Jasin","Kem Trendak","Kuala Sungai Baru","Lubok China","Masjid Tanah"};
        }
        else if(stateIndex == 4){
            items = new String[] {"Bahau","Kuala Klawang","Kuala Pilah","Nilai","Port Dickson","Seremban","Tampin","Si Rusa","Bahau","Labu"};
        }
        else if(stateIndex == 5){
            items = new String[] {"Ringlet", "Kuantan", "Tanah Rata", "Cherating", "Cameron Highlands", "Raub","Kampong Lebu","Kampung Genting","Kuala Rompin","Kampung Paya Bungur"};
        }
        else if(stateIndex == 6){
            items = new String[] {"George Town", "Jelutong", "Gelugor","Bayan Lepas","Balik Pulau","Teluk Kumbar","Pulau Tikus","Taman Jajar","Sungai Ara","Paya Terubong"};
        }
        else if(stateIndex == 7){
            items = new String[] {"Bagan Serai","Batu Gajah","Bidor","Ipoh","Kampar","Kuala Kangsar","Lumut","Pantai Remis","Parit Buntar","Simpang Empat"};
        }
        else if(stateIndex == 8){
            items = new String[] {"Arau","Kaki Bukit","Kangar","Kuala Perlis","Padang Besar","Simpang Ampat"};
        }
        else if(stateIndex == 9){
            items = new String[] {"Sabak Bernam", "Hulu Selangor", "Kuala Selangor", "Gombak", "Klang", "Petaling","Hulu Langat","Kuala Langat","Sepang","Rawang"};
        }
        else if(stateIndex == 10){
            items = new String[] {"Ajil","Al Muktatfi Billah Shah","Ayer Puteh","Bukit Besi","Bukit Payong","Ceneh","Chalok","Cukai","Dungun","Jerteh"};
        }
        else if(stateIndex == 11){
            items = new String[] {"Beaufort","Beluran","Beverly","Bongawan","Inanam","Keningau","Kota Belud","Kota Kinabalu","Kota Kinabatangan","Kota Marudu"};
        }
        else if(stateIndex == 12){
            items = new String[] {"Asajaya","Balingian","Baram","Bekenu","Belaga","Belaga","Belawai","Betong","Bintangor","Bintulu"};
        }
        else{
            items = new String[]{};
        }

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = items[which];
                cityText.setText(item);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Check whether the user is healthy or not based on the answers
    public String checkHealth() {
        int status;
        if (radioGroup_q1.getCheckedRadioButtonId() == R.id.radioButton_no1
                && radioGroup_q2.getCheckedRadioButtonId() == R.id.radioButton_no2
                && radioGroup_q3.getCheckedRadioButtonId() == R.id.radioButton_no3
                && Float.parseFloat(temText.getText().toString()) <= 37.8) {
            status = HealthStatus.STATUS_HEALTHY;
        }
        else {
            status = HealthStatus.STATUS_WARNING;
        }
        return Integer.toString(status);
    }
}
