package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.etc.MSPV3;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MSPV3.initHelper(this);
    }
}
