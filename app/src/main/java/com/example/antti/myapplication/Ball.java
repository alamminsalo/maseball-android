package com.example.antti.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


/**
 * Created by antti on 26.11.2014.
 */
public class Ball extends Drawable {
    float maxspeed;
    float speedfactor;
    float radius;
    Vector2d pos;       //Position vector relative to (0,0)
    Vector2d vel;       //Velocity vector relative to position vector
    Paint paint;

    public Ball(float x, float y, float r){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        radius = r;

        maxspeed = r*9;       //We try to ensure ball won't ever end up beyond a wall..poorly.
        speedfactor = 1.0f * 100/radius;

        pos = new Vector2d(x,y);
        vel = new Vector2d();
    }

    public void move(float roll, float pitch){
        vel.x += roll * 9.81f * speedfactor;
        vel.y += pitch * 9.81f * speedfactor;

        vel.x = Math.abs(vel.x) > maxspeed ? vel.x > 0 ? maxspeed : -maxspeed : vel.x;      //Limit the speed to max
        vel.y = Math.abs(vel.y) > maxspeed ? vel.y > 0 ? maxspeed : -maxspeed : vel.y;

        pos.x += vel.x * 0.06f;
        pos.y += vel.y * 0.06f;
    }
    public void setPosition(float x, float y)
    {
        pos.x = x;
        pos.y = y;
    }
    public Vector2d getPosition(){
        return pos;
    }

    public void setVelocity(float vx, float vy){
        vel.x = vx;
        vel.y = vy;
    }
    public Vector2d getVector(){
       return vel;
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

    public void onCollision(Vector2d vn, Rect object){

        while (object.left < vn.x+pos.x         //Move the ball away from the wall...(lazy method)
                && object.right > vn.x+pos.x    //In real cases better to use circle segments intersecting the object
                && object.top < vn.y+pos.y      //Note: seems to introduce "stickyness"
                && object.bottom > vn.y+pos.y) {

            pos.subtract(vn.getUnitVector());   //Vector values transformed to [-1...1, -1...1]
        }
        vel.reflect(vn);                        //Reflect the movement vector along the normal vector
        vel.scale(0.6f);                        //Lose some speed
    }

    public void onCollisionWithBall(Ball ball, float distance){
        Vector2d collisionVec = pos.subtractProduct(ball.getPosition());
        collisionVec.scale(1/distance);

        float aci = Vector2d.dotProduct(vel,collisionVec);
        float bci = Vector2d.dotProduct(ball.getVector(),collisionVec);

        this.vel.add(collisionVec.scaleProduct(bci - aci));
        ball.getVector().add(collisionVec.scaleProduct(aci-bci));

    }

    public void reset(int x, int y){
        pos.set(x,y);
    }
}
