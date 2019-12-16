package com.example.myapplication.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.WeatherApi;
import com.example.myapplication.WeatherApiInterface;
import com.example.myapplication.model.OpenWeather;

public class WeatherFragment extends Fragment implements WeatherApiInterface {

    private WeatherViewModel weatherViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weatherViewModel =
                ViewModelProviders.of(this).get(WeatherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        final TextView textView = root.findViewById(R.id.text_weather);

        weatherViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        WeatherApi.getInstance().addListener(this);
    }

    @Override
    public void updateWeather(OpenWeather model) {
        try {
            ((TextView) getActivity().findViewById(R.id.text_hum)).setText("Humidity: " + Integer.toString(model.getMain().getHumidity()) + "%");
            ((TextView) getActivity().findViewById(R.id.text_temp)).setText("Temperature: " + Double.toString(model.getMain().getTemp() - 273.0) + " C");
            ((TextView) getActivity().findViewById(R.id.text_wind_speed)).setText("Wind speed: " + Double.toString(model.getWind().getSpeed()) + " m/s");
        }catch (NullPointerException e){

        }
    }

    @Override
    public void onPause() {
        WeatherApi.getInstance().removeListener(this);
        super.onPause();
    }
}