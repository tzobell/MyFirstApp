package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 class responsible for drawing a LineFormula
 */
public class Line extends ShapeDraw {

    LineFormula line;
    boolean drawGoldenPoints = false;

    Path path;
    //constructor that initializes line and path
    //if main is true, then line will only have 3 golden points, else it will have 9 golden points
    public Line(float startx, float starty, float endx, float endy,Path p,float width, float height, boolean main){
        super(width,height);
        path = p;
        line = new LineFormula(startx,starty,endx,endy,Side.above,main);
        p = new Path();
        p.moveTo(startx, starty);
        p.lineTo(endx, endy);
        formula = line;
    }
    public Line(float startx, float starty, float endx, float endy,Path p,float width, float height){
        this(startx,starty,endx,endy,p,width,height,false);
    }

    //draw path that represents the LineFormula line
    //if drawGoldenPoints is true then draw the golden points for line
    @Override
    public void draw(Canvas canvas, Paint paint) {

        try {
            canvas.drawPath(path, paint);
            if (drawGoldenPoints) {
                PointFormula[] gpts = line.GetAllPoints();
                float width = paint.getStrokeWidth();
                paint.setStrokeWidth((float) Maths.GetDistance(line.startx, line.starty, line.endx, line.endy) / 100);
                for (int i = 0; i < gpts.length; ++i) {
                    canvas.drawPoint(gpts[i].x, gpts[i].y, paint);
                }
                drawGoldenPoints = false;
                paint.setStrokeWidth(width);
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    @Override
    public Formula[] GetGoldenPoints(){

        return  line.GetAllPoints();
    }

}
