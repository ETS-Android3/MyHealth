package com.RobX.MyHealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // region Basic Manipulations of the Database
    public static final String DBNAME = "MyHealth.db";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Phone TEXT NOT NULL, " +
                        "Password TEXT NOT NULL, " +
                        "Name TEXT NOT NULL, " +
                        "Age INTEGER NOT NULL, " +
                        "IdentityNumber TEXT NOT NULL UNIQUE," +
                        "HealthStatus INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER");
    }
    // endregion

    // Insert new user data to the database
    public Boolean insertData(String phone, String password, String name, String age, String idNumber) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Phone", phone);
        contentValues.put("Password", password);
        contentValues.put("Name", name);
        contentValues.put("Age", age);
        contentValues.put("IdentityNumber", idNumber);
        contentValues.put("HealthStatus", Integer.toString(HealthStatus.STATUS_UNKNOWN));

        long result = MyDB.insert("User", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Check whether user exists
    public Boolean checkPhone(String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM User WHERE Phone = ?", new String[] {phone});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Check whether password is correct
    public Boolean checkPassword(String phone, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM User WHERE Phone = ? and Password = ?", new String[]{phone, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Return the name of the user
    public String getUserName(String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM User WHERE Phone = ?", new String[] {phone});
        String name = "";
        if (cursor.moveToFirst())
        {
            name = cursor.getString(cursor.getColumnIndex("Name"));
        }
        return name;
    }

    // Return the age of the user
    public String getAge(String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM User WHERE Phone = ?", new String[] {phone});
        String age = "";
        if (cursor.moveToFirst())
        {
            age = cursor.getString(cursor.getColumnIndex("Age"));
        }
        return age;
    }

    // Return the ID number of the user
    public String getID(String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM User WHERE Phone = ?", new String[] {phone});
        String id = "";
        if (cursor.moveToFirst())
        {
            id = cursor.getString(cursor.getColumnIndex("IdentityNumber"));
        }
        return id;
    }

    // Return the health status of the user
    public String getHealth(String phone) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM User WHERE Phone = ?", new String[] {phone});
        String health = "";
        if (cursor.moveToFirst())
        {
            health = cursor.getString(cursor.getColumnIndex("HealthStatus"));
        }
        return health;
    }

    // Set the health status of the user
    public Boolean setHealth(String phone, String health) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("HealthStatus", health);
        int result =  MyDB.update("User", contentValues, "Phone = ?", new String[] {phone});

        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    // Set the ID number of the user
    public Boolean setID(String phone, String ID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdentityNumber", ID);
        int result =  MyDB.update("User", contentValues, "Phone = ?", new String[] {phone});

        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    // Set the name of the user
    public Boolean setName(String phone, String name) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        int result =  MyDB.update("User", contentValues, "Phone = ?", new String[] {phone});

        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    // Set the age of the user
    public Boolean setAge(String phone, String age) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Age", age);
        int result =  MyDB.update("User", contentValues, "Phone = ?", new String[] {phone});

        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }
}
