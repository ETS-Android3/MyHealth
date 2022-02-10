package com.RobX.MyHealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

public class CodeFragment extends Fragment {

    // region Declare Global Variables
    private View view;
    // endregion

    @Override
    public void onResume() {
        super.onResume();

        // If users go to Self-Assessment and then back to Health Code,
        // This fragment should be refreshed to show latest information.
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_code, container, false);

        // Set the colour of status bar
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.white));

        // Display the Health Code
        updateUI();

        // Inflate the layout for this fragment
        return view;
    }

    // A tool function to create QR Code based on input string and colour
    public static Bitmap createQRCodeBitmap(String content, int width,int height,
                                            String character_set,String error_correction_level,
                                            String margin,int color_black, int color_white) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set);
            }
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            }
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black;
                    } else {
                        pixels[y * width + x] = color_white;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Display the Health Code
    private void updateUI() {

        // region Declare and Link Variables
        LinearLayout code = view.findViewById(R.id.codeImage);
        Button update = view.findViewById(R.id.updateCode_btn);
        Button assessment = view.findViewById(R.id.assessment_btn);
        TextView date = view.findViewById(R.id.updateDate);
        TextView status = view.findViewById(R.id.statusText);
        LinearLayout whole = view.findViewById(R.id.wholePage);
        TextView no_code_text = view.findViewById(R.id.no_code);
        TextView do_assessment_text = view.findViewById(R.id.do_assessment);
        // endregion

        // region Instantiate classes and other tools as preparation
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        date.setText(dateFormat.format(currentTime));

        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String name = sp.getString("name", null);
        String ID = sp.getString("ID", null);
        String health = sp.getString("health", null);
        // endregion

        // If user's health status is unknown,
        // show the alert and self-assessment button
        if (health.equals(Integer.toString(HealthStatus.STATUS_UNKNOWN)))
        {
            no_code_text.setVisibility(View.VISIBLE);
            do_assessment_text.setVisibility(View.VISIBLE);
            update.setVisibility(view.GONE);
            assessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), SelfAssessmentActivity.class);
                    startActivity(intent);
                }
            });
        }

        // If not,
        // show the code in correct colour and the update button
        else {
            no_code_text.setVisibility(View.GONE);
            do_assessment_text.setVisibility(View.GONE);
            update.setVisibility(view.VISIBLE);
            int color;
            switch (Integer.parseInt(health)) {
                case HealthStatus.STATUS_DANGEROUS:
                    color = R.color.red_code;
                    status.setText("DANGEROUS");
                    update.setBackgroundResource(R.drawable.button_rounded_corner_red);
                    break;
                case HealthStatus.STATUS_WARNING:
                    color = R.color.yellow_code;
                    status.setText("WARNING");
                    update.setBackgroundResource(R.drawable.button_rounded_corner_yellow);
                    break;
                case HealthStatus.STATUS_HEALTHY:
                    color = R.color.green_code;
                    status.setText("HEALTHY");
                    update.setBackgroundResource(R.drawable.button_rounded_corner_green);
                    break;
                default:
                    color = R.color.yellow_code;
                    break;
            }

            status.setTextColor(getResources().getColor(color, getContext().getTheme()));
            whole.setBackgroundColor(getResources().getColor(color, getContext().getTheme()));
            assessment.setVisibility(view.GONE);
            String data = "User : " + name + " ID : " + ID + " Time : " + currentTime + " Status : " + health;
            Bitmap bitmap = createQRCodeBitmap(data, 250, 250,"UTF-8","H", "0", ContextCompat.getColor(getContext(), color), Color.WHITE);
            Drawable drawable =new BitmapDrawable(getResources(), bitmap);
            code.setBackground(drawable);

            // region Set OnClickListener for the Update Button
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUI();
                }
            });
            // endregion
        }
    }
}