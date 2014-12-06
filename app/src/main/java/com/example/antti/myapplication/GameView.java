package com.example.antti.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.*;
import android.hardware.Sensor;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by antti on 25.11.2014.
 */
public class GameView extends View implements SensorEventListener {

    float zsens,xsens,ysens;
    Point size;
    Ball ball[];
    Wall wall[];
    Hole hole[];
    Goal goal;
    Paint bgpaint;
    Rect baselevel;
    Point reset;
    int levelId;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];


    public GameView(Context context, SensorManager msen) {
        super(context);

        mSensorManager = msen;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        onResume();

        //ball = new Ball(200,200,60);
        ball = new Ball[16];
        wall = new Wall[16];
        hole = new Hole[16];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        reset = new Point();

        bgpaint = new Paint();
        bgpaint.setColor(Color.YELLOW);
        bgpaint.setStyle(Paint.Style.FILL);

        display.getSize(size);
        baselevel = new Rect(0,0,size.x,size.y);

        levelId = 0;
        setLevel(levelId);
    }

    public void setLevel(int i){
        levelId = i;

        for (int a=0; a<16; a++) {
            wall[a] = null;
            hole[a] = null;
            ball[a] = null;
        }
        goal = null;

        wall[0] = new Wall(-5,-5,8,size.y);
        wall[1] = new Wall(-5,-5,size.x,8);
        wall[2] = new Wall(-5,size.y-60,size.x,size.y);
        wall[3] = new Wall(size.x-5,0,size.x+8,size.y);

        switch(i){
            case 0:
                wall[4] = new Wall(0,180,520,200);
                wall[5] = new Wall(500,200,520,1000);
                wall[6] = new Wall(180,980,520,1000);
                wall[7] = new Wall(180,320,200,980);

                hole[0] = new Hole(280,400,70);
                hole[1] = new Hole(560,1020,60);

                goal = new Goal(200,800,520,980);

                reset.x = 100;
                reset.y = 100;
                ball[0] = new Ball(0,0,60);
                ball[0].reset(reset.x,reset.y);
                break;

            case 1:
                wall[4] = new Wall(0,220,520,240);
                wall[5] = new Wall(200,480,720,500);
                wall[6] = new Wall(0, 720, 520, 740);
                wall[7] = new Wall(200, 1000, 720, 1020);

                hole[0] = new Hole(280,400,70);
                hole[1] = new Hole(560,820,60);

                goal = new Goal(500,1000,720,1280);
                reset.x = 100;
                reset.y = 100;
                ball[0] = new Ball(0,0,60);
                ball[0].reset(reset.x, reset.y);
                break;

            case 2:
                reset.x = 100;
                reset.y = 100;
                ball[0] = new Ball(140,160,60);
                ball[1] = new Ball(300,900,70);
                ball[2] = new Ball(100,500,100);
                ball[3] = new Ball(340,300,40);

                break;

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        runLogic();

        canvas.drawPaint(bgpaint);

        if (goal != null)
            goal.draw(canvas);

        for (int j=0; hole[j] != null; j++){
            hole[j].draw(canvas);
        }

        for (int j=0; wall[j] != null; j++){
            wall[j].draw(canvas);
        }
        for (int i=0; ball[i] != null; i++)
            ball[i].draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("ROLL: "+mOrientation[2]+", PITCH: "+(-mOrientation[1])+", YAW: "+mOrientation[0],50,50,paint);
    }


    void runLogic(){

        int accuracy = 24;          //Set how many samples we want to take around the circle
        for (int a=0; ball[a] != null; a++) {
            Vector2d vn = new Vector2d();
            for (int j=0; ball[j] != null; j++){

                float dx = ball[j].getPosition().x - ball[a].getPosition().x;
                float dy = ball[j].getPosition().y - ball[a].getPosition().y;
                float distance = (float) Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                if (distance > 0.1 && distance < (ball[j].getRadius()+ball[a].getRadius())) {
                    ball[a].onCollisionWithBall(ball[j],distance);
                    break;
                }
            }
            for (int j = 0; wall[j] != null; j++) {
                for (int i = 0; i < accuracy; i++) {
                    float angle = (float) Math.toRadians(360 / accuracy * i);
                    vn.x = (float) (Math.cos(angle) * (ball[a].getRadius()));
                    vn.y = (float) (Math.sin(angle) * (ball[a].getRadius()));
                    if (wall[j].getBounds().left < vn.x + ball[a].getPosition().x                //RECTANGLE.Contains(x.y) wasn't always working...
                            && wall[j].getBounds().right > vn.x + ball[a].getPosition().x        //Global position comparison
                            && wall[j].getBounds().top < vn.y + ball[a].getPosition().y
                            && wall[j].getBounds().bottom > vn.y + ball[a].getPosition().y) {

                        ball[a].onCollision(vn, wall[j].getBounds());
                        break;
                    }
                }
            }

            for (int j = 0; hole[j] != null; j++) {
                if (hole[j].isInside(ball[a].getPosition()))
                    ball[a].reset(reset.x, reset.y);
            }

            ball[a].move(mOrientation[2], -mOrientation[1]);            //Calibration issues..? when debugging these values seem about right but movement
            //is occasionally unpredictable

            if (goal != null) {
                if (goal.getBounds().contains((int) ball[a].getPosition().x, (int) ball[a].getPosition().y))
                    setLevel(++levelId);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            zsens = event.values[0];
            xsens = event.values[1];
            ysens = event.values[2];

        }
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            //Log.i("OrientationTestActivity", String.format("Orientation: %f, %f, %f",
            //        mOrientation[0], mOrientation[1], mOrientation[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        for (int a=0; ball[a]!=null; a++)
            ball[a].reset(reset.x, reset.y);
        return false;
    }

    public void onResume(){
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause(){
        mSensorManager.unregisterListener(this);
    }
}
