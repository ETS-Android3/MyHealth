package com.RobX.MyHealth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

public class HospitalDatabaseHelper extends SQLiteOpenHelper {

    // region Basic Manipulations of the Hospital Database
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "Hospital.db";
    public final static String DATABASE_PATH = "/data/data/com.RobX.MyHealth/databases/";
    public static final int DATABASE_VERSION = 1;
    public HospitalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;

    }

    // Create a empty database on the system
    public void createDatabase() throws IOException
    {

        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
        }

        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }

    }

    // Check whether database already exists or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    // Copy the database from assets folder to the phone
    private void copyDataBase() throws IOException
    {

        InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    // Delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }
    // Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }
    // endregion

    // Return the locations of all medical centres
    public Vector<Location> getLocation() {
        openDatabase();
        Vector<Location> locations = new  Vector<Location>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Hospital", null);

        if(cursor != null && cursor.moveToFirst()){
            int i = 0;
            do {
                Location newPoint = new Location("Hospital" + i);
                i++;
                newPoint.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Latitude"))));
                newPoint.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Longitude"))));
                locations.add(newPoint);
            }while(cursor.moveToNext());
        }
        return locations;
    }

    // Return the name of the medical centre
    public String getName(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Hospital WHERE ID = ?", new String[] {Integer.toString(Integer.parseInt(id) + 1)});
        String name = "";
        if (cursor.moveToFirst())
        {
            name = cursor.getString(cursor.getColumnIndex("Name"));
        }
        return name;
    }

    // Return the address of the medical centre
    public String getAddress(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Hospital WHERE ID = ?", new String[] {Integer.toString(Integer.parseInt(id) + 1)});
        String address = "";
        if (cursor.moveToFirst())
        {
            address = cursor.getString(cursor.getColumnIndex("Address"));
        }
        return address;
    }

    // Return the phone of the medical centre
    public String getPhone(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Hospital WHERE ID = ?", new String[] {Integer.toString(Integer.parseInt(id) + 1)});
        String phone = "";
        if (cursor.moveToFirst())
        {
            phone = cursor.getString(cursor.getColumnIndex("Phone"));
        }
        return phone;
    }

    // Return the image of the medical centre
    public byte[] getImage(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Hospital WHERE ID = ?", new String[] {Integer.toString(Integer.parseInt(id) + 1)});
        byte[] byteArray = {};
        if (cursor.moveToFirst())
        {
            byteArray = cursor.getBlob(cursor.getColumnIndex("Image"));
        }
        return byteArray;
    }
}