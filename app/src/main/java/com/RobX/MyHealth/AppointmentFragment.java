package com.RobX.MyHealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetsl.scardview.SCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class AppointmentFragment extends Fragment {

    // region Declare Global Variables
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private View view;
    // endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is to get whether user has successfully make an appointment
        // If so, it will call updateUI() to refresh this page
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            updateUI();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Set the colour of status bar
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.light_green));

        // Display all the appointments
        updateUI();

        // Inflate the layout for this fragment
        return view;
    }

    // Update the interface of appointment fragment based on latest information
    private void updateUI() {

        // region Declare and Link Variables
        LinearLayout banner = view.findViewById(R.id.appointmentBanner);
        LinearLayout pcr = view.findViewById(R.id.pcr_btn);
        LinearLayout vaccine = view.findViewById(R.id.vaccine_btn);
        SCardView no_event = view.findViewById(R.id.no_event);
        ViewGroup insertPoint = view.findViewById(R.id.insert_point);
        // endregion

        // Remove all the appointments to avoid duplication
        insertPoint.removeAllViews();

        // region Instantiate classes and other tools as preparation
        HospitalDatabaseHelper hospitalDatabaseHelper = new HospitalDatabaseHelper(getContext());
        AppointmentDatabaseHelper appointmentDatabaseHelper = new AppointmentDatabaseHelper(getContext());
        Calendar calendar = Calendar.getInstance();
        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String phone = sp.getString("phone",null);
        // endregion

        // Call the appointment database helper to get all the future appointment IDs
        Vector<String> appointmentIDs = appointmentDatabaseHelper.getChronologicalAppointmentIDs(calendar.getTimeInMillis(), phone);

        // If there is at least one appointment
        if (appointmentIDs.size() != 0) {

            // Hide the no event alert
            no_event.setVisibility(View.GONE);

            // Use a for loop to create the exact number of views
            for (int i = appointmentIDs.size() - 1; i >= 0; i --)
            {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.appointment_tab, null);
                String hospitalID = appointmentDatabaseHelper.getHospitalID(appointmentIDs.get(i));

                // region Declare and Link Variables
                ImageView image = v.findViewById(R.id.mcImage);
                TextView name = v.findViewById(R.id.mcName);
                TextView address = v.findViewById(R.id.mcAddress);
                TextView patient = v.findViewById(R.id.patientName);
                TextView type = v.findViewById(R.id.appointmentType);
                TextView date = v.findViewById(R.id.appointmentDate);
                // endregion

                // Set information and picture
                if (appointmentDatabaseHelper.getType(appointmentIDs.get(i)).equals(Integer.toString(AppointmentStatus.PCR_TEST)))
                    type.setText(getText(R.string.pcr_test));
                else
                    type.setText(getText(R.string.vaccine));
                byte[] byteArray0 = hospitalDatabaseHelper.getImage(hospitalID);
                Bitmap bitmap0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
                image.setImageBitmap(bitmap0);
                name.setText(hospitalDatabaseHelper.getName(hospitalID));
                address.setText(hospitalDatabaseHelper.getAddress(hospitalID));
                patient.setText(sp.getString("name",null));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date.setText(simpleDateFormat.format(Long.parseLong(appointmentDatabaseHelper.getDate(appointmentIDs.get(i)))));

                // Insert the newly-created view to the fragment
                insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        // region Set OnClickListeners for the buttons
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectMCActivity.class);
                intent.putExtra("isTest", false);
                someActivityResultLauncher.launch(intent);
            }
        });

        vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectMCActivity.class);
                intent.putExtra("isTest", false);
                someActivityResultLauncher.launch(intent);
            }
        });

        pcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectMCActivity.class);
                intent.putExtra("isTest", true);
                someActivityResultLauncher.launch(intent);
            }
        });
        // endregion
    }
}