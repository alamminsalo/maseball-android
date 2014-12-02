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
    Ball ball;
    Wall wall[];
    Goal goal;
    Paint bgpaint;
    Rect baselevel;
    Point reset;
    int levelId;


    public GameView(Context context) {
        super(context);
        ball = new Ball(200,200,60);
        wall = new Wall[16];
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

    void setLevel(int i){
        for (int a=0; a<16; a++)
            wall[a] = null;
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
                goal = new Goal(200,800,520,980);

                reset.x = 100;
                reset.y = 100;
                ball.reset(reset.x,reset.y);
                break;

            case 1:
                wall[4] = new Wall(0,220,520,240);
                wall[5] = new Wall(200,480,720,500);
                wall[6] = new Wall(0, 720, 520, 740);
                wall[7] = new Wall(200, 1000, 720, 1020);

                goal = new Goal(500,1000,720,1280);
                reset.x = 100;
                reset.y = 100;
                ball.reset(reset.x,reset.y);

                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        runLogic();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);


        canvas.drawPaint(bgpaint);
        goal.draw(canvas);
        //HERE paint holes
        for (int j=0; wall[j] != null; j++){
            wall[j].draw(canvas);
        }
        ball.draw(canvas);
        canvas.drawText("ROLL: "+(float)xsens+", PITCH: "+(float)zsens,50,50,paint);
    }


    void runLogic(){

        int accuracy = 24;          //Set how many samples we want to take around the circle
        Vector2d vn = new Vector2d();
        for (int j=0; wall[j] != null; j++){
            for (int i=0; i<accuracy; i++){
                float angle = (float) Math.toRadians(360/accuracy * i);
                vn.x = (float) (Math.cos(angle)*ball.getRadius());
                vn.y = (float) (Math.sin(angle)*ball.getRadius());
                if (wall[j].getBounds().left < vn.x+ball.getPosition().x                //RECTANGLE.Contains(x.y) wasn't always working...
                        && wall[j].getBounds().right > vn.x+ball.getPosition().x        //Global position comparison
                        && wall[j].getBounds().top < vn.y+ball.getPosition().y
                        && wall[j].getBounds().bottom > vn.y+ball.getPosition().y){

                    ball.onCollision(vn,wall[j].getBounds());
                    break;
                }
            }
        }

        ball.move(xsens, zsens);            //Calibration issues..? when debugging these values seem about right but movement
                                            //is occasionally unpredictable

        if (goal.getBounds().contains((int)ball.getPosition().x,(int)ball.getPosition().y))
            setLevel(++levelId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            zsens = event.values[0];
            xsens = event.values[1];
            ysens = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        ball.reset(reset.x, reset.y);
        return false;
    }
}
