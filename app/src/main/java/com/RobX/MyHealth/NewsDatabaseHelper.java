package com.RobX.MyHealth;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class NewsDatabaseHelper extends SQLiteOpenHelper {

    // region Basic Manipulations of the Hospital Database
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "News.db";
    public final static String DATABASE_PATH = "/data/data/com.RobX.MyHealth/databases/";
    public static final int DATABASE_VERSION = 1;
    public NewsDatabaseHelper(Context context) {
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
    public void onCreate(SQLiteDatabase db) {


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

    // region Press Statement
    public Vector<String> getPressStatementIDs() {
        openDatabase();
        Vector<String> IDs = new  Vector<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement", null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("ID"));
                IDs.add(id);
            }while(cursor.moveToNext());
        }
        return IDs;
    }

    public String getPSTitle(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement WHERE ID = ?", new String[] {id});
        String title = "";
        if (cursor.moveToFirst())
        {
            title = cursor.getString(cursor.getColumnIndex("Title"));
        }
        return title;
    }

    public String getPSDate(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement WHERE ID = ?", new String[] {id});
        String date = "";
        if (cursor.moveToFirst())
        {
            date = cursor.getString(cursor.getColumnIndex("Date"));
        }
        return date;
    }

    public String getPSAuthor(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement WHERE ID = ?", new String[] {id});
        String author = "";
        if (cursor.moveToFirst())
        {
            author = cursor.getString(cursor.getColumnIndex("Author"));
        }
        return author;
    }

    public String getPSText(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement WHERE ID = ?", new String[] {id});
        String text = "";
        if (cursor.moveToFirst())
        {
            text = cursor.getString(cursor.getColumnIndex("Text"));
        }
        return text;
    }

    public byte[] getPSImage(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement WHERE ID = ?", new String[] {id});
        byte[] byteArray = {};
        if (cursor.moveToFirst())
        {
            byteArray = cursor.getBlob(cursor.getColumnIndex("Image"));
        }
        return byteArray;
    }
    // endregion

    // region Rumour Clarifications
    public Vector<String> getRumourClarificationsIDs() {
        openDatabase();
        Vector<String> IDs = new  Vector<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM RumourClarifications", null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("ID"));
                IDs.add(id);
            }while(cursor.moveToNext());
        }
        return IDs;
    }

    public String getRCTitle(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM RumourClarifications WHERE ID = ?", new String[] {id});
        String title = "";
        if (cursor.moveToFirst())
        {
            title = cursor.getString(cursor.getColumnIndex("Title"));
        }
        return title;
    }

    public String getRCDate(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM RumourClarifications WHERE ID = ?", new String[] {id});
        String date = "";
        if (cursor.moveToFirst())
        {
            date = cursor.getString(cursor.getColumnIndex("Date"));
        }
        return date;
    }

    public String getRCAuthor(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM RumourClarifications WHERE ID = ?", new String[] {id});
        String author = "";
        if (cursor.moveToFirst())
        {
            author = cursor.getString(cursor.getColumnIndex("Author"));
        }
        return author;
    }

    public String getRCText(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM RumourClarifications WHERE ID = ?", new String[] {id});
        String text = "";
        if (cursor.moveToFirst())
        {
            text = cursor.getString(cursor.getColumnIndex("Article"));
        }
        return text;
    }

    public byte[] getRCImage(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM RumourClarifications WHERE ID = ?", new String[] {id});
        byte[] byteArray = {};
        if (cursor.moveToFirst())
        {
            byteArray = cursor.getBlob(cursor.getColumnIndex("Image"));
        }
        return byteArray;
    }
    // endregion

    // region Virus Knowledge
    public Vector<String> getVirusKnowledgeIDs() {
        openDatabase();
        Vector<String> IDs = new  Vector<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM VirusKnowledge", null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("ID"));
                IDs.add(id);
            }while(cursor.moveToNext());
        }
        return IDs;
    }

    public String getVKTitle(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM VirusKnowledge WHERE ID = ?", new String[] {id});
        String title = "";
        if (cursor.moveToFirst())
        {
            title = cursor.getString(cursor.getColumnIndex("Title"));
        }
        return title;
    }

    public String getVKDate(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM VirusKnowledge WHERE ID = ?", new String[] {id});
        String date = "";
        if (cursor.moveToFirst())
        {
            date = cursor.getString(cursor.getColumnIndex("Date"));
        }
        return date;
    }

    public String getVKAuthor(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM VirusKnowledge WHERE ID = ?", new String[] {id});
        String author = "";
        if (cursor.moveToFirst())
        {
            author = cursor.getString(cursor.getColumnIndex("Author"));
        }
        return author;
    }

    public String getVKText(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM VirusKnowledge WHERE ID = ?", new String[] {id});
        String text = "";
        if (cursor.moveToFirst())
        {
            text = cursor.getString(cursor.getColumnIndex("Article"));
        }
        return text;
    }

    public byte[] getVKImage(String id) {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM VirusKnowledge WHERE ID = ?", new String[] {id});
        byte[] byteArray = {};
        if (cursor.moveToFirst())
        {
            byteArray = cursor.getBlob(cursor.getColumnIndex("Image"));
        }
        return byteArray;
    }
    // endregion

    // Return the ID of the latest press statement
    public String getLatestPSID() {
        openDatabase();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM PressStatement", null);
        String id = "";
        if (cursor.moveToLast())
        {
            id = cursor.getString(cursor.getColumnIndex("ID"));
        }
        return id;
    }
}
