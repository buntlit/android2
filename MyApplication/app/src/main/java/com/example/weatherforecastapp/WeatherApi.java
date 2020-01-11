package com.example.weatherforecastapp;

import android.os.Handler;

import com.example.weatherforecastapp.model.OpenWeather;
import com.example.weatherforecastapp.model.OpenWeatherForecast;
import com.example.weatherforecastapp.ui.weather.WeatherFragment;

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
    private OpenWeaCoord weaApiCoord;
    private OpenWeaCity weaApiCity;
    private OpenWeaForId weaForApiId;

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
        weaApiCoord = retrofit.create(OpenWeaCoord.class);
        weaApiCity = retrofit.create(OpenWeaCity.class);
        weaForApiId = retrofit.create(OpenWeaForId.class);
        startDataCoord();
        startDataCity();
        startForecastId();
    }

    public interface OpenWeaCoord {
        @GET("data/2.5/weather")
        Call<OpenWeather> getWeatherCoord(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String keyApi);
    }

    public interface OpenWeaCity {
        @GET("data/2.5/weather")
        Call<OpenWeather> getWeatherCity(@Query("q") String city, @Query("appid") String keyApi);
    }

    public interface OpenWeaForId {
        @GET("data/2.5/forecast")
        Call<OpenWeatherForecast> getWeatherForecastId(@Query("id") String cityId, @Query("appid") String keyApi);
    }


    private OpenWeather getWeatherCoord() throws Exception {
        Call<OpenWeather> call = weaApiCoord.getWeatherCoord(Double.toString(MainActivity.lat), Double.toString(MainActivity.lon), "2f0e5b22131acf399237f29d84fdcfeb");

        Response<OpenWeather> response = call.execute();

        if (response.isSuccessful())
            return response.body();
        else
            throw new Exception(response.errorBody().string(), null);
    }

    private OpenWeather getWeatherCity(String cityName) throws Exception {
        Call<OpenWeather> call = weaApiCity.getWeatherCity(cityName, "2f0e5b22131acf399237f29d84fdcfeb");

        Response<OpenWeather> response = call.execute();

        if (response.isSuccessful())
            return response.body();
        else
            throw new Exception(response.errorBody().string(), null);
    }

    private OpenWeatherForecast getWeatherForecastId() throws Exception {
        Call<OpenWeatherForecast> call = weaForApiId.getWeatherForecastId(Integer.toString(WeatherFragment.cityId),  "2f0e5b22131acf399237f29d84fdcfeb");

        Response<OpenWeatherForecast> response = call.execute();

        if (response.isSuccessful())
            return response.body();
        else
            throw new Exception(response.errorBody().string(), null);
    }



    private void startDataCoord() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    OpenWeather model = getWeatherCoord();
                    if (model == null) return;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (WeatherApiInterface listener : listeners) {
                                listener.updateWeatherCoord(model);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    private void startDataCity() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    OpenWeather model = getWeatherCity(MainActivity.defCity);
                    if (model == null) return;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (WeatherApiInterface listener : listeners) {
                                listener.updateWeatherCity(model);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    private void startForecastId() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    OpenWeatherForecast model = getWeatherForecastId();
                    if (model == null) return;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (WeatherApiInterface listener : listeners) {
                                listener.showWeatherForecastId(model);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

}
