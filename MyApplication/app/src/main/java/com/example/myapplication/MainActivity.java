package com.example.myapplication;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.db.DataSource;
import com.example.myapplication.ui.weather.WeatherFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private AppBarConfiguration mAppBarConfiguration;
    //    private SensorManager sensorManager;
//    private Sensor sensorTemp;
//    private Sensor sensorHum;
//    public static String textTemp;
//    public static String textHum;
    public static String defCity;
    SharedPreferences preferences;
    private DataSource dataSource;
    private Button button;
    private EditText editText;
    private NoteAdapter adapter;
    private static final int PERMISION_REQUEST_CODE = 100;
    private LocationManager locationManager;
    private String provider;
    public static double lat;
    public static double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new NoteAdapter(dataSource.getReader());

//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        preferences = getPreferences(MODE_PRIVATE);
        defCity = preferences.getString("defCity", "Moscow");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocation();
        }else{
            requestLocationPermission();
        }

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
    private void requestLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        provider = locationManager.getBestProvider(criteria, true);
        if (provider!=null){
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

    }

    private void requestLocationPermission() {

        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISION_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int index = -1;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i] == Manifest.permission.SEND_SMS) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return;
        if (requestCode == PERMISION_REQUEST_CODE && grantResults[index] == PackageManager.PERMISSION_GRANTED)
            requestLocation();
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
    protected void onResume() {
        super.onResume();
        button = findViewById(R.id.btn);
        editText = findViewById(R.id.enter_city);

        button.setOnClickListener(view -> {
            if (!editText.getText().toString().equals("")) {
                defCity = editText.getText().toString();
            }
            preferences.edit().putString("defCity", defCity).commit();
            defCity = preferences.getString("defCity", "Moscow");
            dataSource.addOrEdit(defCity, WeatherFragment.temp, WeatherFragment.hum, WeatherFragment.wind);
            refreshData();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sw_city1 || id == R.id.sw_city2 || id == R.id.sw_city3) {
            item.setChecked(!item.isChecked());
            defCity = item.getTitle().toString();
            preferences.edit().putString("defCity", defCity).commit();
            defCity = preferences.getString("defCity", "Moscow");
            dataSource.addOrEdit(defCity, WeatherFragment.temp, WeatherFragment.hum, WeatherFragment.wind);
            refreshData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        dataSource.getReader().refresh();
        adapter.notifyDataSetChanged();
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
