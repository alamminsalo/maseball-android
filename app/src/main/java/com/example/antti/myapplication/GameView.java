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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;

/**
 * Created by antti on 25.11.2014.
 */
public class GameView extends View implements SensorEventListener {

    float zsens,xsens,ysens;
    Point size;
    Ball ball;
    Wall wall[];
    Paint bgpaint;
    Rect baselevel;


    public GameView(Context context) {
        super(context);
        ball = new Ball(200,200);
        wall = new Wall[16];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        Display display = wm.getDefaultDisplay();
        size = new Point();


        bgpaint = new Paint();
        bgpaint.setColor(Color.BLUE);
        bgpaint.setStyle(Paint.Style.FILL);

        display.getSize(size);

        System.out.println(context.getResources().getDisplayMetrics().scaledDensity);
        baselevel = new Rect(0,0,size.x,size.y);

        setLevel(0);
    }

    void setLevel(int i){
        for (int a=0; a<16; a++)
            wall[a] = null;

        switch(i){
            case 0:
                wall[0] = new Wall(-5,-5,5,size.y);
                wall[1] = new Wall(-5,-5,size.x,5);
                wall[2] = new Wall(-5,size.y-60,size.x,size.y);
                wall[3] = new Wall(size.x-5,0,size.x+5,size.y);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = getWidth();
        int y = getHeight();
        int radius = 30;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#000000"));
        //canvas.drawCircle(ball.getX(),ball.getY(), radius, paint);
        canvas.drawRect(baselevel,bgpaint);

        for (int j=0; wall[j] != null; j++){
            wall[j].draw(canvas);
        }

        ball.draw(canvas);

    }


    void runLogic(float roll, float pitch){
        int impactX = 0;
        int impactY = 0;


        int accuracy = 4;
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
                    //System.out.println("COLLISION AT SLICE "+i+" AT ["+(vn.x)+","+(vn.y) + "] ANGLE: "+ angle );
                    float dx = vn.x+ball.getPosition().x - wall[j].getBounds().left;
                    float dy = vn.y+ball.getPosition().y - wall[j].getBounds().top;
                    float h = Math.min(dx,dy);
                    System.out.println("Collision point ["+dx+","+dy+"], min "+h);
                    ball.onCollision(vn,h,angle);
                    break;
                }
            }
        }

        ball.move(roll,pitch,impactX,impactY);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            zsens = event.values[0];
            xsens = event.values[1];
            ysens = event.values[2];
        }
        runLogic(xsens,zsens);
        this.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        ball.reset();
        return false;
    }
}
