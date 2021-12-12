package com.example.myapplication.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.callbacks.CallBackMovePlayer;


public class AccFragment extends Fragment {
    private AppCompatActivity activity;
    private CallBackMovePlayer callBackMovePlayer;
    private SensorManager sensorManager;
    private Sensor accSensor;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackMovePlayer(CallBackMovePlayer callBackMovePlayer) {
        this.callBackMovePlayer = callBackMovePlayer;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acc, container, false);
        initSensor();
        return view;
    }

    private void initSensor() {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(x<-3)
                callBackMovePlayer.movePlayer(true);
            else if(x>3)
                callBackMovePlayer.movePlayer(false);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accSensorEventListener);
    }

}