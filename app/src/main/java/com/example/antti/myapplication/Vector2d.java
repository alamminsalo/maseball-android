package com.example.antti.myapplication;

/**
 * Created by antti on 2.12.2014.
 */
public class Vector2d {
    public float x;
    public float y;

    public Vector2d(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2d(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d v){
        this.x = v.x;
        this.y = v.y;
    }

    public static float dotProduct(Vector2d v1,Vector2d v2){
        return v1.x*v2.x + v1.y*v2.y;
    }
/*
    public static float crossProduct(Vector2d v1, Vector2d v2){ //NOT SURE OF IMPLEMENTATION
        return v1.x*v2.y - v2.x*v1.y;
    }
*/

    public static Vector2d addProduct(Vector2d v1, Vector2d v2){
        return new Vector2d(v1.x+v2.x, v1.y + v2.y);
    }

    public float angle(){
        return (float)Math.atan2(this.x,this.y);
    }

    public void rotate(float angle){
        x = (float) (x*(Math.cos(Math.toRadians(angle))) - y*(Math.sin(Math.toRadians(angle))));
        y = (float) (y*(Math.cos(Math.toRadians(angle))) + x*(Math.sin(Math.toRadians(angle))));
    }

    public void mult(Vector2d v){
        this.x *= v.x;
        this.y *= v.y;
    }
    public void mult(float vx, float vy){
        this.x *= vx;
        this.y *= vy;
    }

    public void add(Vector2d v){
        this.x += v.x;
        this.y += v.y;
    }
    public void add(float vx, float vy){
        this.x += vx;
        this.y += vy;
    }

    public void subtract(Vector2d v){
        this.x -= v.x;
        this.y -= v.y;
    }
    public void subtract(float vx, float vy){
        this.x -= vx;
        this.y -= vy;
    }

    public Vector2d subtractProduct(Vector2d v){
        return new Vector2d(this.x - v.x,this.y - v.y);
    }
    public Vector2d subtractProduct(float vx, float vy){
        return new Vector2d(this.x - vx,this.y - vy);
    }

    public Vector2d normalizedProduct(){
        return new Vector2d(Math.abs(x),Math.abs(y));
    }

    public float normalize(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public void scale(float r){
        this.x *= r;
        this.y *= r;
    }

    public float angleBetween(Vector2d v){
        return this.angle() - v.angle();
    }

    public void negate(){
        this.x *= -1;
        this.y *= -1;
    }

    public Vector2d reflectionProduct(Vector2d vn){
        Vector2d ref = new Vector2d(vn);
        float dot = Vector2d.dotProduct(this,vn);
        dot = (float) (dot/(Math.pow(vn.normalize(),2)));
        ref.scale(dot);
        ref.scale(2);
        ref.subtract(this);
        ref.negate();
        return ref;
    }

    public void reflect(Vector2d vn){
        Vector2d ref = new Vector2d(vn);
        float dot = Vector2d.dotProduct(this,vn);
        dot = (float) (dot/(Math.pow(vn.normalize(),2)));
        ref.scale(dot*2);
        ref.subtract(this);
        ref.negate();
        set(ref);
    }

    public void set(Vector2d v){
        this.x = v.x;
        this.y = v.y;
    }
    public void set(float vx, float vy){
        this.x = vx;
        this.y = vy;
    }

    public Vector2d getUnitVector(){
        return new Vector2d((float)Math.sin(this.angle()),(float)Math.cos(this.angle()));
    }

}
