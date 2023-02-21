package com.example.patientqueuemanagement;

import android.app.Application;

import com.example.patientqueuemanagement.Utils.EnglishHebrewTranslator;
import com.example.patientqueuemanagement.Utils.SharedPreferences;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences.init(this);
        EnglishHebrewTranslator.connectToModel();
    }
}