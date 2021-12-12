package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.etc.MSPV3;
import com.example.myapplication.R;

public class StartActivity extends AppCompatActivity {

    public static final String BUNDLE_KEY="BUNDLE_KEY";
    private Button[] buttons;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grantLocationPermission();
        bundle = new Bundle();
        setContentView(R.layout.activity_start);
        findViews();
        initButtons();


    }

    private void initButtons() {
        buttons[0]  .setOnClickListener(v -> myStartAcitivity(MainActivity.class));
        buttons[1]  .setOnClickListener(v -> myStartAcitivity(SettingsActivity.class));
        buttons[2]  .setOnClickListener(v -> myStartAcitivity(Top10Activity.class));
    }

    private void myStartAcitivity(Class<?> activityClass) {
        Intent myIntent = new Intent(this, activityClass);
        initBundle();
        myIntent.putExtra(BUNDLE_KEY, bundle);
        startActivity(myIntent);
    }

    private void grantLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}  , 44);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
    }

    private void initBundle() {
        MSPV3 sp = MSPV3.getMe();
        bundle.putString(MainActivity.GAME_TYPE_KEY, sp.getString(MainActivity.GAME_TYPE_KEY, MainActivity.GAME_TYPE_BUTTONS));
        bundle.putInt(MainActivity.GAME_SPEED_KEY, sp.getInt(MainActivity.GAME_SPEED_KEY, MainActivity.GAME_SPEED_FAST));
    }

    private void findViews() {
        buttons=new Button[]{
                findViewById(R.id.start_BTN_play),
                findViewById(R.id.start_BTN_settings),
                findViewById(R.id.start_BTN_top_10)
        };
    }


}