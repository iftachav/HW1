package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class Top10Activity extends AppCompatActivity {
    public static final String PLAYER_SCORE_KEY= "PLAYER_SCORE_KEY";
    public static final String RECORD_DB_KEY= "RECORD_DB_KEY";
    private int currentScore;
    private RecordDB recordDB;
    private Bundle bundle;
    private ListFragment listFragment;
    private MapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getBundleExtra(StartActivity.BUNDLE_KEY);
        fusedLocationClient    = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_top10);
        MSPV3 sp = MSPV3.getMe();
        String json1=sp.getString(RECORD_DB_KEY, new Gson().toJson(new RecordDB()));
        recordDB= new Gson().fromJson(json1, RecordDB.class);
        updateRecordDB();
        sp.putString(RECORD_DB_KEY, new Gson().toJson(recordDB));

        listFragment = new ListFragment();
        listFragment.setActivity(this);
        listFragment.setRecordDB(recordDB);
        listFragment.setCallBackList(index -> {
            zoomInMap(index);
        });
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, listFragment).commit();


        mapFragment = new MapFragment();
//        mapFragment.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.map_frame, mapFragment).commit();

    }

    private void zoomInMap(int index) {
        Record current = recordDB.getRecords().get(index);
        mapFragment.setLocation(current.getTime(), current.getScore()+"" , current.getLat(), current.getLon());
    }

    private void updateRecordDB() {
        currentScore = bundle.getInt(PLAYER_SCORE_KEY,-1);
        if(currentScore == -1)
            return;
        String currentTime = new Date().toString();
        ArrayList<Record> records = recordDB.getRecords();
        getLocation((lat, lon) -> {
            records.add(new Record().setScore(currentScore).setTime(currentTime).setLat(lat).setLon(lon));
            records.sort((a, b) -> (b.getScore() - a.getScore()));
            if(records.size() > 10){
                records.remove(10);
            }
        });

    }
    private void getLocation(CallBackOfGPS callBackOfGPS){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation().addOnCompleteListener( task->{
                Location location = task.getResult();
                if(location!=null)
                    callBackOfGPS.getUserLocation(location.getLatitude(), location.getLongitude());
                else
                    callBackOfGPS.getUserLocation(0.0, 0.0);
            });

        }
        else {
            callBackOfGPS.getUserLocation(0.0, 0.0);
        }
    }

}