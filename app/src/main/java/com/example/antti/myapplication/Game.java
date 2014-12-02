package com.example.antti.myapplication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;

public class Game extends Activity{
    /** Called when the activity is first created. */


    GameView view;
    SensorManager sman;
    Sensor sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        view = new GameView(this);
        setContentView(view);
        sman = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sman.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sman.registerListener(view,sensor,SensorManager.SENSOR_DELAY_GAME);
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
