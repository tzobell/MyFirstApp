package com.mycompany.ShapeSounds;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 abstract class that handles setting basic variables
 */
public abstract class ShapeDraw extends Shape implements GoldenShape {
    protected boolean drawCircumCircle = false;
    protected  boolean drawGoldenPoints = false;
    protected Formula formula;
    protected float sWidth;
    protected float sHeight;
    protected float   mScaleX;
   protected float   mScaleY;
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
        shape.mScaleX = mScaleX;
        shape.mScaleY = mScaleY;            //
        return shape;
    }
}
