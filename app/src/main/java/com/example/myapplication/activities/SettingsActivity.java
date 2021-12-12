package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.myapplication.etc.MSPV3;
import com.example.myapplication.R;

public class SettingsActivity extends AppCompatActivity {

    private RadioButton[] radioButtons;
    private RadioGroup rg;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.bundle = getIntent().getBundleExtra(StartActivity.BUNDLE_KEY);
        finedViews();
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=(RadioButton)findViewById(checkedId);
            if(rb.getText().toString().contains("Sensor")){
                checkTypeOfGame("acc");
            } else if(rb.getText().toString().contains("slow"))
                checkTypeOfGame("slow");
            else
                checkTypeOfGame("fast");
        });
    }

    private void checkTypeOfGame(String mode) {
        MSPV3 sp = MSPV3.getMe();
        if(mode.equals("acc")){
            sp.putString(MainActivity.GAME_TYPE_KEY, MainActivity.GAME_TYPE_ACC);
            sp.putInt(MainActivity.GAME_SPEED_KEY, MainActivity.GAME_SPEED_SLOW);
        }
        else if(mode.equals("slow")) {
            sp.putString(MainActivity.GAME_TYPE_KEY, MainActivity.GAME_TYPE_BUTTONS);
            sp.putInt(MainActivity.GAME_SPEED_KEY, MainActivity.GAME_SPEED_SLOW);
        }
        else if(mode.equals("fast")){
            sp.putString(MainActivity.GAME_TYPE_KEY, MainActivity.GAME_TYPE_BUTTONS);
            sp.putInt(MainActivity.GAME_SPEED_KEY, MainActivity.GAME_SPEED_FAST);
        }

    }

    private void finedViews() {
        rg = findViewById(R.id.settings_RAD_group);
    }


}