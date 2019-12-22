package com.example.myapplication.ui.weather_hist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherHistViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WeatherHistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("There will be weather history");
    }

    public LiveData<String> getText() {
        return mText;
    }
}