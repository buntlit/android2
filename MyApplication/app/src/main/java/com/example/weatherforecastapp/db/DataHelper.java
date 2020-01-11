package com.example.weatherforecastapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "weather.db";
    private static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "weather";
    public static final String TABLE_ID = "_id";
    public static final String TABLE_CITY = "city";
    public static final String TABLE_TEMPERATURE = "temperature";
    public static final String TABLE_HUMIDITY = "humidity";
    public static final String TABLE_WIND = "wind";
    public static final String TABLE_WEATHER = "weather";

    public DataHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_CITY + " TEXT, " +
                TABLE_TEMPERATURE + " INTEGER, " +
                TABLE_HUMIDITY + " INTEGER, " +
                TABLE_WIND + " REAL, " +
                TABLE_WEATHER + " TEXT);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            String upgradeSTR = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + TABLE_CITY + " TEXT DEFAULT city";
            sqLiteDatabase.execSQL(upgradeSTR);
        }
    }
}
