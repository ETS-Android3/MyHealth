package com.RobX.MyHealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Vector;

public class AppointmentDatabaseHelper extends SQLiteOpenHelper {

    // region Basic Manipulations of the Appointment Database
    public static final String DBNAME = "Appointment.db";
    public AppointmentDatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Appointment (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PatientNumber TEXT NOT NULL, " +
                "HospitalID TEXT NOT NULL, " +
                "Date INTEGER NOT NULL, " +
                "Type INTEGER NOT NULL, " +
                "Result TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Appointment");
    }
    // endregion

    // Insert new appointment data to the database
    public Boolean insertData(String pNo, String hID, String date, String type, String result) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PatientNumber", pNo);
        contentValues.put("HospitalID", hID);
        contentValues.put("Date", date);
        contentValues.put("Type", type);
        contentValues.put("Result", result);

        long r = MyDB.insert("Appointment", null, contentValues);
        if (r == -1)
            return false;
        else
            return true;
    }

    // Return the ID of user's next appointment
    public String getNextAppointmentID(long currentDate, String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE PatientNumber = ?", new String[] {phone});
        String minAppointmentID = "";

        if(cursor != null && cursor.moveToFirst()){
            Long minDate = Long.MAX_VALUE;
            do {
                Long date;
                date = cursor.getLong(cursor.getColumnIndex("Date"));
                if (date > currentDate && date < minDate)
                {
                    minDate = date;
                    minAppointmentID = cursor.getString(cursor.getColumnIndex("ID"));
                }
            }while(cursor.moveToNext());
        }
        return minAppointmentID;
    }

    // Return the result of user's last PCR Ttest
    public String getLastPCRTestResult(long currentDate, String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE PatientNumber = ? AND Type = " + AppointmentStatus.PCR_TEST, new String[] {phone});
        String maxAppointmentID = "";

        if(cursor != null && cursor.moveToFirst()){
            Long maxDate = Long.MIN_VALUE;
            do {
                Long date;
                date = cursor.getLong(cursor.getColumnIndex("Date"));
                if (date < currentDate && date > maxDate)
                {
                    maxDate = date;
                    maxAppointmentID = cursor.getString(cursor.getColumnIndex("ID"));
                }
            }while(cursor.moveToNext());
        }

        if (maxAppointmentID.equals(""))
            return "";
        else
            return getResult(maxAppointmentID);
    }

    // Return all the IDs of user's future appointments
    public Vector<String> getChronologicalAppointmentIDs(long currentDate, String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE PatientNumber = ?", new String[] {phone});
        String appointmentID = "";
        Vector<String> appointmentIDs = new Vector<>();

        if(cursor != null && cursor.moveToFirst()){
            do {
                Long date;
                date = cursor.getLong(cursor.getColumnIndex("Date"));
                if (date > currentDate)
                {
                    appointmentID = cursor.getString(cursor.getColumnIndex("ID"));
                    appointmentIDs.add(appointmentID);
                }
            }while(cursor.moveToNext());
        }

        int length = appointmentIDs.size();

        for (int i = 1; i < length; i++) {
            for (int j = 0; j < length - i; j++) {
                if (getLongDate(appointmentIDs.get(j)) > getLongDate(appointmentIDs.get(j + 1))) {
                    String temp;
                    temp = appointmentIDs.get(j);
                    appointmentIDs.set(j, appointmentIDs.get(j + 1));
                    appointmentIDs.set(j + 1, temp);
                }
            }
        }

        return appointmentIDs;
    }

    // Return the hospital ID of the appointment
    public String getHospitalID(String ID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE ID = ?", new String[] {ID});
        String hospitalID = "";
        if (cursor.moveToFirst())
        {
            hospitalID = cursor.getString(cursor.getColumnIndex("HospitalID"));
        }
        return hospitalID;
    }

    // Return the date of the appointment as a string
    public String getDate(String ID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE ID = ?", new String[] {ID});
        String date = "";
        if (cursor.moveToFirst())
        {
            date = cursor.getString(cursor.getColumnIndex("Date"));
        }
        return date;
    }

    // Return the date of the appointment in milliseconds
    public Long getLongDate(String ID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE ID = ?", new String[] {ID});
        Long date = -1L;
        if (cursor.moveToFirst())
        {
            date = cursor.getLong(cursor.getColumnIndex("Date"));
        }
        return date;
    }

    // Return the result of the appointment
    public String getResult(String ID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE ID = ?", new String[] {ID});
        String result = "";
        if (cursor.moveToFirst())
        {
            result = cursor.getString(cursor.getColumnIndex("Result"));
        }
        return result;
    }

    // Return the type of the appointment (PCR Test or Vaccine)
    public String getType(String ID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Appointment WHERE ID = ?", new String[] {ID});
        String type = "";
        if (cursor.moveToFirst())
        {
            type = cursor.getString(cursor.getColumnIndex("Type"));
        }
        return type;
    }
}
