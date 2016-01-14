package com.mycompany.myfirstapp;

import android.util.Pair;


import java.util.Arrays;
import java.util.Vector;


public class PolyShapeFormula implements ShapeFormula {

    float cy, cx,startx,starty,endx,endy,midx,midy;
    private final int shapepoints;
    Pair<Float, Float> points[];
    Vector<LineFormula> shapeLines;
    Vector<LineFormula> connectingLines;

    Vector<Formula> goldenP;
    Vector<ShapeFormula> inside;

    Vector<ShapeFormula> connectedShapes;
    Vector<LineFormula> centerConnecting;
    private CircleFormula circumcircle;
    private CircleFormula incircle;

    float gpoints[];
    Vector<Float> gpts;
    Vector<Pair<Float,Float>> pairgpts;
    private ShapeFormula parent;


private PolyShapeFormula(int polygonPoints,float startx,float starty,float endx,float endy){
    shapepoints = polygonPoints >= 3 ? polygonPoints : 3;
    this.startx = startx;
    this.starty = starty;
    this.endx = endx;
    this.endy = endy;
    midx = (endx + startx) / 2;
    midy = (endy + starty) / 2;
    inside = new Vector<>();
    parent = null;
    connectedShapes = new Vector<>();
    goldenP = new Vector<>();
    shapeLines = new Vector<>();
    gpts = new Vector<>();
    pairgpts = new Vector<>();
    connectingLines = new Vector<>();
    centerConnecting= new Vector<>();
    gpoints =null;// shapepoints%2==0?new float[shapepoints + (((shapepoints-1)*shapepoints))]:new float[(shapepoints + (((shapepoints-1)*shapepoints)))-1];
}
    //Initilizes values for other variables and fills in array and vector values
    private void Initilize(){
        try {
            if (points.length % 2 == 0) {
                cx = (startx + endx) / 2;
                cy = (starty + endy) / 2;
            } else {
                findCenter();
            }
            initArrays();
            float circumcircleRadius = (float) (getApothem() / (Math.cos(Math.PI / points.length)));
            double sl = getSideLength();
            float incircleRadius = (float) (sl / (2 * Math.tan(Math.PI / points.length)));
            circumcircle = new CircleFormula(cx, cy, circumcircleRadius,false);
            incircle = new CircleFormula(cx, cy, incircleRadius,false);
            //inside.add(incircle);
            goldenP.add(circumcircle);

        } catch (Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }
    }
    public PolyShapeFormula(float startx, float starty, float endx, float endy, Pair<Float, Float> points[]) {
        this(points.length,startx,starty,endx,endy);
        this.points = points;
        Initilize();
    }


    public PolyShapeFormula(float startx, float starty, float endx, float endy, int polygonPoints){
        this(polygonPoints,startx,starty,endx,endy);

         points = new Pair[shapepoints];
        DeterminePoints();
        Initilize();

    }
    private void DeterminePoints() {

        try {
            double startangle;
            //find the midpoints on the x and y axis
            double r;
            double pent;
            //find angle that polygon starts at.
            //adjusted x and y values where (0,0) = (midx,midy):
            //adjusted x = x-midx
            //adjusted y = midy-y
            //theta = arctan(adjusted y/adjusted x)
            //adjust angle as needed to reflect correct quadrant of x,y axis
            if ((startx - midx) < 0) {
                startangle = (Math.atan(((double) (midy - starty)) / ((double) (startx - midx)))) + Math.PI;
            }
            else {
                startangle = (Math.atan(((double) (midy - starty)) / ((double) (startx - midx))));
            }


            //divide 2pi(360 degrees) by the number of points in polygon
            pent = (2 * Math.PI) / shapepoints;
            r = DetermineRadius(startangle);

            //find points
            double angle = startangle;
            for (int i = 0; i < shapepoints; i++) {
                points[i] = getPoint(angle, r);
                angle += pent;
            }

        }
        catch (Exception e) {
            int abc = 123;
        }


    }

    private  Pair<Float,Float> getPoint(double angle, double r){
        double x = (Math.cos(angle) * r);
        x += midx;//turn adjusted x value into actual x value
        double y = (Math.sin(angle) * r);
        y = midy - y;//turn adjusted y value into actual y value
        return  new Pair<>( (float)x, (float)y);
    }

    public Pair<Float,Float> GetStart(){
        return new Pair<>(startx,starty);
    }
    public Pair<Float,Float> GetEnd(){
        return new Pair<>(endx,endy);
    }

    private  double DetermineRadius(double startangle){
        double r;
        //radius = square root((y^2)+(x^2))/2
        r = Maths.GetDistance(startx,starty,endx,endy) / 2;

        if(shapepoints%2 != 0) {
            try {
                //if odd number polygon we want bottom line to cross through endx,endy.
                double pent = (2 * Math.PI) / shapepoints;
                int i = ((shapepoints + 1) / 2) - 1;
                double angle = startangle + (pent * i);
                Pair<Float, Float> p1 = getPoint(angle, r);
                angle += pent;
                Pair<Float, Float> p2 = getPoint(angle, r);
                //mid point of bottom line using the current radius
                Pair<Float, Float> mid = new Pair<>(((p1.first + p2.first) / 2),  ((p1.second + p2.second) / 2));
                //height using the current radius
                double h = Maths.GetDistance(startx,starty,  mid.first, mid.second);
                //if current radius/current height == new radius / new height(or 2*current radius)
                // then new radius = (2 * (current radius)^2)/ current height
                //double newR = (2*r * r) / h;
                r = (2*r * r) / h;
                //find new midx and midy with the new radius
                double x = (Math.cos(startangle) * r);
                midx = (startx - (float) x);
                //x += midx;//turn adjusted x value into actual x value
                double y = (Math.sin(startangle) * r);
                midy = starty + (float) y;
            }
            catch (Exception e) {
                String a = e.getMessage();
                System.out.println(a);
            }

        }
        return r;
    }
    //public void SetParent(ShapeFormula sf){
    //    parent = sf;
   // }
    public double getDiamater(){
        return circumcircle.getDiamater();
    }
    public CircleFormula GetCircumCircle(){
        return circumcircle;
    }
    public CircleFormula getIncircle(){
        return incircle;
    }
    //find the end point of the polygon
    private Pair<Float, Float> getEnd() {
        Pair<Float, Float> d;
        int i = (points.length + 1) / 2;
        i -= 1;
        if (points.length % 2 != 0) {
            Pair<Float, Float> l = points[i];
            Pair<Float, Float> r = points[i + 1];
            d = new Pair<>(((l.first + r.first) / 2),  ((l.second + r.second) / 2));
        } else {
            d = points[i];
        }
        return d;
    }




    //method used to find the center of a regular polygon with an odd number of points
    private void findCenter() {
        try {
            if (points.length % 2 != 0) {
                Pair<Float, Float> d;
                double s = Maths.GetDistance(points[0], points[1]);
                double apothem = s / (2 * (Math.tan((Math.PI / points.length))));
                d = getEnd();
                Pair<Float, Float> distantPoint = Maths.findDistantPoint(d, points[0], apothem, true);
                cx = distantPoint.first;
                cy = distantPoint.second;
            }

        } catch (Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }
    }

    private int initArrays() {
        int abc = 123;
        abc+=1;
        try {

            int gsize = 0;
            String info = "";
            for (int i = 0; i < points.length; ++i) {
                LineFormula lf;
                Side side;

                gpts.add(points[i].first);
                gpts.add(points[i].second);
                goldenP.add(new PointFormula(points[i].first, points[i].second));
                if (i < points.length - 1) {
                    side = determineSide(points[i], points[i + 1]);
                    lf = new LineFormula(points[i].first, points[i].second, points[i + 1].first, points[i + 1].second, side);
                }
                else{
                    side = determineSide(points[points.length - 1], points[0]);
                    lf =  new LineFormula(points[i].first, points[i].second, points[0].first, points[0].second, side);
                }
                shapeLines.add(lf);
                PointFormula[] lfpf  = lf.GetGoldenPoints();
                goldenP.addAll(Arrays.asList(lfpf));
                for(int j = 0; j < lfpf.length; ++j){
                    gpts.add(lfpf[j].x);
                    gpts.add(lfpf[j].y);
                }
                         connectingLines.add(lf);
            }

            for (int i = 0; i < points.length - 1; ++i) {
                for (int j = i + 1; j < points.length; ++j) {
                    PointFormula igolden[] = shapeLines.get(i).GetGoldenPoints();
                    PointFormula jgolden[] = shapeLines.get(j).GetGoldenPoints();
                    int bi = igolden.length - 1;
                    int ai = 0;
                    for (PointFormula anIgolden : igolden) {
                        LineFormula lf = new LineFormula(anIgolden.x, anIgolden.y, jgolden[bi].x, jgolden[bi].y, true);
                        connectingLines.add(lf);
                        if(bi == jgolden.length/2 && ai == igolden.length/2){
                            centerConnecting.add(lf);
                        }
                        PointFormula[] lfpf  = lf.GetGoldenPoints();
                        goldenP.addAll(Arrays.asList(lfpf));
                        for(PointFormula pf:lfpf){
                            gpts.add(pf.x);
                            gpts.add(pf.y);
                        }
                        --bi;
                        ai++;

                    }
                }
            }


        } catch (Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }


        return abc;
    }



    //method to determine which side of the line the center of the polygon is at
    private Side determineSide(Pair<Float, Float> a, Pair<Float, Float> b) {
        Side side = Side.above;
        try {
            float x1 = a.first;
            float y1 = a.second;
            float x2 = b.first;
            float y2 = b.second;

            //if the lines of the start y value and end y value do not straddle the y value of the center then the center is either above or below the line
            if ((y1 >= cy && y2 >= cy) || (y1 < cy && y2 < cy)) {
                //if the y value is greater than the y value for the center point then that means the
                //line is below the center of the polygon and that the center is above the line
                if (y1 >= cy) {
                    side = Side.above;
                }
                //if the y value is less than the y value for the center point then that means the
                //line is above the center of the polygon and that the center is below the line
                else {
                    side = Side.below;
                }
            }
            //the lines start and end y values straddles the center points y value then the line is either on the left or right of the center
            else {
                if (((x1 + x2) / 2) <= cx) {
                    side = Side.right;
                } else {
                    side = Side.left;
                }
            }
        } catch (Exception e) {
            String c = e.getMessage();
            System.out.println(c);
        }

        return side;

    }


    public float[] getPoints(){

        if(gpoints == null){
            gpoints  = new float[gpts.size()];
            for(int i = 0; i < gpts.size(); ++i){
                gpoints[i] = gpts.get(i);
            }
        }

        return gpoints;
    }



    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> pstart,Pair<Float,Float> p ){
        return GetClosestToCircumCircle(pstart.first, pstart.second, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,Pair<Float,Float> p ){
        return GetClosestToCircumCircle(xstart, ystart, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> pstart,float x,float y ){
        return GetClosestToCircumCircle(pstart.first, pstart.second, x, y);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> p){
        return GetClosestToCircumCircle(p.first, p.second);
    }

    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y){
        return GetClosestToCircumCircle(x, y, true,true, xstart, ystart);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y){
        return GetClosestToCircumCircle(x,y,true,false, null,null);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y,boolean lockintoKeyPoints){
        return GetClosestToCircumCircle(x, y, lockintoKeyPoints,true, xstart, ystart);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y,boolean lockintoKeyPoints){
        return GetClosestToCircumCircle(x,y,lockintoKeyPoints,false, null,null);
    }
    //find the point that is the bestfit on the circumcircle
    private Pair<Float,Float> GetClosestToCircumCircle(float X, float Y, boolean lockKeyPoints, boolean startpoints, Float xstart,Float ystart) {
        Pair<Float, Float> point = new Pair<>(X, Y);
        if(lockKeyPoints) {
            double distance = Double.POSITIVE_INFINITY;
            for (Pair<Float, Float> p : points) {
                double tempdistance = Maths.GetDistance(p, X, Y);
                if (tempdistance < distance) {
                    point = p;
                    distance = tempdistance;
                }
            }

            point = comparePointToCircle(X, Y, point, circumcircle, startpoints, xstart, ystart);
        }
        else{
            point = startpoints?circumcircle.GetClosestPoint(xstart,ystart,X,Y,false):circumcircle.GetClosestPoint(X,Y,false);
        }

        return point;

    }
    public Pair<Float,Float> GetClosestToPerimeter(float x,float y){
        return GetClosestPoint(x, y, shapeLines);
    }
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,float x,float y){
        return GetClosestPoint(x, y, shapeLines);
    }
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> p){
        return GetClosestToPerimeter(p.first, p.second);
    }
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> pstart,float x,float y){
        return GetClosestToPerimeter(pstart.first, pstart.second, x, y);
    }
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,Pair<Float,Float> p){
        return GetClosestToPerimeter(xstart, ystart, p.first, p.second);
    }

    public Pair<Boolean,LineFormula> isKeyPoint(Pair<Float,Float> p){
        return isKeyPoint(p.first, p.second);
    }
    public Pair<Boolean,LineFormula> isKeyPoint(float x, float y){
        return isKeyPoint(x, y, false, null, null);
    }

    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y,float xend,float yend){

        return isKeyPoint(x,y,true,xend,yend);
    }
    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y,boolean end,Float xend,Float yend){

        Vector<LineFormula> keyPointLines = new Vector<>();
        boolean isKey = false;
        LineFormula line = null;
        double distance = Double.POSITIVE_INFINITY;
        for(LineFormula lf:shapeLines) {
            if (lf.isKeyPoint(x, y)) {
                if(!isKey){
                    keyPointLines.clear();
                }
                isKey = true;
                keyPointLines.add(lf);
                if (!end) {
                    break;
                }
            }
            else{
                if(!isKey){
                    double tempdis = Maths.GetDistance(lf.GetClosestValue(x,y),x,y);
                    int senario = tempdis < distance?0:tempdis==distance?1:2;
                    switch (senario){
                        case 0:
                            keyPointLines.clear();
                            distance = tempdis;
                        case 1:
                            keyPointLines.add(lf);
                            break;
                        case 2:
                            break;
                    }
                }
            }
        }

        if (!end) {
            line = keyPointLines.get(0);
        }
        else {
            distance = Double.POSITIVE_INFINITY;
            for (LineFormula lf : keyPointLines) {
                Pair<Float, Float> p = lf.GetClosestPoint(xend, yend);
                double tempDis = Maths.GetDistance(p, xend, yend);
                if (tempDis < distance) {
                    distance = tempDis;
                    line = lf;
                }

            }
        }

        return new Pair<>(isKey,line);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float X,float Y) {


        return GetClosestPoint(X, Y, true, xstart, ystart);
    }


    //test if point on circle is closer than one passed
    private Pair<Float,Float> comparePointToCircle(Float X,Float Y, Pair<Float,Float> point, CircleFormula testCircle, boolean startpoints,Float xstart,Float ystart){
        double distance = Maths.GetDistance(X, Y, point);
        Pair<Float, Float> tempPoint;
        tempPoint = startpoints?testCircle.GetClosestPoint(xstart, ystart, X, Y): testCircle.GetClosestPoint(X,Y);
        double tempdistance = Maths.GetDistance(X, Y, tempPoint);

        if (tempdistance < distance) {
            Pair<Boolean,LineFormula> blf = isKeyPoint(point);

            switch (blf.first?1:0){
                //if Pair<Float,Float> point is one the key points of the polygon then we want to "lockin" to that point unless distance is above a threshold
                case 1:
                    Pair<Float,Float> nextClosest = blf.second.GetNextClosestPoint(point.first,point.second);
                    double thresshold = Maths.GetDistance(point,nextClosest)/2;
                    double pointdis = Maths.GetDistance(point,tempPoint);
                    if(pointdis > thresshold){
                        point = tempPoint;
                    }
                    break;
                // if point is not a key point then set point equal to the tempPoint
                case 0:
                    point = tempPoint;
                    break;
            }
        }
        return point;
    }


    //don't include golden points of connecting lines when searching for the closest point.
    public Pair<Float,Float> GetBasicClosestPoint(float x,float y){
        Pair<Float, Float> point = new Pair<>(cx, cy);
        CircleFormula testCircle = incircle;
        Vector<LineFormula> lines = shapeLines;
        try {
            ShapeFormula sf = FindCircumShape(new Pair<>(x, y));
            int out = sf == null||sf==circumcircle ? 0 : sf == this||sf==incircle ? 1 : 2;
            switch (out) {

                //sf == null which mean (X,Y) lies out of bounds of this shape
                case 0:
                    //set testCircle to circumcircle so that only the circumscribed circle of the polygon is searched for, for the closest point
                    point = GetClosestToCircumCircle(x,y);

                    //sf == this
                case 1:

                    point = GetClosestPoint(x,y, lines);
                    Pair<Float,Float> pointA = GetClosestPoint(x,y,centerConnecting);
                    if(Maths.GetDistance(point,x,y) > Maths.GetDistance(pointA,x,y)){
                        point = pointA;
                    }
                    point = comparePointToCircle(x,y,point,testCircle,false, null,null);
                    break;

                //sf !=this which mean (X,Y) lies with in a shape that lies within this shape
                case 2:
                    point = sf.GetBasicClosestPoint(x, y);
                    break;
            }
        }

        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }
        return new Pair<>(point.first, point.second);
    }
    public Pair<Float,Float> GetBasicClosestPoint(Pair<Float,Float> p){
        return GetBasicClosestPoint(p.first,p.second);
    }
    private Pair<Float,Float> GetClosestPoint(float X, float Y, boolean startpoints, Float xstart,Float ystart){
        Pair<Float, Float> point = new Pair<>(cx, cy);
        CircleFormula testCircle = incircle;
        Vector<LineFormula> lines = connectingLines;
        try {
            ShapeFormula sf = FindCircumShape(new Pair<>(X, Y));
            int out = sf == null||sf==circumcircle ? 0 : sf == this||sf==incircle ? 1 : 2;
            switch (out) {

                //sf == null which mean (X,Y) lies out of bounds of this shape
                case 0:
                    //set lines for shapeLines so that only the perimeter lines are searched for, for the closest point
                    lines = shapeLines;
                    //set testCircle to circumcircle so that only the circumscribed circle of the polygon is searched for, for the closest point
                    testCircle = circumcircle;

                //sf == this
                case 1:
                    point = GetClosestPoint(X, Y, lines);
                    point = comparePointToCircle(X,Y,point,testCircle,startpoints,xstart,ystart);
                    break;

                //sf !=this which mean (X,Y) lies with in a shape that lies within this shape
                case 2:
                    point = sf.GetClosestPoint(X, Y);
                    break;
            }
        }

        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }
        return new Pair<>(point.first, point.second);
    }

    public Pair<Float, Float> GetClosestPoint(float X, float Y) {
       return GetClosestPoint(X,Y,false,null,null);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> p){
        return GetClosestPoint(p.first, p.second);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,Pair<Float,Float> p ){
        return GetClosestPoint(pstart.first, pstart.second, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,Pair<Float,Float> p ){
        return GetClosestPoint(xstart, ystart, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,float x,float y ){
        return GetClosestPoint(pstart.first, pstart.second, x, y);
    }
    private Pair<Float, Float> GetClosestPoint(float X, float Y, Vector<LineFormula> lines) {
     return GetClosestPointLine(X,Y,lines).second;
    }



    private Pair<LineFormula,Pair<Float, Float>> GetClosestPointLine(float X, float Y, Vector<LineFormula> lines) {
        Pair<Float, Float> point;
        LineFormula lf = new LineFormula(cx, cy, cx, cy);
        double distance = Maths.GetDistance(X, Y, cx, cy);
        for (LineFormula line : lines) {
            Pair<Float, Float> temppoint = line.GetClosestValue(X, Y);
            double tempdis = Maths.GetDistance(X, Y, temppoint.first, temppoint.second);
            boolean intercept = lf.doesLineCross(line);
            if (tempdis < distance || intercept) {
                if (intercept) {
                    double curdis = Maths.GetDistance(lf.GetClosestPoint(X, Y), X, Y);
                    double tempdistance = Maths.GetDistance(line.GetClosestPoint(X, Y), X, Y);
                    if (curdis > tempdistance) {
                        distance = tempdis;
                        lf = line;
                    }
                } else {
                    distance = tempdis;
                    lf = line;
                }
            }
        }
        point =  lf.GetClosestPoint(X, Y);
        return new Pair<>(lf,point);
    }
    //returns points of interest on the polygon
    public Formula[] GetGoldenPoints(){
        Formula[] p = new Formula[goldenP.size()];
        return goldenP.toArray(p);
    }

    public LineFormula[] GetGoldenLines(){
        LineFormula lf[] = new LineFormula[connectingLines.size()];
        return connectingLines.toArray(lf);

    }
    //returns the main points of the polygon
    public Pair<Float,Float>[] GetKeyPoints(){
        return points;
    }

    public double Area(){
        int n = points.length;
        double s = Maths.GetDistance(points[0], points[1]);
        double p = n*s;
        double  apothem = s/(2*(Math.tan(180/n)));
        return (apothem*p)/2;
    }


    //find the end point of a hypothetical shape with the same number of points as this shape 
    // and the same starting point as this shape that has the area passed to the function
    /*public Pair<Float,Float> getEndForArea(Pair<Float,Float> start,double area){
        return getEndForArea(area);
    }   
    
    public Pair<Float,Float> getEndForArea(double area){
        int n = points.length;
        Pair<Float,Float> end = getEnd();
        Pair<Float,Float> start = getStart();
        double height = Maths.GetDistance(start, end);
        double s =getSideLength();
        double p = n*s;
        double  apothem =getApothem();
        double oarea = (apothem*p)/2;
        double newS = (2*oarea)/(n*apothem);
        double newHeight = (newS*height)/s;

        return  Maths.findDistantPoint(end, start, newHeight, false);


    }*/

    //return the apothem for this shape
    public double getApothem(){
        return getSideLength()/(2*(Math.tan(Math.PI/points.length)));
    }

    //returns the length of the line between two connected points on the regular polygon
    public double getSideLength(){
        return   Maths.GetDistance(points[0], points[1]);
    }


    public boolean inCircumCircle(Pair<Float,Float> p){
        return circumcircle.inBounds(p);
    }

    public boolean inCircumCircle(float x, float y){
        return circumcircle.inBounds(x, y);
    }

    public boolean inCircumCircle(Formula shape){
        return circumcircle.inBounds(shape);
    }
    //determine if the x,y pair passed is inside this shape
    public boolean inBounds( Pair<Float,Float> p){
        boolean in = true;
        try {
            for (int j = 0; j < shapeLines.size(); ++j) {
                if (!shapeLines.get(j).inside(p) ) {
                    in = false;
                    j = shapeLines.size();
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return in;
    }

    public boolean inBounds(float x, float y){
        return inBounds(new Pair<>(x, y));
    }
    //determine if the shape passed is inside this shape
    public boolean inBounds(Formula shape){
        boolean in = true;
        in = circumcircle.inBounds(shape);
        /*Pair<Float,Float>[] kp = shape.GetKeyPoints();
        try {
            for (int i = 0; i < kp.length; ++i) {
                if(!inBounds(kp[i]) ){
                    i = kp.length;
                    in = false;
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }*/
            return in;
    }

    //find the smallest shape in this shape(including this shape) that the shape passed is completely inside of.
    public ShapeFormula FindCircumShape(ShapeFormula shape){
        ShapeFormula sh = null;
        try{
            if(inBounds(shape)){
                sh = this;
                for(int i = 0; i < inside.size(); ++i){
                    if(inside.get(i).inBounds(shape)){
                        sh = inside.get(i).FindCircumShape(shape);
                        i = inside.size();
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return sh;
    }

    public ShapeFormula FindCircumShape(Pair<Float,Float> p){

        return FindCircumShape(p.first, p.second);
    }
    //find the smallest shape in this shape(including this shape) that the point (x,y) is inside of.
    public ShapeFormula FindCircumShape(float x, float y){
        ShapeFormula sh = null;
        try{
            if(inBounds(x,y)){
                sh = this;
                for(int i = 0; i < inside.size(); ++i){
                    if(inside.get(i).inBounds(x,y)){
                        sh = inside.get(i).FindCircumShape(x,y);
                        i = inside.size();
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return sh;
    }


    //if shape is completly with in this shape then add the shape to this shape and return true, 
    //other wise do not add shape to this shape and return false
    public Pair<Boolean,ShapeFormula> AddShape(ShapeFormula shape){
        Pair<Boolean,ShapeFormula> bs = new Pair<>(false,null);
        if(Math.abs(shape.GetCircumCircle().h - circumcircle.h)<=.1 && Math.abs(shape.GetCircumCircle().k -circumcircle.k)<=.1 && Math.abs(shape.GetCircumCircle().radius - circumcircle.radius)<=.1){
            bs = new Pair<>(false,null);
        }
        boolean in = inBounds(shape);
        try {
            if (in ) {
                boolean inshape = false;
                for (int i = 0; i < inside.size(); ++i) {
                    bs = inside.get(i).AddShape(shape);
                    if (bs.first ) {
                        inshape = true;
                        i = inside.size();
                    }
                }

                if (!inshape ) {
                    bs = new Pair<Boolean,ShapeFormula>(true,this);
                    boolean inthisshape = false;
                    int index = -1;
                    for (int i = 0; i < inside.size(); ++i) {
                        if (shape.AddShape(inside.get(i)).first ) {
                            inthisshape = true;
                            index = i;
                            i = inside.size();
                        }
                    }
                    if (inthisshape ) {
                        inside.remove(index);
                    }
                    inside.add(shape);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return bs;
    }
    public void RemoveShape(ShapeFormula shape){
        if(shape == parent){
            parent = null;
        }
        else{
            int size = inside.size();
            for(int i = 0; i < size; ++i){
                if(inside.get(i)==shape){
                    ShapeFormula s = inside.get(i);
                    inside.remove(i);
                    Vector<ShapeFormula> insideremoved = s.GetInsideShapes();
                    for(ShapeFormula sf:insideremoved){
                        AddShape(sf);
                    }
                    i = size;
                }
            }
        }
    }
    public Vector<ShapeFormula> GetInsideShapes(){
        return inside;
    }
    //note the play is the frequency = area(this)/area(shapes inside this shape)
    //note the play is the frequency = area(this)/area(shapes inside this shape)
    public void Play() {

        try {
            double diamater = this.getDiamater();

            double area = circumcircle.Area();
            double f = area / incircle.Area();
            double radius = circumcircle.radius;
            play(f);
            for (ShapeFormula sh : inside) {

                double frequency = area / (Math.PI * (Math.pow((sh.getDiamater() / 2), 2)));
                sh.Play();
                play(frequency);
            }

            for (ShapeFormula sh : connectedShapes) {
                double distance = Maths.CircleDistance(circumcircle, sh.GetCircumCircle());


                double areaOverLap = Math.abs(Maths.FindAreaOverlappingCircles(circumcircle, sh.GetCircumCircle()));
                if(areaOverLap > 0) {
                    double frequency = circumcircle.Area() / areaOverLap;
                    play(frequency);
                }
                double pDiamater = sh.getDiamater();
                double frequency = pDiamater > diamater ? pDiamater / diamater : diamater / pDiamater;
                play(frequency);
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    private void play(double frequency ){

        try {
            Notes note = DiatonicScale.findNote(frequency);
            Sound.AddNote(note);
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }

    }




    public boolean doesLineCross(LineFormula lf){
        boolean cross = false;
        LineFormula lc = null;
        int crossCount = 0;
        for(LineFormula line: shapeLines){
            if(line.doesLineCross(lf)){
                cross = true;
                lc = line;
                ++crossCount;
            }
        }

        //handles case where starting point in on a line, but doesn't cross through the shape
        if(cross && crossCount == 1){
            if(lc.onTheLine(lf.startx,lf.starty)){
                if(!inBounds(lf.endx,lf.endy)){
                    cross = false;
                }
            }
            else{
                if(lc.onTheLine(lf.endx,lf.endy)){
                    if(!inBounds(lf.startx,lf.starty)){
                        cross = false;
                    }
                }
            }
        }
        return cross;
    }


    //addshape to connectedshapes if it's circumcircle is not completly with in the circumcircle of this shape,
    // but has at least one point that is partially connected to or inside this shape
    public void AddConnectedShape(ShapeFormula sf){

                    connectedShapes.add(sf);
        for(ShapeFormula sfi: inside){
            if(Maths.CirclesOverlap(sf.GetCircumCircle(),sfi.GetCircumCircle()).first){
                sfi.AddConnectedShape(sf);
            }
        }

    }
    //returns vector of shapes that are connected to, but not completely inside of this shape
    public Vector<ShapeFormula> GetConnectedShapes(){
        return connectedShapes;

    }



}
