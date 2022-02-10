package com.RobX.MyHealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set the colour of status bar
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.health_green));

        // region Declare and Link Variables
        TextView more_btn = view.findViewById(R.id.more_btn);
        TextView updateDate = view.findViewById(R.id.updateDate);

        TextView totalConfirmed = view.findViewById(R.id.confirmedText);
        TextView newConfirmed = view.findViewById(R.id.newConfirmedText);
        TextView totalRecovered = view.findViewById(R.id.recoveredText);
        TextView newRecovered = view.findViewById(R.id.newRecoveredText);
        TextView totalActive = view.findViewById(R.id.activeText);
        TextView newActive = view.findViewById(R.id.newActiveText);
        TextView totalDeath = view.findViewById(R.id.deathText);
        TextView newDeath = view.findViewById(R.id.newDeathText);

        LinearLayout selfAssessment = view.findViewById(R.id.selfAssessment_btn);
        LinearLayout travelAdvice = view.findViewById(R.id.travelAdvice_btn);
        LinearLayout pandemicMap = view.findViewById(R.id.pandemicMap_btn);
        LinearLayout FAQ = view.findViewById(R.id.FAQ_btn);

        SCardView no_event = view.findViewById(R.id.no_event);
        SCardView appointment = view.findViewById(R.id.appointment);
        ImageView image = view.findViewById(R.id.mcImage);
        TextView name = view.findViewById(R.id.mcName);
        TextView patient = view.findViewById(R.id.patientName);
        TextView type = view.findViewById(R.id.appointmentType);
        TextView date = view.findViewById(R.id.appointmentDate);
        // endregion

        // region Instantiate classes and other tools as preparation
        Calendar calendar = Calendar.getInstance();
        HospitalDatabaseHelper hospitalDatabaseHelper = new HospitalDatabaseHelper(getContext());
        AppointmentDatabaseHelper appointmentDatabaseHelper = new AppointmentDatabaseHelper(getContext());
        NewsDatabaseHelper newsDatabaseHelper = new NewsDatabaseHelper(getContext());
        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String phone = sp.getString("phone",null);
        patient.setText(sp.getString("name",null));
        String minIndex = appointmentDatabaseHelper.getNextAppointmentID(calendar.getTimeInMillis(), phone);
        String lastResult = appointmentDatabaseHelper.getLastPCRTestResult(calendar.getTimeInMillis(), phone);
        String hospitalID = appointmentDatabaseHelper.getHospitalID(minIndex);
        // endregion

        // Call the function to display statistics data
        setStatistics(updateDate, totalConfirmed,newConfirmed, totalRecovered, newRecovered, totalActive, newActive, totalDeath, newDeath);

        // If there is an upcoming appointment
        if (!minIndex.equals(""))
        {
            // Hide the no event alert
            no_event.setVisibility(View.GONE);

            // Set Information and Picture
            byte[] byteArray0 = hospitalDatabaseHelper.getImage(hospitalID);
            Bitmap bitmap0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
            Drawable d0 = new BitmapDrawable(getResources(),bitmap0);
            image.setBackground(d0);
            name.setText(hospitalDatabaseHelper.getName(hospitalID));
            if (appointmentDatabaseHelper.getType(minIndex).equals(Integer.toString(AppointmentStatus.PCR_TEST)))
                type.setText(getText(R.string.pcr_test));
            else
                type.setText(getText(R.string.vaccine));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setText(simpleDateFormat.format(Long.parseLong(appointmentDatabaseHelper.getDate(minIndex))));
        }
        // If there is no upcoming event
        else {
            // Hide the appointment tab
            appointment.setVisibility(View.GONE);
        }

        // Set up the latest two articles
        String latestID = newsDatabaseHelper.getLatestPSID();
        TextView articleTitle1 = view.findViewById(R.id.articleTitle1);
        TextView articleDate1 = view.findViewById(R.id.articleDate1);
        LinearLayout article1 = view.findViewById(R.id.article1);
        articleTitle1.setText(newsDatabaseHelper.getPSTitle(latestID));
        articleDate1.setText(newsDatabaseHelper.getPSDate(latestID));
        article1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ArticleTextActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("id", latestID);
                startActivity(intent);
            }
        });

        String secondLatestID = Integer.toString(Integer.parseInt(latestID) - 1);
        TextView articleTitle2 = view.findViewById(R.id.articleTitle2);
        TextView articleDate2 = view.findViewById(R.id.articleDate2);
        LinearLayout article2 = view.findViewById(R.id.article2);
        articleTitle2.setText(newsDatabaseHelper.getPSTitle(secondLatestID));
        articleDate2.setText(newsDatabaseHelper.getPSDate(secondLatestID));
        article2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ArticleTextActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("id", secondLatestID);
                startActivity(intent);
            }
        });

        // If the result of user's last PCR Test is positive,
        // set the health status to dangerous
        if (lastResult.equals(Integer.toString(AppointmentStatus.PCR_POSITIVE)))
        {
            DatabaseHelper DB = new DatabaseHelper(getContext());
            DB.setHealth(sp.getString("phone", null), Integer.toString(HealthStatus.STATUS_DANGEROUS));
            editor.putString("health", Integer.toString(HealthStatus.STATUS_DANGEROUS));
            editor.commit();
        }
        // If user's latest PCR Test is no longer positive,
        // set the health status to unknown
        else if (sp.getString("health", null).equals(Integer.toString(HealthStatus.STATUS_DANGEROUS)) && lastResult.equals(Integer.toString(AppointmentStatus.PCR_NEGATIVE)))
        {
            DatabaseHelper DB = new DatabaseHelper(getContext());
            DB.setHealth(sp.getString("phone", null), Integer.toString(HealthStatus.STATUS_UNKNOWN));
            editor.putString("health", Integer.toString(HealthStatus.STATUS_UNKNOWN));
            editor.commit();
        }

        // region Set OnClickListeners for the buttons
        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PandemicMapActivity.class);
                startActivity(intent);
            }
        });

        selfAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelfAssessmentActivity.class);
                startActivity(intent);
            }
        });

        travelAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TravelAdviceActivity.class);
                startActivity(intent);
            }
        });

        pandemicMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PandemicMapActivity.class);
                startActivity(intent);
            }
        });

        FAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FAQActivity.class);
                startActivity(intent);
            }
        });
        // endregion

        // Inflate the layout for this fragment
        return view;
    }

    // Set the statistics in the home fragment
    public void setStatistics(TextView updateDate, TextView totalConfirmed, TextView newConfirmed, TextView totalRecovered, TextView newRecovered, TextView totalActive, TextView newActive, TextView totalDeath, TextView newDeath) {
        SharedPreferences data = getActivity().getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        updateDate.setText(data.getString("date", "0"));
        totalConfirmed.setText(data.getString("totalConfirmed", "0"));
        newConfirmed.setText(data.getString("newConfirmed", "0"));
        totalRecovered.setText(data.getString("totalRecovered", "0"));
        newRecovered.setText(data.getString("newRecovered", "0"));
        totalActive.setText(data.getString("totalActive", "0"));
        newActive.setText(data.getString("newActive", "0"));
        totalDeath.setText(data.getString("totalDeath", "0"));
        newDeath.setText(data.getString("newDeath", "0"));
    }
}