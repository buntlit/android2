package com.example.myapplication;

import android.os.Handler;

import com.example.myapplication.model.OpenWeather;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class WeatherApi {

    private Set<WeatherApiInterface> listeners;
    private static WeatherApi instance = null;
    Handler handler = new Handler();
    private Timer timer = null;
    private Retrofit retrofit;
    private OpenWea weaApi;

    public static WeatherApi getInstance() {
        instance = instance == null ? new WeatherApi() : instance;
        return instance;
    }

    public void addListener(WeatherApiInterface listener) {
        if (!listeners.contains(listener)) {
            listeners.add((listener));
        }
    }

    public void removeListener(WeatherApiInterface listener) {
        if (!listeners.contains(listener)) {
            listeners.remove((listener));
        }
    }

    public WeatherApi() {
        listeners = new HashSet<>();
        retrofit = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/").addConverterFactory(GsonConverterFactory.create()).build();
        weaApi = retrofit.create(OpenWea.class);
        startData();
    }

    public interface OpenWea {
        @GET("data/2.5/weather")
//        Call<OpenWeather> getWeather(@Query("q") String city, @Query("appid") String keyApi);
        Call<OpenWeather> getWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String keyApi);
    }

    private OpenWeather getWeather(String cityName) throws Exception {
        Call<OpenWeather> call = weaApi.getWeather(Double.toString(MainActivity.lat), Double.toString(MainActivity.lon), "2f0e5b22131acf399237f29d84fdcfeb");

        Response<OpenWeather> response = call.execute();

        if (response.isSuccessful())
            return response.body();
        else
            throw new Exception(response.errorBody().string(), null);
    }

    private void startData() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    OpenWeather model = getWeather(MainActivity.defCity);
                    if (model == null) return;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (WeatherApiInterface listener : listeners) {
                                listener.updateWeather(model);
                            }
                        }
                    });
                    MainActivity.defCity = model.getName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000, 5000);
    }
}
