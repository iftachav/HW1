package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final int DELAY = 1000;
    private final int numOfLanes=3;
    private final int numOfObstaclesInLane=4;
    private ImageView panel_IMG_heart1;
    private ImageView panel_IMG_heart2;
    private ImageView panel_IMG_heart3;
    private ImageView panel_IMG_car1;
    private ImageView panel_IMG_car2;
    private ImageView panel_IMG_car3;
    private ImageView panel_IMG_left_arrow;
    private ImageView panel_IMG_right_arrow;
    private ImageView[] obstacles;


    final Handler handler = new Handler();
    private Runnable r = new Runnable() {
        public void run() {
            Log.d("pttt", "Tick: " + 1);

            moveObstacles();
            newObstacles();



//            if (obstacles[0].getVisibility() == View.GONE){
//                obstacles[0].setVisibility(View.VISIBLE);
//            }
//            else {
//                obstacles[0].setVisibility(View.GONE);
//            }
            handler.postDelayed(this, DELAY);
        }
    };

//    private RelativeLayout rlroad1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

//        int road1Size=rlroad1.getHeight();


        panel_IMG_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(panel_IMG_car1.getVisibility()==View.VISIBLE)
                    return;
                if(panel_IMG_car2.getVisibility()==View.VISIBLE){
                    panel_IMG_car1.setVisibility(View.VISIBLE);
                    panel_IMG_car2.setVisibility(View.GONE);
                }
                if(panel_IMG_car3.getVisibility()==View.VISIBLE){
                    panel_IMG_car3.setVisibility(View.GONE);
                    panel_IMG_car2.setVisibility(View.VISIBLE);
                }

            }
        });
        panel_IMG_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(panel_IMG_car3.getVisibility()==View.VISIBLE)
                    return;
                if(panel_IMG_car2.getVisibility()==View.VISIBLE){
                    panel_IMG_car3.setVisibility(View.VISIBLE);
                    panel_IMG_car2.setVisibility(View.GONE);
                }
                if(panel_IMG_car1.getVisibility()==View.VISIBLE){
                    panel_IMG_car1.setVisibility(View.GONE);
                    panel_IMG_car2.setVisibility(View.VISIBLE);
                }

            }
        });





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
                obstacles[i].setVisibility(View.GONE);

                if(i!=numOfLanes||((i%numOfObstaclesInLane)%numOfLanes)!=0){
                    Log.d("inMove", "HERE: " + 2);

                    obstacles[i+1].setVisibility(View.VISIBLE);
                }
            }
//            if(obstacles[i].getVisibility()==View.VISIBLE){

//                if(i==numOfLanes||(i-numOfObstaclesInLane)%numOfLanes==0){
//                    obstacles[i].setVisibility(View.GONE);
//                }
//                else{
//                    obstacles[i+1].setVisibility(View.VISIBLE);
//                }
//                obstacles[i].setVisibility(View.GONE);
//            }
        }
    }

    private void newObstacles() {
        int random=new Random().nextInt(4);

        obstacles[random*numOfObstaclesInLane].setVisibility(View.VISIBLE);
//        obstacles[0].setVisibility(View.VISIBLE);





//        switch(random = new Random().nextInt(4)){
//            case 0:
//                obstacles[random*numOfObstaclesInLane].setVisibility(View.VISIBLE);
//                return;
//            case 1:
//                obstacles[4].setVisibility(View.VISIBLE);
//                return;
//            case 2:
//                obstacles[8].setVisibility(View.VISIBLE);
//                return;
//        }


    }

    private void findViews() {
        panel_IMG_heart1 = findViewById(R.id.panel_IMG_heart1);
        panel_IMG_heart2 = findViewById(R.id.panel_IMG_heart2);
        panel_IMG_heart3 = findViewById(R.id.panel_IMG_heart3);
        panel_IMG_car1 = findViewById(R.id.panel_IMG_car1);
        panel_IMG_car2 = findViewById(R.id.panel_IMG_car2);
        panel_IMG_car3 = findViewById(R.id.panel_IMG_car3);
        panel_IMG_left_arrow = findViewById(R.id.panel_IMG_left_arrow);
        panel_IMG_right_arrow = findViewById(R.id.panel_IMG_right_arrow);
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
//        RelativeLayout rlroad1 = findViewById(R.id.panel_rlroad1);
    }
}