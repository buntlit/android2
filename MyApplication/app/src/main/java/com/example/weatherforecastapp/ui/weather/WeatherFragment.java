package com.example.weatherforecastapp.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weatherforecastapp.MainActivity;
import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.WeatherApi;
import com.example.weatherforecastapp.WeatherApiInterface;
import com.example.weatherforecastapp.model.OpenWeather;
import com.example.weatherforecastapp.model.OpenWeatherForecast;
import com.squareup.picasso.Picasso;

public class WeatherFragment extends Fragment implements WeatherApiInterface {

    public static String cityCoord;
    public static Integer tempCoord;
    public static Integer humCoord;
    public static Double windCoord;
    public static String weatherCoord;
    public static Integer idCoord;

    public static Integer tempCity;
    public static Integer humCity;
    public static Double windCity;
    public static String weatherCity;
    public static Integer idCity;

    EditText editText;

    public static int cityId = 1851632;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        WeatherApi.getInstance().addListener(this);
        getActivity().findViewById(R.id.layout_coord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idCoord != null) {
                    cityId = idCoord;
                    visibility(View.VISIBLE);
                }
            }
        });
        getActivity().findViewById(R.id.layout_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idCity != null) {
                    cityId = idCity;
                    visibility(View.VISIBLE);
                }
            }
        });
        getActivity().findViewById(R.id.layout_forecast_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibility(View.INVISIBLE);
            }
        });

        getActivity().findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = getActivity().findViewById(R.id.enter_city);
                if (!editText.getText().toString().equals("")) {
                    MainActivity.clickOrSelect(editText.getText().toString());
                }
            }
        });

    }

    public void visibility(int visible) {
        try {
            Thread.sleep(1000);
            getActivity().findViewById(R.id.layout_forecast_id).setVisibility(visible);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateWeatherCoord(OpenWeather model) {
        try {
            cityCoord = model.getName();
            humCoord = model.getMain().getHumidity();
            tempCoord = (int) (model.getMain().getTemp() - 273.0);
            windCoord = model.getWind().getSpeed();
            weatherCoord = model.getWeather().get(0).getMain();
            idCoord = model.getId();


            ((TextView) getActivity().findViewById(R.id.text_city_coord)).setText(cityCoord);
            ((TextView) getActivity().findViewById(R.id.text_hum_coord)).setText(getString(R.string.hum) + " " + Integer.toString(humCoord) + "%");
            ((TextView) getActivity().findViewById(R.id.text_temp_coord)).setText(getString(R.string.temp) + " " + Integer.toString(tempCoord) + " ℃");
            ((TextView) getActivity().findViewById(R.id.text_wind_speed_coord)).setText(getString(R.string.wind) + " " + Double.toString(windCoord) + " " + getString(R.string.wind_speed));
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_coord));


        } catch (NullPointerException e) {

        }
    }

    @Override
    public void updateWeatherCity(OpenWeather model) {
        try {
            humCity = model.getMain().getHumidity();
            tempCity = (int) (model.getMain().getTemp() - 273.0);
            windCity = model.getWind().getSpeed();
            weatherCity = model.getWeather().get(0).getMain();
            idCity = model.getId();

            ((TextView) getActivity().findViewById(R.id.text_city_default)).setText(MainActivity.defCity);
            ((TextView) getActivity().findViewById(R.id.text_hum_default)).setText(getString(R.string.hum) + " " + Integer.toString(humCity) + "%");
            ((TextView) getActivity().findViewById(R.id.text_temp_default)).setText(getString(R.string.temp) + " " + Integer.toString(tempCity) + "℃");
            ((TextView) getActivity().findViewById(R.id.text_wind_speed_default)).setText(getString(R.string.wind) + " " + Double.toString(windCity) + " " + getString(R.string.wind_speed));
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_default));
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void showWeatherForecastId(OpenWeatherForecast model) {
        try {
            ((TextView) getActivity().findViewById(R.id.text_city_id)).setText("" + model.getCity().getName());
            ((TextView) getActivity().findViewById(R.id.text_temp_id_first)).setText(getString(R.string.weather_after3) + " " + Integer.toString((int) (model.getList().get(0).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(0).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_first));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_second)).setText(getString(R.string.weather_after6) + " " + Integer.toString((int) (model.getList().get(1).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(1).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_second));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_third)).setText(getString(R.string.weather_after9) + " " + Integer.toString((int) (model.getList().get(2).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(2).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_third));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_four)).setText(getString(R.string.weather_after12) + " " + Integer.toString((int) (model.getList().get(3).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(3).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_four));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_five)).setText(getString(R.string.weather_after15) + " " + Integer.toString((int) (model.getList().get(4).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(4).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_five));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_six)).setText(getString(R.string.weather_after18) + " " + Integer.toString((int) (model.getList().get(5).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(5).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_six));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_seven)).setText(getString(R.string.weather_after21) + " " + Integer.toString((int) (model.getList().get(6).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(6).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_seven));

            ((TextView) getActivity().findViewById(R.id.text_temp_id_eight)).setText(getString(R.string.weather_after24) + " " + Integer.toString((int) (model.getList().get(7).getMain().getTemp() - 273)) + "℃");
            Picasso.with(getActivity())
                    .load(getString(R.string.url) + model.getList().get(7).getWeather().get(0).getIcon() + getString(R.string.size_and_tipe))
                    .into((ImageView) getActivity().findViewById(R.id.image_id_eight));
        } catch (NullPointerException e) {

        }

    }


    @Override
    public void onPause() {
        WeatherApi.getInstance().removeListener(this);
        super.onPause();
    }
}