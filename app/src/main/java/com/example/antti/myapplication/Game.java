package com.example.antti.myapplication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
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

    protected void onResume() {
        super.onResume();
        sman.registerListener(view, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        sman.unregisterListener(view);
    }



}
