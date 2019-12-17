package com.example.myapplication;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.ui.weather.WeatherViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SensorManager sensorManager;
    //    private Sensor sensorTemp;
//    private Sensor sensorHum;
//    public static String textTemp;
//    public static String textHum;
    public static String defCity;
    SharedPreferences preferences;
    private WeatherViewModel weatherViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        preferences = getPreferences(MODE_PRIVATE);
        defCity = preferences.getString("defCity", "Moscow");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_weather, R.id.nav_weather_hist, R.id.about, R.id.nav_feedback)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sw_city1 || id == R.id.sw_city2 || id == R.id.sw_city3) {
            item.setChecked(!item.isChecked());
            defCity = item.getTitle().toString();
            preferences.edit().putString("defCity", defCity).commit();
            defCity = preferences.getString("defCity", "Moscow");
        }
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        final TextView textView = findViewById(R.id.text_weather);
        weatherViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return super.onOptionsItemSelected(item);
    }


//        sensorTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//        sensorHum = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
//        sensorManager.registerListener(listenerHum, sensorHum, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(listenerTemp, sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);


//    private void showTempSensors(SensorEvent event) {
//        textTemp = "Temperature value = " + event.values[0] + "°C";
//
//    }

//    SensorEventListener listenerTemp = new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent sensorEvent) {
//            showTempSensors(sensorEvent);
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int i) {
//
//        }
//    };
//
//    private void showHumSensors(SensorEvent event) {
//        textHum = "Humidity value = " + event.values[0] + "%";
//
//    }
//
//    SensorEventListener listenerHum = new SensorEventListener() {
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        }
//
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            showHumSensors(event);
//        }
//    };


}
