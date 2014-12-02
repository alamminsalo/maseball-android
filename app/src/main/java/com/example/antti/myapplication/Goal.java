package com.example.antti.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by antti on 27.11.2014.
 */
public class Goal extends Wall {

    public Goal(int left, int top, int right, int bottom){
        super(left,top,right,bottom);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
    }
}
