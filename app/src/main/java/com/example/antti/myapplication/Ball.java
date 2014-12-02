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
    Vector2d pos;
    Vector2d vel;
    Point size;
    Paint paint;

    public Ball(float x, float y){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        size = new Point();
        size.x = 60;
        size.y = 60;

        pos = new Vector2d(x,y);
        vel = new Vector2d();

        this.setBounds((int) x, (int) y, size.x, size.y);
    }

    public void move(double roll, double pitch, int impactX, int impactY){
        if (impactX == 0)
        {
            vel.x += roll * 9.81f * 2;
        }
        else
        {
            vel.x *= -0.5f;
            if (Math.abs(vel.x) < 1.0f)
                vel.x = 0;
            if (impactX < 0)
                pos.x += 3;
            else pos.x -= 3;
        }

        if (impactY == 0)
        {
            vel.y += pitch * 9.81f * 2;
        }
        else
        {
            vel.y *= -0.5f;
            if (Math.abs(vel.y) < 1.0f)
                vel.y = 0;
            if (impactY < 0)
                pos.y += 3;
            else pos.y -= 3;
        }
        pos.x += vel.x * 0.166f;
        pos.y += vel.y * 0.166f;


        //this.setBounds((int)(pos.x-getRadius()),(int)(pos.y-getRadius()),size.x,size.y);

        //System.out.println("BALL at ["+getBounds().centerX()+","+getBounds().centerY()+"]");

        //moveHitbox();
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
        canvas.drawCircle(pos.x,this.pos.y,size.x/2,paint);
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
        return size.x/2;
    }

    public void onCollision(Vector2d vn, float d, float angle){
/*        collisionPoint.x -= this.getBounds().centerX();     //TRANSFORM TO
        collisionPoint.y -= this.getBounds().centerY();     //LOCAL VECTOR

        float collisionAngle = (float) Math.toDegrees(Math.atan2(collisionPoint.x, collisionPoint.y));
        //System.out.println("COLLISION ANGLE: " + normalAngle);

        PointF collisionTangent = new PointF();             //TRANSLATE TO TANGENTIAL VECTOR (+90 DEG, VECTOR OF WALL)
        float tangentAngle = normalAngle + 90;
        collisionTangent.x = (float)(Math.cos(Math.toRadians(tangentAngle)));
        collisionTangent.y = (float)(Math.sin(Math.toRadians(tangentAngle)));
        System.out.println("TANGENT ANGLE: " + tangentAngle);


        //DOT PRODUCT (NORMAL,DIRECTION)
        PointF dirVector = new PointF();
        dirVector.x = (float)(Math.cos(Math.toRadians(getAngle()))*getRadius());
        dirVector.y = (float)(Math.sin(Math.toRadians(getAngle()))*getRadius());
        System.out.println("DIRECTION VECTOR: ["+dirVector.x+","+dirVector.y+"]");

        PointF productVector = new PointF();
        productVector.x = dirVector.x * normalVector.x;
        productVector.y = dirVector.y * normalVector.y;
        float productAngle = (float)(Math.toDegrees(Math.atan2(productVector.x,productVector.y)));
        System.out.println("PRODUCT ANGLE: "+productAngle);

        //ADD (PRODUCT + NORMAL)
        PointF normalVector = new PointF();

        PointF finalVector = new PointF();
        finalVector.x = productVector.x + normalVector.x;
        finalVector.y = productVector.y + normalVector.y;
        //System.out.println("FINAL ANGLE: "+(float)(Math.toDegrees(Math.atan2(finalVector.x,finalVector.y))));




        float moveAngle = (float)getAngle();
        moveAngle += (normalAngle - moveAngle)*2;
        float finalAngle = 180 - (float) (getAngle() - normalAngle);
        System.out.println("FINAL ANGLE: "+moveAngle+" NORMAL ANGLE: "+normalAngle+" MOVE ANGLE: "+Math.round(getAngle()) + " MID ANGLE: "+(normalAngle-(float)getAngle()));

        setAngle(moveAngle);

        //MOVE AWAY FROM WALL
        x -= Math.cos(Math.toRadians(normalAngle))*(getRadius()-Math.abs(normalPoint.x)+2);
        y -= Math.sin(Math.toRadians(normalAngle))*(getRadius()-Math.abs(normalPoint.y)+2);
*/
        Vector2d intersectVector = new Vector2d((float)(d*(Math.cos(angle))), (float)(d*(Math.sin(angle))));
        System.out.println(intersectVector.x+","+intersectVector.y);
        pos.subtract(intersectVector);
        vel.reflect(vn);
        vel.scale(0.8f);
    }

    public void reset(){
        pos.set(360,640);
    }
}
