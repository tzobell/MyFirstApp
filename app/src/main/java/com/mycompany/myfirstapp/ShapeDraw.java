package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by Owner on 12/11/2015.
 */
public abstract class ShapeDraw extends Shape implements GoldenShape {
    protected boolean drawCircumCircle = false;
    protected  boolean drawGoldenPoints = false;
    protected Formula formula;
    protected float sWidth;
    protected float sHeight;
    protected float   mScaleX;    // cached from onResize
   protected float   mScaleY;    // cached from onResize
    public ShapeDraw(float width,float height){
        sWidth = width;
        sHeight = height;
    }
    public Formula[] GetGoldenPoints(){
        return formula.GetGoldenPoints();
    }
    //public Formula[] GetGoldenLines();
    public Formula GetFormula(){
        return formula;
    }
    public void DrawCircumCircle(boolean circumCircle){
        drawCircumCircle = circumCircle;
    }
    public void DrawGoldenPoints(boolean drawGpoints){
        drawGoldenPoints = drawGpoints;
    }
    @Override
    public abstract void draw(Canvas canvas, Paint paint);

    @Override
    protected void onResize(float width, float height) {
        mScaleX = width / sWidth;
        mScaleY = height / sHeight;
    }

    @Override
    public ShapeDraw clone()throws CloneNotSupportedException{
        return (ShapeDraw) super.clone();
    }
    protected ShapeDraw clone(ShapeDraw shape){
        shape.sWidth = sWidth;
        shape.sHeight = sHeight;
        shape.mScaleX = mScaleX;    // cached from onResize
        shape.mScaleY = mScaleY;    // cached from onResize

        //shape.harmoniousPoints = harmoniousPoints.clone();
        return shape;
    }
}
