package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Note;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

public class DataSource implements Closeable {

    private final DataHelper dbHelper;
    private SQLiteDatabase database;
    private DataReader reader;

    public DataSource(Context context) {
        dbHelper = new DataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        reader = new DataReader(database);
        reader.open();
    }

    @Override
    public void close() throws IOException {
        database.close();
        reader.close();
    }

    public void addOrEdit(String city, Integer temp, Integer hum, Double wind) {
        if (reader.getCount() == 0) {
            add(city, temp, hum, wind);
        } else {
            for (int i = 0; i < reader.getCount(); i++) {
                if (reader.getPosition(i).getCity().toLowerCase().equals(city.toLowerCase())) {
                    edit(reader.getPosition(i), city, temp, hum, wind);
                    return;
                }
            }
            add(city, temp, hum, wind);
        }

    }

    public Note add(String city, Integer temp, Integer hum, Double wind) {
        Note note = new Note();
        ContentValues values = new ContentValues();
        values.put(DataHelper.TABLE_CITY, city);
        values.put(DataHelper.TABLE_TEMPERATURE, temp);
        values.put(DataHelper.TABLE_HUMIDITY, hum);
        values.put(DataHelper.TABLE_WIND, wind);

        long id = database.insert(DataHelper.TABLE_NAME,
                null,
                values);

        note.setId(id);
        note.setCity(city);
        note.setTemperature(temp);
        note.setHumidity(hum);
        note.setWind(wind);
        return note;
    }

    public void edit(Note note, String city, Integer temp, Integer hum, Double wind) {
        ContentValues values = new ContentValues();
        values.put(DataHelper.TABLE_CITY, city);
        values.put(DataHelper.TABLE_TEMPERATURE, temp);
        values.put(DataHelper.TABLE_HUMIDITY, hum);
        values.put(DataHelper.TABLE_WIND, wind);
        values.put(DataHelper.TABLE_ID, note.getId());

        database.update(DataHelper.TABLE_NAME,
                values,
                DataHelper.TABLE_ID + "=" + note.getId(),
                null);
    }

    public void delete(Note note) {
        database.delete(DataHelper.TABLE_NAME,
                DataHelper.TABLE_ID + "=" + note.getId(),
                null);
    }

    public void deleteAll() {
        database.delete(DataHelper.TABLE_NAME,
                null,
                null);
    }

    public DataReader getReader() {
        return reader;
    }
}
