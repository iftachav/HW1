package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.fragments.AccFragment;
import com.example.myapplication.fragments.ButtonsFragment;
import com.example.myapplication.callbacks.CallBackMovePlayer;
import com.example.myapplication.R;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String GAME_TYPE_KEY="GAME_TYPE_KEY";
    public static final String GAME_TYPE_BUTTONS="GAME_TYPE_BUTTONS";
    public static final String GAME_TYPE_ACC="GAME_TYPE_ACC";
    public static final String GAME_SPEED_KEY="GAME_SPEED_KEY";
    public static final int GAME_SPEED_SLOW=1000;
    public static final int GAME_SPEED_FAST=500;
    private final int COINSCORE = 50;
    private final int TICKSCORE = 10;
    private Bundle  bundle;
    private ButtonsFragment fragmentButtons;
    private AccFragment fragmentAcc;
    private int delay = 1000;
    private final int numOfLanes=5;
    private final int numOfObstaclesInLane=7;
    private int obstaclesFlags[];
    private int carsFlags[];
    private int dollarsFlags[];
    private int numOfLifes=3;
    private boolean newFlag;
    private ImageView[] booms;
    private ImageView[] roads;
    private ImageView[] obstacles;
    private ImageView[] cars;
    private ImageView[] hearts;
    private ImageView[] dollars;
    private TextView score;
    private MediaPlayer mpCrush;
    private MediaPlayer mpCoin;
    private int currentScore=0;
    private float playbackSpeed;
    private final CallBackMovePlayer callBackMovePlayer = direction -> {
        if(direction)
            rightArrowAction();
        else
            leftArrowAction();
    };


    final Handler handler = new Handler();
    private Runnable r = new Runnable() {
        public void run() {
            Log.d("pttt", "Tick: " + 1);
            for (int i = 0; i < booms.length; i++) {
                if(booms[i].getVisibility()==View.VISIBLE) {
                    booms[i].setVisibility(View.INVISIBLE);
                    cars[i].setVisibility(View.VISIBLE);
                }
            }
            moveObstaclesAndDollars();
            if(newFlag)
                newObstacles();
            else
                newDollars();

            checkCrush();
            increaseScore(TICKSCORE);
            handler.postDelayed(this, delay);
        }
    };


    private void newDollars(){
        int random=new Random().nextInt(numOfLanes);
        dollars[random*numOfObstaclesInLane].setVisibility(View.VISIBLE);
        newFlag=true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = getIntent().getBundleExtra(StartActivity.BUNDLE_KEY);
        setContentView(R.layout.activity_main);
        mpCrush = MediaPlayer.create(this, R.raw.sound_car_crush);
        mpCoin = MediaPlayer.create(this, R.raw.sound_coin);
        newFlag=true;
        findViews();
        initGameType();
        initGameSpeed();
        obstaclesFlags = new int[obstacles.length];//35
        carsFlags = new int[cars.length];//5
        dollarsFlags = new int[dollars.length];//35
        Arrays.fill(obstaclesFlags,0);
        Arrays.fill(carsFlags,0);
        Arrays.fill(dollarsFlags,0);
    }
    private void gameOver(){
        bundle.putInt(Top10Activity.PLAYER_SCORE_KEY,currentScore);
        Intent myIntent = new Intent(this, Top10Activity.class);
        myIntent.putExtra(StartActivity.BUNDLE_KEY, bundle);
        startActivity(myIntent);
        finish();
    }

    private void initGameSpeed() {
        delay = bundle.getInt(GAME_SPEED_KEY, GAME_SPEED_SLOW);
        if(delay==GAME_SPEED_FAST)
            playbackSpeed = 2.0f;
        else
            playbackSpeed = 1.0f;
        mpCoin.setPlaybackParams(mpCoin.getPlaybackParams().setSpeed(playbackSpeed));
        mpCrush.setPlaybackParams(mpCrush.getPlaybackParams().setSpeed(playbackSpeed));
        if(mpCrush.isPlaying() || mpCoin.isPlaying()){
            mpCoin.pause();
            mpCrush.pause();
        }


    }

    private void initGameType() {
        if(bundle.getString(GAME_TYPE_KEY, GAME_TYPE_BUTTONS).equals(GAME_TYPE_BUTTONS)){
            initGameSpeed();//need to check.
            fragmentButtons = new ButtonsFragment();
            fragmentButtons.setActivity(this);
            fragmentButtons.setCallBackMovePlayer(callBackMovePlayer);
            getSupportFragmentManager().beginTransaction().add(R.id.panel_FRG_type, fragmentButtons).commit();
        }
        else{
            fragmentAcc = new AccFragment();
            fragmentAcc.setActivity(this);
            fragmentAcc.setCallBackMovePlayer(callBackMovePlayer);
            getSupportFragmentManager().beginTransaction().add(R.id.panel_FRG_type, fragmentAcc).commit();
        }


    }

    private void leftArrowAction() {
        for (int i = 0; i < cars.length; i++) {
            if(cars[i].getVisibility()==View.VISIBLE)
                carsFlags[i]=1;
        }


        for (int i = 1; i < carsFlags.length; i++) {
            if(carsFlags[i]==1){
                cars[i].setVisibility(View.INVISIBLE);
                cars[i-1].setVisibility(View.VISIBLE);
            }
        }
        Arrays.fill(carsFlags,0);
        checkCrush();
    }

    private void rightArrowAction() {
        for (int i = 0; i < cars.length; i++) {
            if(cars[i].getVisibility()==View.VISIBLE)
                carsFlags[i]=1;
        }

        for (int i = 0; i < carsFlags.length-1 ; i++) {
            if(carsFlags[i]==1) {
                cars[i].setVisibility(View.INVISIBLE);
                cars[i + 1].setVisibility(View.VISIBLE);
            }
        }
        Arrays.fill(carsFlags,0);
        checkCrush();
    }

    private void checkCrush() {
        for (int i = 0; i < numOfLanes; i++) {
            if(cars[i].getVisibility()==View.VISIBLE && obstacles[i*numOfObstaclesInLane+numOfLanes+1].getVisibility()==View.VISIBLE){
                booms[i].setVisibility(View.VISIBLE);
                cars[i].setVisibility(View.INVISIBLE);
                obstacles[i*numOfObstaclesInLane+numOfLanes+1].setVisibility(View.INVISIBLE);
                Toast.makeText(this, "CRUSH!", Toast.LENGTH_SHORT).show();
                decreseLife();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                mpCrush.start();
            }
            if(cars[i].getVisibility()==View.VISIBLE && dollars[i*numOfObstaclesInLane+numOfLanes+1].getVisibility()==View.VISIBLE){
                dollars[i*numOfObstaclesInLane+numOfLanes+1].setVisibility(View.INVISIBLE);
//                int currentScore =Integer.parseInt(score.getText().toString());
                increaseScore(COINSCORE);
                mpCoin.start();
            }
        }
    }

    private void increaseScore(int up) {
        currentScore+=up;
        score.setText(currentScore+"m");
    }

    private void decreseLife() {
        hearts[numOfLifes-1].setVisibility(View.INVISIBLE);
        numOfLifes--;
        if(numOfLifes<1)
            gameOver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(r, delay);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
    }

    private void moveObstaclesAndDollars() {
        for (int i = 0; i < obstacles.length; i++) {
            if(obstacles[i].getVisibility()==View.VISIBLE)
                obstaclesFlags[i]=1;
            if(dollars[i].getVisibility()==View.VISIBLE)
                dollarsFlags[i]=1;
        }


        for (int i = obstaclesFlags.length-1; i >= 0; i--) {
            if(obstaclesFlags[i]==1) {

                if ((i+1)%numOfObstaclesInLane!=0) {
                    obstacles[i + 1].setVisibility(View.VISIBLE);
                }
                obstacles[i].setVisibility(View.INVISIBLE);
            }

            if(dollarsFlags[i]==1){
                if ((i+1)%numOfObstaclesInLane!=0) {
                    dollars[i + 1].setVisibility(View.VISIBLE);
                }
                dollars[i].setVisibility(View.INVISIBLE);
            }

        }
        Arrays.fill(obstaclesFlags,0);
        Arrays.fill(dollarsFlags,0);
    }

    private void newObstacles() {
        int random=new Random().nextInt(numOfLanes);
        obstacles[random*numOfObstaclesInLane].setVisibility(View.VISIBLE);
        newFlag=false;
    }

    private void findViews() {
        score = findViewById(R.id.panel_LBL_score);
        roads = new ImageView[]{
                findViewById(R.id.panel_IMG_road1),
                findViewById(R.id.panel_IMG_road2),
                findViewById(R.id.panel_IMG_road3),
                findViewById(R.id.panel_IMG_road4),
                findViewById(R.id.panel_IMG_road5)
        };
        Glide.with(this).load(R.drawable.img_road).into(roads[0]);
        Glide.with(this).load(R.drawable.img_road).into(roads[1]);
        Glide.with(this).load(R.drawable.img_road).into(roads[2]);
        Glide.with(this).load(R.drawable.img_road).into(roads[3]);
        Glide.with(this).load(R.drawable.img_road).into(roads[4]);
        booms = new ImageView[]{
                findViewById(R.id.panel_IMG_boom1),
                findViewById(R.id.panel_IMG_boom2),
                findViewById(R.id.panel_IMG_boom3),
                findViewById(R.id.panel_IMG_boom4),
                findViewById(R.id.panel_IMG_boom5)
        };
        hearts = new ImageView[]{
                findViewById(R.id.panel_IMG_heart1),
                findViewById(R.id.panel_IMG_heart2),
                findViewById(R.id.panel_IMG_heart3)
        };
        cars = new ImageView[]{
                findViewById(R.id.panel_IMG_car1),
                findViewById(R.id.panel_IMG_car2),
                findViewById(R.id.panel_IMG_car3),
                findViewById(R.id.panel_IMG_car4),
                findViewById(R.id.panel_IMG_car5)
        };
        obstacles= new ImageView[]{
            findViewById(R.id.panel_IMG_obstacle11),
            findViewById(R.id.panel_IMG_obstacle12),
            findViewById(R.id.panel_IMG_obstacle13),
            findViewById(R.id.panel_IMG_obstacle14),
            findViewById(R.id.panel_IMG_obstacle15),
            findViewById(R.id.panel_IMG_obstacle16),
            findViewById(R.id.panel_IMG_obstacle17),
            findViewById(R.id.panel_IMG_obstacle21),
            findViewById(R.id.panel_IMG_obstacle22),
            findViewById(R.id.panel_IMG_obstacle23),
            findViewById(R.id.panel_IMG_obstacle24),
            findViewById(R.id.panel_IMG_obstacle25),
            findViewById(R.id.panel_IMG_obstacle26),
            findViewById(R.id.panel_IMG_obstacle27),
            findViewById(R.id.panel_IMG_obstacle31),
            findViewById(R.id.panel_IMG_obstacle32),
            findViewById(R.id.panel_IMG_obstacle33),
            findViewById(R.id.panel_IMG_obstacle34),
            findViewById(R.id.panel_IMG_obstacle35),
            findViewById(R.id.panel_IMG_obstacle36),
            findViewById(R.id.panel_IMG_obstacle37),
            findViewById(R.id.panel_IMG_obstacle41),
            findViewById(R.id.panel_IMG_obstacle42),
            findViewById(R.id.panel_IMG_obstacle43),
            findViewById(R.id.panel_IMG_obstacle44),
            findViewById(R.id.panel_IMG_obstacle45),
            findViewById(R.id.panel_IMG_obstacle46),
            findViewById(R.id.panel_IMG_obstacle47),
            findViewById(R.id.panel_IMG_obstacle51),
            findViewById(R.id.panel_IMG_obstacle52),
            findViewById(R.id.panel_IMG_obstacle53),
            findViewById(R.id.panel_IMG_obstacle54),
            findViewById(R.id.panel_IMG_obstacle55),
            findViewById(R.id.panel_IMG_obstacle56),
            findViewById(R.id.panel_IMG_obstacle57),
        };
        dollars = new ImageView[]{
                findViewById(R.id.panel_IMG_dollar11),
                findViewById(R.id.panel_IMG_dollar12),
                findViewById(R.id.panel_IMG_dollar13),
                findViewById(R.id.panel_IMG_dollar14),
                findViewById(R.id.panel_IMG_dollar15),
                findViewById(R.id.panel_IMG_dollar16),
                findViewById(R.id.panel_IMG_dollar17),
                findViewById(R.id.panel_IMG_dollar21),
                findViewById(R.id.panel_IMG_dollar22),
                findViewById(R.id.panel_IMG_dollar23),
                findViewById(R.id.panel_IMG_dollar24),
                findViewById(R.id.panel_IMG_dollar25),
                findViewById(R.id.panel_IMG_dollar26),
                findViewById(R.id.panel_IMG_dollar27),
                findViewById(R.id.panel_IMG_dollar31),
                findViewById(R.id.panel_IMG_dollar32),
                findViewById(R.id.panel_IMG_dollar33),
                findViewById(R.id.panel_IMG_dollar34),
                findViewById(R.id.panel_IMG_dollar35),
                findViewById(R.id.panel_IMG_dollar36),
                findViewById(R.id.panel_IMG_dollar37),
                findViewById(R.id.panel_IMG_dollar41),
                findViewById(R.id.panel_IMG_dollar42),
                findViewById(R.id.panel_IMG_dollar43),
                findViewById(R.id.panel_IMG_dollar44),
                findViewById(R.id.panel_IMG_dollar45),
                findViewById(R.id.panel_IMG_dollar46),
                findViewById(R.id.panel_IMG_dollar47),
                findViewById(R.id.panel_IMG_dollar51),
                findViewById(R.id.panel_IMG_dollar52),
                findViewById(R.id.panel_IMG_dollar53),
                findViewById(R.id.panel_IMG_dollar54),
                findViewById(R.id.panel_IMG_dollar55),
                findViewById(R.id.panel_IMG_dollar56),
                findViewById(R.id.panel_IMG_dollar57),
        };
    }
}