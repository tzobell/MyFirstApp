package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

import java.util.Arrays;
import java.util.Vector;


public class PolyShape extends ShapeDraw {
    //number of points in the polygon
    private final int shapepoints;

    private Vector<Pair<Pair<Float,Float>,Pair<Float,Float>>> paths;
    private  PolyShapeFormula gshape;

    protected PolyShape(PolyShapeFormula psf,  float stdWidth, float stdHeight ){
        super(stdWidth,stdHeight);
        gshape = psf;
        formula = gshape;
        Vector<Pair<Float,Float>> points = new Vector<>(Arrays.asList(gshape.GetKeyPoints()));
        shapepoints = points.size();
        paths = new Vector<>(shapepoints);

        for (int i = 0; i < shapepoints - 1; ++i) {
            paths.add( new Pair<>(new Pair<>(points.get(i).first, points.get(i).second), new Pair<>(points.get(i+1).first, points.get(i+1).second)));
        }
        //connect last point to first point
        paths.add(new Pair<>(new Pair<>(points.get(shapepoints - 1).first, points.get(shapepoints - 1).second), new Pair<>(points.get(0).first, points.get(0).second)));

    }
    public PolyShape(int polygonPoints, float startx,float starty,float endx, float endy, float stdWidth, float stdHeight) {
        this(new PolyShapeFormula(startx, starty, endx, endy, polygonPoints),stdWidth,stdHeight);
        //super(stdWidth, stdHeight);
       /* gshape = new PolyShapeFormula(startx, starty, endx, endy, polygonPoints);
        formula = gshape;
        points = new Vector<>(Arrays.asList(gshape.GetKeyPoints()));
        shapepoints = points.size();
        paths = new Vector<>(shapepoints);

        for (int i = 0; i < shapepoints - 1; ++i) {
            paths.add( new Pair<Pair<Float, Float>, Pair<Float, Float>>(new Pair<>(points.get(i).first, points.get(i).second), new Pair<>(points.get(i+1).first, points.get(i+1).second)));
        }
        //connect last point to first point
        paths.add(new Pair<Pair<Float, Float>, Pair<Float, Float>>(new Pair(points.get(shapepoints - 1).first, points.get(shapepoints - 1).second), new Pair(points.get(0).first, points.get(0).second)));
   */
    }




    @Override
    public void draw(Canvas canvas, Paint paint) {
        try {
            canvas.save();
            canvas.scale(mScaleX, mScaleY);

                for (int i = 0; i < shapepoints; ++i) {
                    canvas.drawLine(paths.get(i).first.first, paths.get(i).first.second, paths.get(i).second.first, paths.get(i).second.second, paint);
                }






            if(drawCircumCircle){
                CircleFormula outcircle = gshape.GetCircumCircle();
                CircleFormula incircle = gshape.getIncircle();
                canvas.drawCircle(outcircle.h,outcircle.k,outcircle.radius,paint);
                canvas.drawCircle(incircle.h,incircle.k,incircle.radius,paint);


                drawCircumCircle = false;

            }
            if(drawGoldenPoints){


                float[] gpts = gshape.getPoints();
                float width = paint.getStrokeWidth();

                paint.setStrokeWidth((float)gshape.getDiamater()/100);
                canvas.drawPoints(gpts, paint);
                int paintcolor = paint.getColor();
                paint.setColor(Color.CYAN);
                canvas.drawPoint(gshape.cx,gshape.cy,paint);
                paint.setColor(Color.GREEN);

                paint.setStrokeWidth(width);
                paint.setColor(paintcolor);

                drawGoldenPoints = false;
            }
            canvas.restore();
        }
        catch(Exception e){
            int abc = 123;
        }
    }




    @Override
    public ShapeDraw clone()  {
        PolyShape shape = new PolyShape(new PolyShapeFormula(gshape.startx,gshape.endx,gshape.endx,gshape.endy,shapepoints),sWidth,sHeight);

        shape = (PolyShape)clone(shape);
        return shape;
    }
}

