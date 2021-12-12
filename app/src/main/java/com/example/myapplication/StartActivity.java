package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    public static final String BUNDLE_KEY="BUNDLE_KEY";
    private Button[] buttons;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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