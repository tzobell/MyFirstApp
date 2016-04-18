package com.mycompany.ShapeSounds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;


import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


import java.util.Vector;


//handles the drawing of shapes on the canvas
public class CustomDrawableView extends View {
    private GoldenShapeDrawable  mDrawable;
    private ScaleGestureDetector detector;
    private GestureDetectorCompat mGestureDetector;
    private Paint mBitmapPaint;
    Matrix drawMatrix;
    Canvas can = null;
    Bitmap bm = null;
    Bitmap previousBm = null;
    Bitmap drawingBm = null;
    Path p;
    ShapeFormula startShape = null;
    ShapeFormula endShape = null;
    ShapeFormula previousEndShape = null;
    private Vector<ShapeFormula> shapes;
    private Vector<ShapeSummary>shapeHistory;
    private Vector<MotionEvent> motionevents;
    Pair<Float,ShapeFormula> intersect =null;
    Vector<ShapeFormula> intersectShapes;
    Vector<ShapeFormula> intersectThisShape;
    Vector<ShapeFormula> intersectPrevShapes;
    double thresholdDistance;
    double initialthresholdDistance;
    private float scaleFactor = 1.f;
    private float startx;
    private float starty;
    private float endx;
    private float endy;
    private float width = 0;
    private float height;
    private float preX;
    private float preY;
    float lastFocusX;
    float lastFocusY;
    float focusX,focusY;
    float canvasWidth = 0;
    float canvasHeight = 0;
    boolean lastMotion = false;
    boolean undoing = false;
    private int playMotionEventsOffset = 0;
    ShapeType shape;
    boolean startset = false;
    boolean endset = false;
    boolean startInCircumCircle = false;
    boolean startInBounds = false;
    boolean playmotion = false;
    boolean zoom = false;
    boolean zoomMotion = false;
    int drawColor = Color.BLACK;
    int backGroundColor = Color.WHITE;
    boolean drawing = true;

    RectF currentRect;
   // float focusX,focusY;

    //constructor
    public CustomDrawableView(Context context) {
        super(context);
        init();


    }

    public CustomDrawableView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }

     private void init() {
         shapes = new Vector<>();
         intersectShapes = new Vector<>();
         intersectThisShape = new Vector<>();
         intersectPrevShapes = new Vector<>();
         detector = new ScaleGestureDetector(getContext(), new ScaleListener());
         mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);
         shape = ShapeType.triangle;
         endy = 0;
         endx = 0;
         startx = 0;
         starty = 0;
         width = 0;
         height = 0;
         mBitmapPaint = new Paint();//Paint.DITHER_FLAG
         mDrawable = new GoldenShapeDrawable(new PolyShape(1, 0, 0, 0, 0, 0, 0));
         mDrawable.getPaint().setColor(drawColor);
         mDrawable.setBounds(0, 0, 0, 0);
         motionevents = new Vector<>();
         shapeHistory = new Vector<>();
         thresholdDistance = 32;
         drawMatrix = new Matrix();
         initialthresholdDistance = 3;
         currentRect = new RectF(0,0,0,0);
     }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        try {
            super.onSizeChanged(w, h, oldw, oldh);
            bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvasHeight = h;
            canvasWidth = w;
            can = new Canvas(bm);
            backGroundColor = bm.getPixel(0, 0);
            drawingBm = bm;
            currentRect = new RectF(0,0,canvasWidth,canvasHeight);

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
            canvas.save();
          //  canvas.scale(scaleFactor, scaleFactor, this.focusX, this.focusY);

            //canvas.drawBitmap(bm, 0, 0, mBitmapPaint);
            //canvas.drawBitmap(bm, drawMatrix, mBitmapPaint);
            float[] mvals = new float[9];
            drawMatrix.getValues(mvals);
            canvas.translate(mvals[Matrix.MTRANS_X], mvals[Matrix.MTRANS_Y]);
            canvas.scale(mvals[Matrix.MSCALE_X], mvals[Matrix.MSCALE_Y]);
            canvas.drawBitmap(bm, 0, 0, mBitmapPaint);

            mDrawable.draw(canvas);
            canvas.restore();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //undo the last shape drawn
    // remove references to the last shape drawn and redraw without the last shape drawn.
    public void Undo() {
        try {
            ShapeType curShape = shape;
            if (shapeHistory.size() > 0) {
                undoing = true;
                Clear();

                ShapeSummary ls = shapeHistory.get(shapeHistory.size() - 1);
                Formula f = ls.sf;
                if (f instanceof ShapeFormula) {                    
                    Vector<ShapeFormula> associated = ls.GetAssociatedShapes();
                    for (ShapeFormula shapeformula : associated) {
                        shapeformula.RemoveShape((ShapeFormula) ls.sf);
                    }
                    int size = shapes.size();
                    for (int i = 0; i < size; ++i) {
                        if (shapes.get(i) == ls.sf) {
                            shapes.remove(i);
                            i = size;
                        }
                    }
                    //for (ShapeFormula s : sf.GetInsideShapes()) {
                     //   AddShape(s);
                   // }
                }
            }
            shapeHistory.remove(shapeHistory.size() - 1);
            for (int i = 0; i < shapeHistory.size(); ++i) {
                ShapeSummary ss = shapeHistory.get(i);
                shape = ss.shape;
                setStart(ss.startx, ss.starty);
                setEnd(ss.endx, ss.endy);
                //mDrawable.draw(can);
                draw();
                mDrawable.gs.DrawCircumCircle(true);
                invalidate();
                newCanvas(false);
            }
            shape = curShape;
        }

        catch(Exception e){
            System.out.println(e.getMessage());
        }
        try {
            startset = false;
            endset = false;
            startShape = null;
            zoom = false;
           // previousBm = bm.copy(Bitmap.Config.ARGB_8888, true);
            undoing = false;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }


    }
    //reset the canvas and other variables and objects to start over
    public void Clear(){
        try {
            if(!playmotion){
                motionevents.clear();
            }
            bm = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
            can = new Canvas(bm);

           if(!undoing){
               shapes.clear();
               shapeHistory.clear();
               scaleFactor = 1f;
               drawMatrix = new Matrix();
               currentRect = new RectF(0,0,canvasWidth,canvasHeight);
           }
            startset = false;
            endset = false;
            startShape = null;
            startInCircumCircle = false;
            invalidate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //play the sounds of the shapes
    public void QueUpForPlay(){
        CountDownTimer cd = null;
        try {
            for (ShapeFormula sf : shapes) {
                sf.Play();
            }
            //cd = Sound.playAll();
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        //return cd;
    }

    //if f is an instance of ShapeFormula then try adding f to a shapeFormula in vector<ShapeFormula> shapes.
    //if f cannot be added to any of the ShapeFormula's in shapes then add f to shapes.
    void AddShape(Formula f){
        try{
        
        if(f instanceof ShapeFormula){
            ShapeFormula insideShape = null;
            ShapeFormula sf =(ShapeFormula)f;
            boolean added = false;
            if(startShape!=null ){
                sf.AddConnectedShape(startShape);
                startShape.AddConnectedShape(sf);
            }
            for(ShapeFormula sfi:intersectShapes){
                sf.AddConnectedShape(sfi);
                sfi.AddConnectedShape(sf);
            }
            for(int i = 0; i < shapes.size(); ++i){
                Pair<Boolean,ShapeFormula> bs = shapes.get(i).AddShape(sf);
                if(bs.first ){
                    added = true;
                    i = shapes.size();
                    insideShape = bs.second;
                }
            }
            if(!added){
                shapes.add(sf);
            }
            if(!undoing){
                shapeHistory.add(new ShapeFormulaSummary(startx,starty,endx,endy,shape,sf,insideShape,startShape));
            }
        }
        else {
            if (!undoing) {
                shapeHistory.add(new ShapeSummary(startx, starty, endx, endy, shape, f, null, startShape));
            }
        }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    //sets newcanvas to true to indicate to save canvas nex time onDraw is called.
    //Calls AddShape if addShape == true
    // draws shape in mDrawable onto Canvas Can then resets mDrawable for the next shape to be drawn
    void newCanvas(boolean addShape){
        try {
            Formula f = mDrawable.GetGoldenShape().GetFormula();
            if(!undoing && addShape) {
                AddShape(f);
            }
            ShapeType st = shape;
            mDrawable.draw(can);
            can.save();
            shape = ShapeType.circle;
            shape = st;
            setStart(0, 0);
            setEnd(0, 0);
            getnewshape();
            mDrawable.getPaint().setColor(drawColor);
            mDrawable.setBounds(0, 0, 0, 0);


        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    void newCanvas(){
        newCanvas(true);
    }


    //calls getnewshape() to initilize mDrawable to the correct shape
    //then sets the color,style and bounds for mDrawable
    public void draw(int color){
        try {
            getnewshape();
            mDrawable.getPaint().setColor(color);
            mDrawable.getPaint().setStyle(Paint.Style.STROKE);
            mDrawable.setFBounds(0f, 0f, width, height);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void draw(){
        draw(drawColor);

    }

    //initilizes mDrawable with the shape and shape demensions
    private void getnewshape(){
        try {
            if(shape == ShapeType.circle) {
                mDrawable = new GoldenShapeDrawable(new CircleShape(startx, starty, endx, endy,width,height));
            }
            else {
                if (shape == ShapeType.line) {
                    p = new Path();
                    p.moveTo(startx, starty);
                    p.lineTo(endx, endy);
                    mDrawable = new GoldenShapeDrawable(new Line(startx, starty, endx, endy, p, width, height, false));
                }
                else {
                    int polyPoints = ShapeType.GetNumberOfPoints(shape);
                    mDrawable = new GoldenShapeDrawable(new PolyShape(polyPoints, startx, starty, endx, endy, width, height));
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    //sets the starting points of the current shape being drawn and sets startset to true if it is currently false.
    //and recalculates the width and height if the endset is true;
    public void setStart(float X, float Y){
        if(!startset){
            startset = true;
        }
        startx = X;
        starty = Y;
        if(endset){
            SetWidthAndHeight();
        }
    }

    public void setStart(Pair<Float,Float> start){
        setStart(start.first, start.second);
    }

    //sets the ending points of the current shape being drawn and sets endset to true if it is currently false, and sets the width and height of the shape
    public void setEnd(float X, float Y){
        try {
            if(!endset){
                endset = true;
            }
             endx = X;
            endy = Y;
            SetWidthAndHeight();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void setEnd(Pair<Float,Float> end) {
        setEnd(end.first, end.second);
    }

    //calculates the width and height for the current shape
    public void SetWidthAndHeight(){
        width = Math.abs(endx - startx);
        height = Math.abs(endy - starty);
    }

    //return Formula of type st with starting and ending points (xstart,ystart),(xend,yend)
    private Formula CreateFormula(float xstart,float ystart,float xend,float yend,ShapeType st ){
        Formula sh = null ;
        try {
            if(st == ShapeType.circle) {
              sh= new CircleFormula(xstart, ystart, xend, yend,true);
            }
            else {
                if (st == ShapeType.line) {
                    p = new Path();
                    p.moveTo(startx, starty);
                    p.lineTo(endx, endy);
                    sh = new LineFormula(xstart, ystart, xend, yend);
                }
                else {
                    int polyPoints = ShapeType.GetNumberOfPoints(shape);
                   sh =new PolyShapeFormula(xstart, ystart, xend, yend,polyPoints);
                }
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return sh;
    }

    //return shapeFormula of ShapeType st starting st point (xstart,ystart) and ending at (xend,yend)
    //if st is line then will return circle
    private ShapeFormula CreateShape(float xstart,float ystart,float xend,float yend,ShapeType st){
        if(st == ShapeType.line){
            st = ShapeType.circle;
        }
        return (ShapeFormula)CreateFormula(xstart,ystart,xend,yend,st);
    }

    public void SetShape(ShapeType st){
        shape = st;
    }

    //find if the thisshape circumcircle intersect test circumcircle.
    //if thisshape circle is within the threshold distance of test circle then see if the prevShape circumcircle is with in initialthresholdDistance of the test circumcircle.
    // if the prevshape circumcircle is closer return endx,endy with the distance, else return x,y with the distance
    //if thisshape it is not with in the threshold return x,y with positive infinity as the distance
    private Pair<Double,Pair<Float,Float>> CirclesIntersect(float x, float y,ShapeFormula thisshape, ShapeFormula prevShape, ShapeFormula test) {
    double distance = Double.POSITIVE_INFINITY;
    Pair<Float,Float> closest = new Pair<>(x, y);
    try {
        if (thisshape != null && endset && !test.GetCircumCircle().equals(startShape.GetCircumCircle()) ) {
            float circleDistance = (float) Maths.CircleDistance(thisshape.GetCircumCircle(), test.GetCircumCircle());
            float predis = (float)Maths.CircleDistance(prevShape.GetCircumCircle(), test.GetCircumCircle());
            if(circleDistance <= 0){
                intersectThisShape.add(test);
            }
            if(predis <=0){
                intersectPrevShapes.add(test);
            }
            if ( Math.abs(circleDistance) <= thresholdDistance) {
                if (intersect == null || intersect.first > Math.abs(circleDistance)) {
                    if (Math.abs(predis) <= initialthresholdDistance) {
                        intersect = Math.abs(predis) < Math.abs(circleDistance) ? new Pair<>(Math.abs(predis), test) : new Pair<>(Math.abs(circleDistance), test);
                        distance = Math.abs(predis) < Math.abs(circleDistance) ? predis : Math.abs(circleDistance);
                        closest = Math.abs(predis) < Math.abs(circleDistance) ? new Pair<>(endx, endy) : new Pair<>(x, y);
                    }
                }
            }
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return new Pair<>(distance, closest);
}

    //finds the closest point to (x,Y) out shapes
    private Pair<ShapeFormula,Pair<Float,Float>> FindClosest(float x, float y){
        intersectThisShape.clear();
        intersectPrevShapes.clear();
        intersectShapes.clear();
        Pair<Float,Float> closest = new Pair<>(x,y);//closest point to x,y
        Pair<Float,Float> closestInBounds = null;//closest point to x,y thats inside a shape
        Pair<Float,Float> p = new Pair<>(x,y);
        ShapeFormula closestShape = null;
        ShapeFormula closetInboundShape = null;//shape that closestInBounds is on/in
        intersect=null;
        try {
            //ShapeFormula thisshape = CreateShape(startx, starty, x, y, shape);
          //  ShapeFormula prevShape = endset?CreateShape(startx, starty, endx, endy, shape):null;
            double distance = Double.POSITIVE_INFINITY;
            //go through each shape, find the closest point in each shape and keep track of the closest one so far
            for (int i = 0; i < shapes.size(); ++i) {
                Pair<Float,Float> c = startset ? shapes.get(i).GetClosestPoint(startx, starty, x, y) : shapes.get(i).GetBasicClosestPoint(x, y);

                double tempdis = Maths.GetDistance(p, c);
                if (tempdis < distance) {
                    closest = c;
                    distance = tempdis;
                    closestShape = shapes.get(i);
                    if(closestShape.inBounds(c)){
                        closestInBounds = c;
                        closetInboundShape = closestShape;
                    }
                    if (!startset) {
                        startShape = shapes.get(i);
                    }
                }
                /*Pair<Double,Pair<Float,Float>> cintersect = CirclesIntersect(x,y,thisshape,prevShape,shapes.get(i));
                if(cintersect!=null && distance > cintersect.first){
                    distance = cintersect.first;
                    closest = cintersect.second;
                }*/

            }

            if(!startset && closestInBounds!= null && closetInboundShape!= null && closetInboundShape.inBounds(x,y)){
                closest = closestInBounds;
                startShape = closetInboundShape;
            }
            //if(intersect!=null &&distance < intersect.first){
                intersect = null;
           // }
            intersectShapes= closest.first == endx && closest.second == endy?intersectPrevShapes:intersectThisShape;

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return new Pair<>(closestShape, closest);
    }

    //adjust the starting and ending points so that they allign with eachother inside of a shapes circumcircle
    private Pair<Float,Float> AdjustPointsInside(float closestX,float closestY,float x, float y, ShapeFormula inShape) {
        LineFormula startToEnd = new LineFormula(startx, starty, x, y);
        //want to see if the line intersects the startshape to determine what to set the closest to
        //if the closest point(closestX,closestY) is the same as (startx,starty) then we need to use the x,y values passed to this method
        //to determine if the the line intersects the startshape
        boolean cross = false;

        Pair<Float,Float> closest = new Pair<>(x, y);
        try {
            boolean same = (startx == closestX && starty == closestY);
            if (!same) {
                cross = startShape.doesLineCross(startToEnd);
            }
            Pair<Float, Float> closestStart;
            boolean startInB = startInCircumCircle && startShape.inBounds(startx, starty);
            boolean endInBounds = startShape.inBounds(x, y);
            Pair<Float, Float> closestE = endInBounds ? new Pair<>(closestX, closestY) : inShape.GetClosestToPerimeter(closestX, closestY);
            Pair<Float, Float> closestS = startInB ? new Pair<>(startx, starty) : startShape.GetClosestToPerimeter(startx, starty);

            if (cross) {
                startToEnd = new LineFormula(closestS.first, closestS.second, closestE.first, closestE.second);
                closest = startToEnd.GetClosestValue(x, y, false);
            } else {
                Pair<Boolean, LineFormula> blf = inShape.isKeyPoint(closestE.first, closestE.second, startx, starty);
                if (blf.first) {
                    // closestX = closestp.first;
                    //closestY = closestp.second;
                    Pair<Float, Float> closestToLine = blf.second.GetClosestValue(startx, starty, false);
                    closest = closestE;
                    setStart(closestToLine);
                } else {
                    if (blf.second != null) {
                        closest = new Pair<>(closestX, closestY);
                        LineFormula perpLine = blf.second.GetPerpindicular(closestX, closestY);
                        closestStart = perpLine.GetClosestValue(startx, starty, false);
                        closestStart = inShape.GetClosestToCircumCircle(closestStart);
                        setStart(closestStart);
                    }
                }

            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return closest;
    }

    //adjust the starting and ending points so that they allign with eachother outisde of other shapes
    private Pair<Float,Float> AdjustPointsOutside(float closestX,float closestY,float x, float y) {
        LineFormula startToEnd = new LineFormula(startx, starty, x, y);
        //want to see if the line intersects the startshape to determine what to set the closest to
        //if the closest point(closestX,closestY) is the same as (startx,starty) then we need to use the x,y values passed to this method
        //to determine if the the line intersects the startshape
        boolean cross = false;

        Pair<Float,Float> closest = new Pair<>(x, y);
        try {
            boolean same = (startx == closestX && starty == closestY);
            Pair<Float, Float> closestStart;
            if (!same) {
                cross = startShape.doesLineCross(startToEnd);
            }
            //the point is not with in the circumcircle of an already exsisting shape
            if (cross) {
                closest = startShape.GetClosestToCircumCircle(x, y, false);
                startToEnd = new LineFormula(x, y, closest.first, closest.second);

                closestStart = startToEnd.GetClosestValue(startx, starty, false);
                closestStart = startShape.GetClosestToCircumCircle(closestStart.first, closestStart.second);
                setStart(closestStart);
            } else {
                closestStart = startShape.GetClosestToCircumCircle(x, y, false);
                LineFormula startlf = new LineFormula(closestStart.first, closestStart.second, x, y);
                closestStart = startlf.GetClosestValue(startx, starty, false);
                closestStart = startShape.GetClosestToCircumCircle(closestStart);
                setStart(closestStart);
            }

            if (shapes.size() > 0) {

                closest = AdjustPointToDiatonicRatio(x, y);
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return closest;
    }

    //if drawing concentric circles we want (diamater of circleA)/(diamater of circleB) == frequency of a note or (diamater of circleB)/(diamater of circleA) == frequency of a note
    //Determine the current diamater of the circumcircle for the shape being drawn, call this D1
    //Next,use the diamater of the shape that (startx,starty) starts at or find the shape with a circumcircle diamater that is closest to d1. Call this diamater D2.
    //Determine the frequency, if D2 >= D1 then frequency = D2/D1 else frequency = D1/D2
    //Find the note frequency closest to the frequency call this noteFreq
    // Then find the point along the line segment (startx,starty),(endx,endy) closest to (endx,endy) that will give a
    //diamater to the circle or circumcircle currently being drawn such that newDiamater/D2 == noteFreq or D2/newDiamater= noteFreq where the larger of (newDiamater,D2) is the numeratord
    Pair<Float,Float> AdjustPointToDiatonicRatio(float x, float y){
        Pair<Float,Float> closest = new Pair<>(x,y);
        try {
            if (shapes.size() > 0) {
                LineFormula lf = new LineFormula(startx, starty, x, y);
                double diamater = Maths.GetDistance(startx, starty, x, y);
                double shapeDiamater = startShape != null ? startShape.getDiamater() : diamater;
                double freq = shapeDiamater > diamater ? shapeDiamater / diamater : diamater / shapeDiamater;
                Pair<Notes, Octave> noteoct = DiatonicScale.findOctave(freq);
                double notefreq = DiatonicScale.getNoteFrequency(noteoct);
                double newDiamater = shapeDiamater / notefreq;
                if (shapeDiamater <= diamater) {
                    newDiamater = shapeDiamater * notefreq;
                }
                closest = lf.findDistantPoint(newDiamater);
                Pair<Float, Float> samesize = lf.findDistantPoint(startShape.getDiamater());
                double distance = Maths.GetDistance(x, y, closest);
                if (Maths.GetDistance(x, y, samesize) <= distance) {
                    closest = samesize;
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return closest;
    }

    //return the shape that (closestX,closestY) lies the most in, and set startShape and startinShape as needed based on senario.
    //inaCircumCircle informs wither the point used to determine (closestX,closestY) is inside a shape or it's circumcircle
    //if inaCircumCircle is true then inShape is the circumcircle of the shape that (closestX,closestY) is inside of.
    private ShapeFormula DetermineInShape(boolean inaCircumCircle, ShapeFormula inShape, float closestX,float closestY){
        try {
            int senario = startset && !inaCircumCircle ? 0 :
                    !startset && !inaCircumCircle ? 1 :
                            startset ? 2 : 3;

            switch (senario) {
                //startset && !inaCircumCircle
                case 0:
                    break;
                //!startset || inaCircumCircle
                case 1:
                case 2:
                case 3:
                    switch (senario) {
                        // !startset && !inaCircumCircle
                        case 1:
                            //if starting points have not been set and (x,y) is not in a shape, then the starting points are not in a shape
                            startInCircumCircle = false;
                            break;
                        //inaCircumCircle
                        case 2:
                        case 3:
                            if (inShape != null) {
                                //find smallest shape that the point (closestX,closestY) is inside of, in case there are shapes within the inshape.
                                ShapeFormula circumShape = inShape.FindCircumShape(closestX, closestY);
                                inShape = circumShape != null ? circumShape : inShape;
                                switch (senario) {
                                    // startset && inaCircumCircle
                                    case 2:
                                        endShape = inShape;
                                        break;
                                    // !startset && inaCircumCircle
                                    case 3:
                                        startInCircumCircle = true;
                                        startShape = inShape;
                                        break;
                                }
                            }
                            break;
                    }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return inShape;
    }

    //use x,y to determine the Pair<Float,Float> point to return
    //first fine the closest point in the shapes to (x,y)
    //if the closest point is one that intersects the circumcircle of another point, or the starting point has not been set, or the startShape is null then return the closest point found
    //
    private Pair<Float,Float> DeterminePoint(float x, float y){
        float closestX;
        float closestY;
        boolean inaCircumCircle;
        ShapeFormula inShape;
        Pair<Float,Float> closest = new Pair<>(x,y);
        previousEndShape = endShape;
        endShape = null;
        try {
            Pair<ShapeFormula,Pair<Float,Float>> closestinfo = FindClosest(x,y);
            closestX = closestinfo.second.first;
            closestY = closestinfo.second.second;
            closest = new Pair<>(closestX,closestY);
            boolean endInBounds = startShape!=null && startShape.inBounds(x, y);
            if(!startset){
                startInBounds = closestinfo.first!=null &&closestinfo.first.inBounds(x,y);
            }
            else{
                if(intersect!=null && startShape!= null){
                    //set intersect to null, if the circle it intersects is inside of the startshape and both the starting and endingpoints of the current shape are outside of the startshape
                    if(!(!startInBounds  && !endInBounds && !startShape.GetCircumCircle().inBounds(intersect.second.GetCircumCircle()) )){
                        intersect = null;
                    }
                }
            }
            if(intersect==null &&startset &&startShape!=null){
                inaCircumCircle = closestinfo.first != null && closestinfo.first.inCircumCircle(x, y);
                inShape = DetermineInShape(inaCircumCircle, closestinfo.first, closestX, closestY);
                closest = new Pair<>(closestX, closestY);
                if (startset && startShape != null) {
                    boolean sameShape = inaCircumCircle && (startShape == inShape);
                   // boolean startInBounds = startInCircumCircle && startShape.inBounds(startx, starty);
                    if (!startInBounds && !endInBounds) {
                        //the point is not with in the circumcircle of an already exsisting shape
                        if (!inaCircumCircle) {
                            closest = AdjustPointsOutside(closestX, closestY, x, y);
                        } else {
                            //the shape that the startpoint is on/in and the shape the current point is on/in is the same
                            if (sameShape) {
                                closest = AdjustPointsInside(closestX, closestY, x, y, inShape);
                            }
                        }
                    }
                    else{
                        if(endInBounds){
                            endShape = inShape;
                        }
                        else{
                            if(!startShape.inCircumCircle(x,y)&&endShape == null){
                                closest = AdjustPointToDiatonicRatio(x,y);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return closest;
    }

    //replay motions minus the last motion
    public void OneLessMotion(){
        playMotionEventsOffset = (playMotionEventsOffset + motionevents.size()) >0?playMotionEventsOffset-1:playMotionEventsOffset;
        PlayMotions();
    }

    //add one to play motionEventsOffset if it is less than 0
    public void OneMoreMotion(){
        playMotionEventsOffset = playMotionEventsOffset< 0?playMotionEventsOffset+1:playMotionEventsOffset;
        PlayMotions();
    }

    //play the drawing motions
    public void PlayMotions(){
        try {
            if (motionevents.size() > 0) {
                playmotion = true;
                this.Clear();
                int mnum = motionevents.size() + playMotionEventsOffset;
                for (int i = 0; i < mnum; ++i) {
                    if (i == mnum - 1) {
                        lastMotion = true;
                    }
                    onTouchEvent(motionevents.get(i));
                    lastMotion = false;
                }
                playmotion = false;
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    //return the shapeType of Formula sf
    private ShapeType DetermineShape(Formula sf){
        ShapeType st = null;
        try {
            if (sf instanceof LineFormula) {
                st = ShapeType.line;
            } else {
                if (sf instanceof CircleFormula) {
                    st = ShapeType.circle;
                } else {
                    if (sf instanceof PolyShapeFormula) {
                        PolyShapeFormula psf = (PolyShapeFormula) sf;
                        st = ShapeType.GetShapeType(psf.points.length);

                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return st;
    }

    //draw the goldenpoints for formula F with int color
    private void DrawGoldenPoints(Formula f, int color){
        try {
            Pair<Float,Float> start = new Pair<>(startx, starty);
            Pair<Float,Float> end = new Pair<>(endx, endy);
            ShapeType st = shape;

            if (f != null) {
                shape = DetermineShape(f);
                setStart(f.GetStart());
                setEnd(f.GetEnd());
                if (!zoom) {
                    draw(color);
                }
                mDrawable.gs.DrawGoldenPoints(true);
                invalidate();
                newCanvas(false);
            }
            shape = st;
            setStart(start);
            setEnd(end);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void DrawGoldenPoints(Formula f){
        DrawGoldenPoints(f, drawColor);
    }
    private void UnDrawGoldenPoints() {
        try {
            bm = Bitmap.createBitmap((int)canvasWidth, (int)canvasHeight, Bitmap.Config.ARGB_8888);
            can.setBitmap(bm);
            can.drawBitmap(previousBm,0,0,mBitmapPaint);
            can.save();
            // DrawGoldenPoints(f, backGroundColor);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void Zoom(boolean zooming){
        zoom = zooming;

    }


    public void CanDraw(boolean canDraw){
        drawing = canDraw;
    }


    public Pair<Float,Float> GetRealValues(float eventX, float eventY){

        float[] mvals = new float[9];


        drawMatrix.getValues(mvals);
        Matrix m = this.getMatrix();
        Matrix inverse = new Matrix();

        drawMatrix.invert(inverse);
        float[] point = {eventX,eventY};
        inverse.mapPoints(point);
        //float realX = (eventX -mvals[Matrix.MTRANS_X] )/ mvals[Matrix.MSCALE_X];
        //float realY = (eventY -mvals[Matrix.MTRANS_Y] )/ mvals[Matrix.MSCALE_Y];
        // float realVal = focusVal > eventVal? focusVal + ((eventVal - focusVal) / scaleFactor): focusVal - (( focusVal - eventVal) / scaleFactor);
        return new Pair<>(point[0],point[1]);

    }


    @Override
    //reads motions and calls methods to set starting and ending points and to draw canvas depending on the motion
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if(event!=null) {

               /* float X = ((event.getX() - this.focusX) * scaleFactor) +  this.focusX;
                float Y = ((event.getY()- this.focusX) * scaleFactor) +  this.focusX;*/
                Pair<Float,Float> realvals = GetRealValues(event.getX(),event.getY());
                float X = realvals.first;//GetRealValue(event.getX(),focusX);//Vthis.focusX > event.getX()? this.focusX + ((event.getX() - this.focusX) / scaleFactor): this.focusX - (( this.focusX - event.getX()) / scaleFactor);
                float Y = realvals.second;//GetRealValue(event.getY(), focusY);//this.focusY > event.getY()? this.focusY + ((event.getY() - this.focusY) / scaleFactor): this.focusY - (( this.focusY - event.getY()) / scaleFactor);

                Pair<Float,Float> p;
                //if playmotion is false then add this motion event to the motionevents Vector
                if (!playmotion) {
                    motionevents.add(MotionEvent.obtain(event));
                }
                if(drawing) {
                    if (!zoom) {


                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            //first press on screen which means we are going to
                            //se the values for startx and starty
                            case MotionEvent.ACTION_DOWN:
                                preX = X;
                                preY = Y;
                                p = DeterminePoint(X, Y);
                                setStart(p.first, p.second);
                                break;
                            //finger on screen and moving so set the endx,endy values
                            case MotionEvent.ACTION_MOVE:
                                if (preX != X && preY != Y) {
                                    p = DeterminePoint(X, Y);
                                    setEnd(p.first, p.second);
                                    //if the point (endx,endy) is inside of a shape then draw the goldenpoints for that shape
                                    //if during the next ACTION_MOVE the endshape is the same as the previousEndshape then we don't need to do anthing sense all goldenpoints have
                                    //already been drawn or undrawn as needed
                                    //if the endshape is different from the previousEndShape then the previousEndshape goldenpoints need to be undrawn
                                    //and if the new values for (endx,endy) are in another shape then draw the golden points for that shape

                           /* if(endShape !=previousEndShape){
                                if(previousEndShape!=null){
                                    UnDrawGoldenPoints();
                                }
                                if(endShape!=null ){
                                    DrawGoldenPoints(endShape);
                                }
                            }*/
                                    if (!zoom) {
                                        draw();
                                    }
                                    //if the shape's circumcircle just barley intersects another shapes circumcircle, then draw the circumcircle for the shape currently being drawn.
                                    if (intersect != null) {
                                        mDrawable.gs.DrawCircumCircle(true);
                                    }
                                    invalidate();
                                }
                                break;

                            //second finger on screen means we are zooming in
                       /* case MotionEvent.ACTION_POINTER_DOWN:
                            zoom = true;
                            break;*/

                            //all fingers off of the screen, so now we will find and set the endpoint,finalize the shape that was drawn, and reset variables
                            case MotionEvent.ACTION_UP:
                                p = DeterminePoint(X, Y);
                                setEnd(p.first, p.second);
                                if (!zoom) {
                                    if (endShape != null) {
                                      //  UnDrawGoldenPoints();
                                    }
                                    draw();
                                    mDrawable.gs.DrawCircumCircle(true);
                                    //mDrawable.gs.DrawGoldenPoints(true);
                                    invalidate();
                                    newCanvas();
                                }
                                startset = false;
                                endset = false;
                                startShape = null;
                                invalidate();
                                zoom = false;
                                previousBm = bm.copy(Bitmap.Config.ARGB_8888, true);


                                break;

                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                        }


                    }
                    if (zoom) {

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_POINTER_DOWN:
                                zoomMotion = true;
                               break;

                            case MotionEvent.ACTION_POINTER_UP:
                                zoomMotion = false;

                        }
                        if(zoomMotion) {

                            detector.onTouchEvent(event);
                           // focusX = detector.getFocusX();
                           // focusY = detector.getFocusY();
                        }
                        else{
                            mGestureDetector.onTouchEvent(event);

                        }
                        invalidate();
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return true;

    }


    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private  float MIN_ZOOM = 1f;
        private  float MAX_ZOOM = 10f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastFocusX = detector.getFocusX();
            lastFocusY = detector.getFocusY();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Matrix transformationMatrix = new Matrix();
            focusX = detector.getFocusX();
            focusY = detector.getFocusY();
            try {
               transformationMatrix.postTranslate(-focusX, -focusY);
                scaleFactor *= detector.getScaleFactor();
                float tscale = detector.getScaleFactor();
                if(scaleFactor < MIN_ZOOM || scaleFactor > MAX_ZOOM){
                    float prescaleFactor = scaleFactor/tscale;
                    scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
                    tscale = scaleFactor/prescaleFactor;
                }
                transformationMatrix.postScale(tscale, tscale);
                transformationMatrix.postTranslate(focusX, focusY);
                drawMatrix.postConcat(transformationMatrix);
                if(scaleFactor == MIN_ZOOM){
                    drawMatrix = new Matrix();
                }
                correctMatrix();
            }
           catch(Exception e){
               System.out.println(e.getMessage());
           }
            return true;
        }
    }
    //sees if any of the the view would display outside of the canvas with the current transformation matrix and if so corrects the MTRANS_X and MTRANS_Y values as needed so that
    //the view only displays things with in the canvas area
    private void correctMatrix(){
        float[] mvals = new float[9];
        drawMatrix.getValues(mvals);
        float distanceX = 0;
        float distanceY = 0;
        float tdx = mvals[Matrix.MTRANS_X];
        float tdy = mvals[Matrix.MTRANS_Y];

        //x' = (x*scalefactor)+transition
        //so x = (x'-transition)/scalefactor
        //if x' <= width then x <= width
        //if (x'-transition)/scalefactor > width when x' = width
        //then correct so that distanx will make (width-transition)/scalefactor <= width
        if (((canvasWidth - tdx) / scaleFactor) > canvasWidth) {
            distanceX = tdx - (canvasWidth - (canvasWidth * scaleFactor));
        }

        //x' = (x*scalefactor)+transition
        //so x = (x'-transition)/scalefactor
        //if x' == 0 then x >=0
        //so (0-t)/s >= 0 => t/s <= 0
        //sense s is alays >=1 then t <=0
        //t is greater than 0, then set distanceX value such that transitionX value will be 0
        if ((tdx) > 0) {
            distanceX = mvals[Matrix.MTRANS_X];
        }


        //y' = (y*scalefactor)+transition
        //so y = (y'-transition)/scalefactor
        //if y' <= height then y <= height
        //if (y'-transition)/scalefactor > height when y' = height
        //then correct so that distany will make (height-transition)/scalefactor <= height
        if (((canvasHeight - tdy) / scaleFactor) > canvasHeight) {
            distanceY = tdy - (canvasHeight - (canvasHeight * scaleFactor));
        }

        //y' = (y*scalefactor)+transition
        //so y = (y'-transition)/scalefactor
        //if y' == 0 then y >=0
        //so (0-t)/s >= 0 => t/s <= 0
        //sense s is alays >=1 then t <=0
        //t is greater than 0, then set distancey value such that transitiony value will be 0
        if ((tdy) > 0) {
            distanceY = mvals[Matrix.MTRANS_Y];
        }
        drawMatrix.postTranslate(-distanceX, -distanceY);
    }

    private final GestureDetector.SimpleOnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (scaleFactor > 1) {
                try {
                    drawMatrix.postTranslate(-distanceX, -distanceY);
                    correctMatrix();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return true;
        }

    };


}