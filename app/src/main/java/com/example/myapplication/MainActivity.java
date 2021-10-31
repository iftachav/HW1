package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final int DELAY = 1000;
    private final int numOfLanes=3;
    private final int numOfObstaclesInLane=4;
    private int obstaclesFlags[];
    private int carsFlags[];
    private int numOfLifes=3;
    private boolean newFlag;
    private ImageView panel_IMG_left_arrow;
    private ImageView panel_IMG_right_arrow;
    private ImageView[] obstacles;
    private ImageView[] cars;
    private ImageView[] hearts;



    final Handler handler = new Handler();
    private Runnable r = new Runnable() {
        public void run() {
            Log.d("pttt", "Tick: " + 1);

            moveObstacles();
            if(newFlag)
                newObstacles();
            else
                newFlag=true;
            checkCrush();

            handler.postDelayed(this, DELAY);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newFlag=true;
        findViews();

        obstaclesFlags = new int[obstacles.length];
        carsFlags = new int[cars.length];
        Arrays.fill(obstaclesFlags,0);
        Arrays.fill(carsFlags,0);



        panel_IMG_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < cars.length; i++) {
                    if(cars[i].getVisibility()==View.VISIBLE)
                        carsFlags[i]=1;
                }


                for (int i = 1; i < carsFlags.length; i++) {
                    if(carsFlags[i]==1){
                        cars[i].setVisibility(View.GONE);
                        cars[i-1].setVisibility(View.VISIBLE);
                    }
                }
                Arrays.fill(carsFlags,0);
            }
        });
        panel_IMG_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < cars.length; i++) {
                    if(cars[i].getVisibility()==View.VISIBLE)
                        carsFlags[i]=1;
                }

                for (int i = 0; i < carsFlags.length-1 ; i++) {
                    if(carsFlags[i]==1) {
                        cars[i].setVisibility(View.GONE);
                        cars[i + 1].setVisibility(View.VISIBLE);
                    }
                }
                Arrays.fill(carsFlags,0);
            }
        });





    }

    private void checkCrush() {
        for (int i = 0; i < numOfLanes; i++) {
            if(cars[i].getVisibility()==View.VISIBLE && obstacles[i*numOfObstaclesInLane+numOfLanes].getVisibility()==View.VISIBLE){
                Toast.makeText(this, "CRUSH!", Toast.LENGTH_SHORT).show();
                decreseLife();



                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                }


            }
        }
    }

    private void decreseLife() {
        hearts[numOfLifes-1].setVisibility(View.GONE);
        numOfLifes--;
        if(numOfLifes<1){
            numOfLifes=3;
            for (int i = 0; i < numOfLifes; i++) {
                hearts[i].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(r, DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
    }

    private void moveObstacles() {
        for (int i = 0; i < obstacles.length; i++) {
            if(obstacles[i].getVisibility()==View.VISIBLE){
                obstaclesFlags[i]=1;
            }
        }


        for (int i = obstaclesFlags.length-1; i >= 0; i--) {
            if(obstaclesFlags[i]==1) {

                if (i != numOfObstaclesInLane - 1 && (i - numOfObstaclesInLane < 0 || i % numOfObstaclesInLane != 3)) {
                    obstacles[i + 1].setVisibility(View.VISIBLE);
                }
                obstacles[i].setVisibility(View.GONE);
            }

//                works from here but with 1 extra line.
//                if(i==numOfObstaclesInLane-1||(i-numOfObstaclesInLane>0 && i%numOfObstaclesInLane ==3)){
////                    obstacles[i].setVisibility(View.GONE);
//                }
//                else{
//                    obstacles[i+1].setVisibility(View.VISIBLE);
//                }
//                obstacles[i].setVisibility(View.GONE);
//            }
        }
        Arrays.fill(obstaclesFlags,0);
    }

    private void newObstacles() {
        int random=new Random().nextInt(3);

        obstacles[random*numOfObstaclesInLane].setVisibility(View.VISIBLE);
        newFlag=false;

    }

    private void findViews() {
        panel_IMG_left_arrow = findViewById(R.id.panel_IMG_left_arrow);
        panel_IMG_right_arrow = findViewById(R.id.panel_IMG_right_arrow);
        hearts = new ImageView[]{
                findViewById(R.id.panel_IMG_heart1),
                findViewById(R.id.panel_IMG_heart2),
                findViewById(R.id.panel_IMG_heart3)
        };
        cars = new ImageView[]{
                findViewById(R.id.panel_IMG_car1),
                findViewById(R.id.panel_IMG_car2),
                findViewById(R.id.panel_IMG_car3)
        };
        obstacles= new ImageView[]{
            findViewById(R.id.panel_IMG_obstacle11),
            findViewById(R.id.panel_IMG_obstacle12),
            findViewById(R.id.panel_IMG_obstacle13),
            findViewById(R.id.panel_IMG_obstacle14),//3
            findViewById(R.id.panel_IMG_obstacle21),
            findViewById(R.id.panel_IMG_obstacle22),
            findViewById(R.id.panel_IMG_obstacle23),
            findViewById(R.id.panel_IMG_obstacle24),//7
            findViewById(R.id.panel_IMG_obstacle31),
            findViewById(R.id.panel_IMG_obstacle32),
            findViewById(R.id.panel_IMG_obstacle33),
            findViewById(R.id.panel_IMG_obstacle34),//11

        };
    }
}