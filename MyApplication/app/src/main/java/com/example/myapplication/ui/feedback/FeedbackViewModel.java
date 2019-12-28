package com.example.myapplication.ui.feedback;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedbackViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FeedbackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("There you can write feedback");
    }

    public LiveData<String> getText() {
        return mText;
    }
}