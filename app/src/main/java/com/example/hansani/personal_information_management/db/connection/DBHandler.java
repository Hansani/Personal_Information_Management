package com.example.hansani.personal_information_management.db.connection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hansani on 8/23/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "dayTimeSchedule.db";
    public static final String LOCATION = "/data/data/com.example.hansani.personal_information_management/databases/";
    public static final int DB_VERSION = 1;
    public Context contextdb;
    private SQLiteDatabase dayTimeDB;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.contextdb = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

    //open the database
    public void openDataBase() {
        String dbpath = contextdb.getDatabasePath(DB_NAME).getPath();
        if (dayTimeDB != null && dayTimeDB.isOpen()) {
            return;
        }
        dayTimeDB = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    //close the database
    public void closeDataBase() {
        if (dayTimeDB != null) {
            dayTimeDB.close();
        }
    }

    public String getVenue(String day, String Time_From, String Time_To) {
        String venue;
        openDataBase();
        Cursor cursor = dayTimeDB.rawQuery("SELECT Venue FROM dayTimeSchedule WHERE Day = ? AND Time" +
                " = ? AND Time_to = ?", new String[]{day, Time_From, Time_To});
        Log.d("select query", cursor.toString());
        if (cursor.getCount() <= 0) {
            cursor.close();
        }
        if (cursor.moveToFirst()) {
            venue = cursor.getString(cursor.getColumnIndex("Venue"));
            return venue;
        }
        cursor.close();
        closeDataBase();
        return null;
    }

    public boolean updateVenue(String day, String Time_From, String Time_To, String Venue) {
        openDataBase();
        Cursor cursor = dayTimeDB.rawQuery("Update dayTimeSchedule SET Venue=? WHERE Day=? AND Time = ? AND Time_To=?", new String[]{Venue, day, Time_From, Time_To});
        if (cursor.moveToFirst()) {
            cursor.close();
            closeDataBase();
            return true;
        } else {
            cursor.close();
            closeDataBase();
            return false;
        }
    }

}


