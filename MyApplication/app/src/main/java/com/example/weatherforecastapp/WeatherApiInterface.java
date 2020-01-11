package com.example.weatherforecastapp;

import com.example.weatherforecastapp.model.OpenWeather;
import com.example.weatherforecastapp.model.OpenWeatherForecast;

public interface WeatherApiInterface {
    void updateWeatherCoord(OpenWeather model);
    void updateWeatherCity(OpenWeather model);
    void showWeatherForecastId(OpenWeatherForecast model);
}
