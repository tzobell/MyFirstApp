package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.util.Pair;

/**
 * Created by Owner on 9/11/2015.
 */
public class GPathShape extends ShapeDraw {

    LineFormula line;
boolean drawGoldenPoints = false;
    Pair<Float,Float> keyPoints[];
    Path mPath;
    public GPathShape(float startx, float starty, float endx, float endy,Path p,float width, float height, boolean main){
        super(width,height);
       mPath = p;
        keyPoints = new Pair[2];
        keyPoints[0] = new Pair<Float,Float>(startx,starty);
        keyPoints[1] = new Pair<Float,Float>(endx,endy);

        line = new LineFormula(startx,starty,endx,endy,Side.above,main);
        p = new Path();
        p.moveTo(startx, starty);
        p.lineTo(endx, endy);
        formula = line;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawPath(mPath, paint);
      //  if(drawGoldenPoints){
            Formula gpoints[] = GetGoldenPoints();
            for(Formula f: gpoints){
                float h,k,radius;
                Pair<Float,Float> p = f.GetClosestPoint(0,0);
                float r = 2.5f;
                if(f instanceof CircleFormula){
                    CircleFormula cf = (CircleFormula)f;
                    h = cf.h;
                    k = cf.k;
                    radius = cf.radius;
                    canvas.drawCircle(h,k,radius,paint);
                }
                else{
                    float width = paint.getStrokeWidth();
                    paint.setStrokeWidth(5.0f);
                    paint.setColor(Color.CYAN);
                    Pair<Float,Float> pf = f.GetClosestPoint(0, 0);
                    h = pf.first;
                    k = pf.second;
                    radius = 2.5f;
                    //canvas.drawCircle(h,k,radius,paint);
                    canvas.drawPoint(h, k, paint);
                    paint.setStrokeWidth(width);
                    paint.setColor(Color.BLACK);
                }


            }

       // }
    }


    public GPathShape(float startx, float starty, float endx, float endy,Path p,float width, float height){
        this(startx,starty,endx,endy,p,width,height,false);
    }

    @Override
    public Formula[] GetGoldenPoints(){

        return  line.GetAllPoints();
    }




}
