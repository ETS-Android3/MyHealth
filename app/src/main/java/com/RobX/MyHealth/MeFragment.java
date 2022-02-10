package com.RobX.MyHealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MeFragment extends Fragment {

    // region Declare Global Variables
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    // endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is to get whether user has successfully change language
        // If so, it will finish this activity and start another new Home Activity with new language
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);

        // Set the colour of status bar
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.health_green));

        // region Declare and Link Variables
        TextView name = view.findViewById(R.id.username);
        TextView id = view.findViewById(R.id.idnumber);
        TextView age = view.findViewById(R.id.age);
        LinearLayout id_btn = view.findViewById(R.id.change_id);
        LinearLayout age_btn = view.findViewById(R.id.change_age);
        LinearLayout language = view.findViewById(R.id.Language);
        LinearLayout about = view.findViewById(R.id.About);
        SwitchButton notification = view.findViewById(R.id.notification_btn);
        Button signout = view.findViewById(R.id.btnsignout);
        // endregion

        // region Instantiate classes and other tools as preparation
        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        name.setText(sp.getString("name",null));
        id.setText(sp.getString("ID", null));
        age.setText(sp.getString("age", null));
        notification.setChecked(Boolean.parseBoolean(sp.getString("notification", "false")));
        DatabaseHelper DB = new DatabaseHelper(getContext());
        // endregion

        // region Allow Users to Change Information by Building an AlertDialog
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter New Name: ");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(54, 0, 54, 0);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
                input.setLines(1);
                input.setMaxLines(1);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(name.getText());
                container.addView(input, lp);
                builder.setView(container);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_name = input.getText().toString();
                        DB.setName(sp.getString("phone", null), new_name);
                        editor.putString("name", new_name);
                        editor.commit();
                        name.setText(sp.getString("name", null));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter New ID Number: ");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(54, 0, 54, 0);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
                input.setLines(1);
                input.setMaxLines(1);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(id.getText());
                container.addView(input, lp);
                builder.setView(container);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_id = input.getText().toString();
                        DB.setID(sp.getString("phone", null), new_id);
                        editor.putString("ID", new_id);
                        editor.commit();
                        id.setText(sp.getString("ID", null));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        age_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter New Age: ");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(54, 0, 54, 0);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
                input.setLines(1);
                input.setMaxLines(1);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(age.getText());
                container.addView(input, lp);
                builder.setView(container);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_age = input.getText().toString();
                        DB.setAge(sp.getString("age", null), new_age);
                        editor.putString("age", new_age);
                        editor.commit();
                        age.setText(sp.getString("age", null));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        // endregion

        // region Save the Changed Value of the Switch Button
        notification.setmOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void OnCheckedChanged(boolean isChecked) {
                editor.putString("notification", Boolean.toString(notification.isChecked()));
                editor.commit();
            }
        });
        // endregion

        // region Set OnClickListeners for the buttons
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
                someActivityResultLauncher.launch(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // endregion

        // Inflate the layout for this fragment
        return view;
    }
}