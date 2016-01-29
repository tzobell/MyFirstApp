package com.mycompany.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.PathShape;


import android.graphics.drawable.shapes.Shape;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;



/**

 */
public class drawtest extends View {
    private ShapeDrawable  mDrawable;
    Canvas can = null;

    float startx;
    float starty;
    float endx;
    float endy;

    float width = 0;
    float canvasWidth = 0;
    float canvasHeight = 0;
    private Paint mBitmapPaint;
    float height;

    Bitmap bm = null;
    Path p;


    boolean startset = false;
    public drawtest(Context context) {
        super(context);
        endy=0;
        endx=0;
        startx=0;
        starty=0;
        width = 0;
        height = 0;
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.setBounds(0, 0, 0, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvasHeight = h;
        canvasWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
        bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        can = new Canvas(bm);

    }


    public void test(){
        setStart(canvasWidth/2, canvasHeight/2);
        double r = 125;
        double tempx = 236;
        double h = startx;
        double k = starty;
        //(x-h)^2 + (y-k)^2 = r^2
        //y = sqrt(r^2 - (x-h)^2) + k

        for(int i = 0; i < 2; ++i){
            double tempy = Math.sqrt((r*r) - Math.pow((tempx - h),2)) + k;
            setEnd((float) tempx, (float) tempy);
            draw();
            invalidate();
            newCanvas();
            tempx+=56;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            canvas.drawBitmap(bm, 0, 0, mBitmapPaint);
            mDrawable.draw(canvas);
            canvas.restore();


    }

    public void Clear(){
        bm = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.ARGB_8888);
        can = new Canvas(bm);
        invalidate();
    }

    void newCanvas(){
            mDrawable.draw(can);
            can.save();
    }

    public void setStart(float X, float Y){
        startx = X;
        starty = Y;
    }

    public void setEnd(float X, float Y){
            endx = X;
            endy = Y;
            width = Math.abs(endx - startx);
            height = Math.abs(endy - starty);
    }

    private void getnewshape() {
        p = new Path();
        p.moveTo(startx, starty);
        p.lineTo(endx, endy);
        mDrawable = new ShapeDrawable(new PathShape(p, (int)width, (int)height));

    }

    public void SetShape(ShapeType t){}

    public void draw() {
        int left = (int)startx;
        int right = (int)endx;
        int top = (int)starty;
        int bottom = (int)endy;
        if(startx > endx){
            left = (int)endx;
            right = (int)startx;
        }
        if(starty > endy){
            top = (int)endy;
            bottom = (int)starty;
        }

        getnewshape();
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.getPaint().setStyle(Paint.Style.STROKE);
        mDrawable.setBounds(0,0,(int)(width),(int)(height));//0, 0, (int) width, (int) height);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {


            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    test();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    break;

                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    break;
            }


        return true;

    }



}