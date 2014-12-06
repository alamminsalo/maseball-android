package com.example.antti.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by antti on 4.12.2014.
 */
public class Hole extends Drawable{
    float radius;
    Vector2d pos;
    Paint paint;

    public Hole(float x, float y, float r){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        radius = r;

        pos = new Vector2d(x,y);
    }

    public void setPosition(float x, float y)
    {
        pos.x = x;
        pos.y = y;
    }
    public Vector2d getPosition(){
        return pos;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(pos.x,pos.y,radius,paint);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public float getRadius()
    {
        return radius;
    }

    public void reset(int x, int y){
        pos.set(x,y);
    }

    public boolean isInside(Vector2d ballpos) {
        float w = Math.abs(ballpos.x - pos.x);
        float h = Math.abs(ballpos.y - pos.y);
        float distance = (float) Math.sqrt(w*w + h*h);
        if (distance < radius)
            return true;
        return false;
    }

}
