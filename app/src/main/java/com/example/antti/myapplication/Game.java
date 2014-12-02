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
    SensorManager sman;
    Sensor sensor;
    Timer timer;
    TimerTask timerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        final Button button = (Button) findViewById(R.id.play);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initGame();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (view != null)
            sman.registerListener(view, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        if (view != null)
            sman.unregisterListener(view);
    }

    void initGame(){
        view = new GameView(this);
        setContentView(view);
        sman = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sman.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sman.registerListener(view,sensor,SensorManager.SENSOR_DELAY_GAME);

        timer = new Timer();
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                view.postInvalidate();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100/32);
    }

}
