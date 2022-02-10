package com.RobX.MyHealth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class StatisticsHelper {

    private Context context;

    public StatisticsHelper(Context mContext)
    {
        context = mContext;
    }

    // Fetch the latest data
    public void fetchStatistics() {
        SharedPreferences data = context.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = data.edit();

        String url = "https://disease.sh/v3/covid-19/countries/malaysia?strict=true";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the JSON object and
                // handle it inside try and catch
                try {
                    JSONObject jsonObject
                            = new JSONObject(
                            response);

                    // If the data has been updated,
                    // use the new data.
                    if (jsonObject.getInt("todayCases") != 0 || jsonObject.getInt("todayRecovered") != 0 || jsonObject.getInt("todayDeaths") != 0)
                    {
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Calendar cal = Calendar.getInstance();
                        dataEditor.putString("date", date.format(cal.getTimeInMillis()));
                        dataEditor.putString("totalConfirmed", jsonObject.getString("cases"));
                        dataEditor.putString("newConfirmed", context.getString(R.string.plus_number, jsonObject.getString("todayCases")));
                        dataEditor.putString("totalRecovered", jsonObject.getString("recovered"));
                        dataEditor.putString("newRecovered", context.getString(R.string.plus_number, jsonObject.getString("todayRecovered")));
                        dataEditor.putString("totalActive", jsonObject.getString("active"));
                        dataEditor.putString("totalDeath", jsonObject.getString("deaths"));
                        dataEditor.putString("newDeath", context.getString(R.string.plus_number, jsonObject.getString("todayDeaths")));

                        Integer dif = Integer.parseInt(jsonObject.getString("todayCases"))
                                - Integer.parseInt(jsonObject.getString("todayRecovered"))
                                - Integer.parseInt(jsonObject.getString("todayDeaths"));

                        if (dif >= 0)
                        {
                            dataEditor.putString("newActive", String.format(Locale.getDefault(), "+ %d", Math.abs(dif)));
                        }
                        else
                        {
                            dataEditor.putString("newActive", String.format(Locale.getDefault(), "- %d", Math.abs(dif)));
                        }
                        dataEditor.commit();
                    }
                    // If not,
                    // use yesterday's data
                    else {
                        fetchYesterdayStatistics();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((MainActivity) context).gotoNext();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "Cannot Fetch Data!", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).gotoNext();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    // Fetch yesterday's data instead
    public void fetchYesterdayStatistics() {
        SharedPreferences data = context.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = data.edit();


        String url = "https://disease.sh/v3/covid-19/countries/malaysia?yesterday=true&strict=true";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the JSON object and
                // handle it inside try and catch
                try {
                    JSONObject jsonObject
                            = new JSONObject(
                            response);
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    dataEditor.putString("date", date.format(cal.getTimeInMillis()));
                    dataEditor.putString("totalConfirmed", jsonObject.getString("cases"));
                    dataEditor.putString("newConfirmed", context.getString(R.string.plus_number, jsonObject.getString("todayCases")));
                    dataEditor.putString("totalRecovered", jsonObject.getString("recovered"));
                    dataEditor.putString("newRecovered", context.getString(R.string.plus_number, jsonObject.getString("todayRecovered")));
                    dataEditor.putString("totalActive", jsonObject.getString("active"));
                    dataEditor.putString("totalDeath", jsonObject.getString("deaths"));
                    dataEditor.putString("newDeath", context.getString(R.string.plus_number, jsonObject.getString("todayDeaths")));

                    Integer dif = Integer.parseInt(jsonObject.getString("todayCases"))
                            - Integer.parseInt(jsonObject.getString("todayRecovered"))
                            - Integer.parseInt(jsonObject.getString("todayDeaths"));

                    if (dif >= 0)
                    {
                        dataEditor.putString("newActive", String.format(Locale.getDefault(), "+ %d", Math.abs(dif)));
                    }
                    else
                    {
                        dataEditor.putString("newActive", String.format(Locale.getDefault(), "- %d", Math.abs(dif)));
                    }
                    dataEditor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((MainActivity) context).gotoNext();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "Cannot Fetch Data!", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).gotoNext();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}
