package com.example.myapplication.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Note;

import java.io.Closeable;
import java.io.IOException;

public class DataReader implements Closeable {
    private final SQLiteDatabase database;
    public Cursor cursor;
    private String[] allColumn = {DataHelper.TABLE_ID,
            DataHelper.TABLE_CITY,
            DataHelper.TABLE_TEMPERATURE,
            DataHelper.TABLE_HUMIDITY,
            DataHelper.TABLE_WIND};

    public DataReader(SQLiteDatabase database) {
        this.database = database;
    }

    public void open() {
        query();
        cursor.moveToFirst();
    }

    public void refresh() {
        int pos = cursor.getPosition();
        query();
        cursor.moveToPosition(pos);
    }

    private void query() {
        cursor = database.query(DataHelper.TABLE_NAME,
                allColumn,
                null,
                null,
                null,
                null,
                null);
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }

    public Note cursorToNote() {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setCity(cursor.getString(1));
        note.setTemperature(cursor.getInt(2));
        note.setHumidity(cursor.getInt(3));
        note.setWind(cursor.getDouble(4));
        return note;
    }

    public Note getPosition(int position){
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    public int getCount(){
        return cursor.getCount();
    }
}
