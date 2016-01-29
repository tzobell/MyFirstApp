package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.Pair;

import java.util.Vector;

/**
 handles the  drawing of a circle
 */
public class CircleShape extends ShapeDraw {
    CircleFormula cf;
    //constructor create circleFormula
    public CircleShape(float startx, float starty, float endx, float endy,float width,float height){
        super(width,height);
        cf = new CircleFormula(startx,starty,endx,endy,true);
        formula = cf;
    }
    //draws circle and goldenpoint if set to true
    @Override
    public void draw(Canvas canvas, Paint paint) {
        try {
            Paint tpaint = new Paint();
            tpaint.setStyle(Paint.Style.STROKE);
            tpaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
            canvas.drawCircle(cf.h, cf.k, cf.radius, paint);
            if (drawGoldenPoints) {
                Formula gpoints[] = GetGoldenPoints();
                for (Formula f : gpoints) {
                    float h, k, radius;
                    Pair<Float, Float> p = f.GetClosestPoint(0, 0);
                    float r = 2.5f;
                    if (f instanceof CircleFormula) {
                        CircleFormula cf = (CircleFormula) f;
                        h = cf.h;
                        k = cf.k;
                        radius = cf.radius;
                    } else {
                        Pair<Float, Float> pf = f.GetClosestPoint(0, 0);
                        h = pf.first;
                        k = pf.first;
                        radius = 2.5f;
                    }
                    canvas.drawCircle(h, k, radius, paint);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    @Override
    public ShapeDraw clone()  {
        CircleShape shape = new CircleShape(0f,0f,0f,0f,0f,0f);
        shape.cf = new CircleFormula(cf.h,cf.k, cf.radius);
        shape = (CircleShape)clone(shape);
        return shape;
    }

}
