package com.example.antti.myapplication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class Game extends Activity{

    GameView view;
    Timer timer;
    TimerTask timerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
    }

    protected void onResume() {
        super.onResume();

        if (view != null)
            view.onResume();
    }

    protected void onPause() {
        super.onPause();
        if (view != null)
            view.onPause();
    }

    public void initGame(View v){
        view = new GameView(this,(SensorManager)getSystemService(SENSOR_SERVICE));
        setContentView(view);

        timer = new Timer();
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                view.postInvalidate();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 16);
    }

    public void showLevels(View v){
        setContentView(R.layout.levels);

        Button button1 = (Button) findViewById(R.id.level_1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initGame(v);
                view.setLevel(0);
            }
        });

        Button button2 = (Button) findViewById(R.id.level_2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initGame(v);
                view.setLevel(1);
            }
        });

        Button button3 = (Button) findViewById(R.id.level_3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initGame(v);
                view.setLevel(2);
            }
        });
    }
}
