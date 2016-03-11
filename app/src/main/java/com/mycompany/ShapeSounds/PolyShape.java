package com.mycompany.ShapeSounds;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

import java.util.Arrays;
import java.util.Vector;

/*class handles the drawing of a PolyShapeFormula*/
public class PolyShape extends ShapeDraw {

    private final int shapepoints;//number of points in the polygon

    private Vector<Pair<Pair<Float,Float>,Pair<Float,Float>>> paths;//contains pairs of points to draw lines with
    private  PolyShapeFormula gshape;

    //constructor that initilizes formula, gshape,shapepoints, and vector  paths
    protected PolyShape(PolyShapeFormula psf,  float stdWidth, float stdHeight ){
        super(stdWidth,stdHeight);
        gshape = psf;
        formula = gshape;
        Vector<Pair<Float,Float>> points = new Vector<>(Arrays.asList(gshape.GetKeyPoints()));
        shapepoints = points.size();
        paths = new Vector<>(shapepoints);

        for (int i = 0; i < shapepoints - 1; ++i) {
            paths.add( new Pair<>(new Pair<Float,Float>(points.get(i).first, points.get(i).second), new Pair<Float,Float>(points.get(i+1).first, points.get(i+1).second)));
        }
        //connect last point to first point
        paths.add(new Pair<>(new Pair<Float,Float>(points.get(shapepoints - 1).first, points.get(shapepoints - 1).second), new Pair<Float,Float>(points.get(0).first, points.get(0).second)));

    }
    //pass new PolyShapeFormula with polygonPoints number of points starting at (startx,starty) and ending at (endx,endy) to main constructor
    public PolyShape(int polygonPoints, float startx,float starty,float endx, float endy, float stdWidth, float stdHeight) {
        this(new PolyShapeFormula(startx, starty, endx, endy, polygonPoints),stdWidth,stdHeight);

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        try {
            int color = paint.getColor();
            int b = Color.BLACK;
            canvas.save();
            //draw lines of the perimeter of the polygon
            canvas.scale(mScaleX, mScaleY);
                for (int i = 0; i < shapepoints; ++i) {
                    canvas.drawLine(paths.get(i).first.first, paths.get(i).first.second, paths.get(i).second.first, paths.get(i).second.second, paint);
                }
            //if drawCircumCircle ==true, then draw the circumcircle of the polygon
            if(drawCircumCircle){
                CircleFormula outcircle = gshape.GetCircumCircle();
                CircleFormula incircle = gshape.GetInCircle();

                canvas.drawCircle(outcircle.h,outcircle.k,outcircle.radius,paint);
                canvas.drawCircle(incircle.h,incircle.k,incircle.radius,paint);
                drawCircumCircle = false;
            }
            //if drawGoldenPoints == true then draw the golden points of the polygon
            if(drawGoldenPoints) {

                Vector<LineFormula> connectingLines = gshape.connectingLines;
                for (LineFormula lf:connectingLines) {
                    canvas.drawLine(lf.startx,lf.starty,lf.endx,lf.endy, paint);
                }

                float[] gpts = gshape.getPoints();
                float width = paint.getStrokeWidth();
                paint.setStrokeWidth((float) gshape.getDiamater() / 100);
                canvas.drawPoints(gpts, paint);
                int paintcolor = paint.getColor();
                paint.setColor(Color.CYAN);
                canvas.drawPoint(gshape.cx, gshape.cy, paint);
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

