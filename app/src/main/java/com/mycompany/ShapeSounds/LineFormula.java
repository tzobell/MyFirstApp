package com.mycompany.ShapeSounds;
/*
LineFormula class implements Formula interface and has variables and methods to describe a line segment and to find closest point on the line to another given point
* */

import android.util.Pair;

public class LineFormula implements Formula {

    private float m;//slope
    private float b;//yint
    private boolean straightLine; //false if line has a slope 0 or infinity
    private boolean constX; //true if slope is infinity
    private boolean constY; //true if slope is 0

    private Side side; //side that the line has
    private PointFormula points[];//key points +  golden points arranged in a binary search tree
    private PointFormula goldenP[];//only the golden points
    private PointFormula gpoints[];//contains all points
    private Pair<Float,Float> keyPoints[]; //start and end points
   // private float[] pts; //all points with each x y having their own index
     float startx,starty,endx,endy;


    //constructor
    public LineFormula(float startx,float starty,float endx,float endy, Side side, boolean main){
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.side = side;
        try {

            keyPoints = new Pair[2];
            keyPoints[0] = new Pair<Float,Float>(startx,starty);
            keyPoints[1] = new Pair<Float,Float>(endx,endy);
            //if startx && endx are not the same and starty and endy are not the same then the line is not straight
            if (startx != endx && starty != endy) {
                straightLine = false;
                constX = false;
                constY = false;
                m = (endy - starty) / (endx - startx);
                b = starty - (m * startx);
            } else {
                straightLine = true;
                if (startx == endx) {
                    constX = true;
                    constY = false;
                } else {
                    constX = false;
                    constY = true;
                }

            }
            gpoints = GoldenPoints.getAllPoints(startx, starty, endx, endy,main);
            //pts = new float[gpoints.length *2];
            int index = 0;
           // for(PointFormula p:gpoints){
            //    pts[index++] = p.x;
           //     pts[index++] = p.y;
           // }

            goldenP = GoldenPoints.getGoldenPoints(startx, starty, endx, endy,main);
            //find the number of points that should be in the points bindary search tree
            int l = gpoints.length % 2 != 0 ? gpoints.length : gpoints.length + 1;
            points = new PointFormula[(2*l)+1];
            createTree(0, 0, l - 1, gpoints);
        }
        catch (Exception e){
            String a = e.getMessage();
            System.out.println(a);
        }

    }
    public LineFormula(float startx,float starty,float endx,float endy){
        this(startx,starty,endx,endy,Side.above, false);
    }
    public LineFormula(float startx,float starty,float endx,float endy, Side side){
        this(startx,starty,endx,endy,side, false);
    }
    public LineFormula(float startx,float starty,float endx,float endy, boolean main){
        this(startx,starty,endx,endy,Side.above, main);
    }
    //return the side of the line that the point (x,y) is on
  public Side getSide(float x, float y){
      Side s = null;
      try {
          //if the line is not straight or is horizontal then (x,y) is either above or below the line
          if (!straightLine || constY) {
              float ty = FoX(x, y).second;
              if (y > ty) {
                  s = Side.below;
              } else {
                  if (y < ty) {
                      s = Side.above;
                  }
              }
          } else {
              //if the line is a vertical straight line then (x,y) is either to the left or right of the line.
              if (constX) {
                  if (x > startx) {
                      s = Side.right;
                  }
                  if (x < startx) {
                      s = Side.left;
                  }
              }
          }
      }
      catch (Exception e){
          String a  = e.getMessage();
          System.out.println(a);
      }
      return s;
  }

    public Pair<Float,Float> GetStart(){
        return new Pair<Float,Float>(startx,starty);
    }
    public Pair<Float,Float> GetEnd(){
        return new Pair<Float,Float>(endx,endy);
    }
    //returns array of points not including the start and stop points on line
    public PointFormula[] GetGoldenPoints(){
        return  goldenP;
    }

    //return all points of interest in line including the start and stop points of the line
    public PointFormula[] GetAllPoints(){
        return gpoints;
    }

    //returns array containing all the points in the shape including golden points, but with each x and y value at its own index int the array
    //so instead of index(i) == Pair<Float,Float>(x,y), we have index(i) == x and index(i+1) == y
   // public float[] GetPoints(){
   //     return pts;
   // }

//create binary search tree for searching through points
    private void createTree(int i, int start,int end, PointFormula[] gpoints) {
        try {
            if (start <= end) {
                int mid = ((end + start)/2);
                points[i] = mid < gpoints.length ? gpoints[mid] : null;
                int childA = (2 * i) + 1;
                int childB = (2 * i) + 2;
                createTree(childA, start, mid - 1, gpoints);
                createTree(childB, mid + 1, end, gpoints);
            }
        }
        catch(Exception e){
            String a = e.getMessage();
            System.out.println(a);
        }
    }

    //returns the closest point in the points array to point (X,Y)
    public Pair<Float,Float> GetClosestPoint(float X, float Y) {
       return GetClosestPoint(X, Y, false, null);
    }

    //returns the closest point in the points array to point (X,Y)
    //if except is true then it returns the closest point, unless it is equal to notThis, in
    //which case it returns the next closest point
    private Pair<Float,Float> GetClosestPoint(float X, float Y,boolean except,Pair<Float,Float> notThis) {
        double curdis = Double.POSITIVE_INFINITY;
        int closest = 0;
        try {
            for (int i = 0; i < gpoints.length; ++i) {
                if (!except || !gpoints[i].equals(notThis.first, notThis.second)) {
                    double tempdis = Maths.GetDistance(X, Y, gpoints[i]);
                    if (tempdis < curdis) {
                        curdis = tempdis;
                        closest = i;
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return gpoints[closest].GetClosestPoint(X, Y);
    }


    //gets the second closest point to (x,y)
    public Pair<Float,Float> GetNextClosestPoint(Float x,Float y){
        return GetClosestPoint(x, y, true, GetClosestPoint(x, y));
    }

    //returns the closest point to x,y out of the starting and ending point of the line
    public Pair<Float,Float> GetBasicClosestPoint(float x,float y){
        double distanceStart = Maths.GetDistance(x,y,startx,starty);
        double distanceEnd = Maths.GetDistance(x,y,endx,endy);
        return distanceStart>distanceEnd?new Pair<Float,Float>(endx,endy):new Pair<Float,Float>(startx,starty);
    }
    public Pair<Float,Float> GetBasicClosestPoint(Pair<Float,Float> p){
        return GetBasicClosestPoint(p.first,p.second);
    }

    //returns the closest (x,y) value on the line
    //if withInSegment is true, find the closest value that lies on or between (startx,starty),(endx,endy) to point (X,Y)
    //else if withInSegment if false find closest value to (X,Y) on the line as if the line went out to infinity
    public Pair<Float,Float> GetClosestValue(float x,float y, boolean withInSegment){
        //false,false, = 0
        //both true = 3
        //true,false = 1
        //false,true = 2
        float closestx = x;
        float closesty = y;
        try {
            if (!straightLine) {
                //find line of y = mx+b that is perpendicular to this line and passes through (X,Y)
                float pm = -1 * (1 / m);
                float pb = y - (pm * x);
                closestx = (y - pb) / pm;
                //y value at which this line and the line perpendicular to it that passes through (X,Y) = (b+(m^2 * pb))/(m^2 + 1)
                closesty = (b + (m * m * pb)) / ((m * m) + 1);
                closestx = (closesty - b) / m;

                //if closest point on equation y = mx+b to point (X,Y) is outside of (startx,starty),(endx,endy), then find which end point is closest to (X,Y)
               if(withInSegment) {
                   if (!Maths.inRange(startx, endx, closestx) || !Maths.inRange(starty, endy, closesty) ) {
                       double distanceStart = Maths.GetDistance(startx, starty, x, y);
                       double distanceEnd = Maths.GetDistance(endx, endy, x, y);
                       if (distanceStart < distanceEnd) {
                           closestx = startx;
                           closesty = starty;
                       } else {
                           closestx = endx;
                           closesty = endy;
                       }
                   }
               }

            } else {
                if (constX) {
                    closestx = startx;
                    closesty = y;
                } else {
                    closestx = x;
                    closesty = starty;

                }
            }
        }
        catch(Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }

       return new Pair<Float,Float>(closestx,closesty);
    }

    public Pair<Float,Float> GetClosestValue(Float X,Float Y){
        return GetClosestValue(X, Y, true);
    }

    //find the line of y = mx+b if line has a slope or x=startx or y = starty if line has a slope of 0 or infinity
    // that is perpendicular to this line and passes through (X,Y)
    public LineFormula GetPerpindicular(float x,float y){
        float tempStarty = starty;
        float tempEndy = endy;
        float tempStartx = startx;
        float tempEndx = endx;

        try {
            if (!straightLine) {
                float pm = -1 * (1 / m);//slop of perpendicular line
                float pb = y - (pm * x); //y int of perpendicular line

                //startx,endx
                int senario = Maths.inRange(startx, endx, x) ? 0 : Maths.inRange(startx, x, endx) ? 1 : 2;
                switch (senario) {
                    //x is inbetween or equal to the range from (startx to endx);
                    case 0:
                        tempStartx = startx;
                        tempEndx = endx;
                        tempStarty = (pm * tempStartx) + pb;
                        tempEndy = (pm * tempEndx) + pb;
                        break;
                    //endx is inbetween or equal to the range from (startx to x);
                    case 1:
                        tempStartx = startx;
                        tempEndx = x;
                        tempStarty = (pm * tempStartx) + pb;
                        tempEndy = (pm * tempEndx) + pb;
                        break;
                    //startx is inbetween or equal to the range from (x to endx);
                    case 2:
                        tempStartx = x;
                        tempEndx = endx;
                        tempStarty = (pm * tempStartx) + pb;
                        tempEndy = (pm * tempEndx) + pb;
                        break;
                }
            } else {
                if (constX) {
                    tempStarty = y;
                    tempEndy = y;
                    double dis = Maths.GetDistance(startx, endy, endx, endy);
                    tempStartx = (float) (x + (dis / 2));
                    tempEndx = (float) (x - (dis / 2));
                    int senario = Maths.inRange(tempStartx, tempEndx, startx) ? 0 : Maths.inRange(tempEndx, startx, tempStartx) ? 1 : 2;
                    switch (senario) {
                        //startx inbetween tempstartx and tempEndx
                        case 0:
                            break;
                        //tempstartx inbetween tempendx and startx
                        case 1:
                            tempStartx = startx;
                            break;
                        //tempendx inbetween startx and tempstartx
                        case 2:
                            tempEndx = startx;
                            break;
                    }
                } else {
                    tempStartx = x;
                    tempEndx = x;
                    double dis = Maths.GetDistance(startx, endy, endx, endy);
                    tempStarty = (float) (y + (dis / 2));
                    tempEndy = (float) (y - (dis / 2));
                    int senario = Maths.inRange(tempStarty, tempEndy, starty) ? 0 : Maths.inRange(tempEndy, starty, tempStarty) ? 1 : 2;
                    switch (senario) {
                        //starty inbetween tempstarty and tempEndy
                        case 0:
                            break;
                        //tempstarty inbetween tempendy and starty
                        case 1:
                            tempStarty = starty;
                            break;
                        //tempendy inbetween starty and tempstarty
                        case 2:
                            tempEndy = starty;
                            break;
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return new LineFormula(tempStartx,tempStarty,tempEndx,tempEndy);
    }

    //return true or false for wither the lineFormula passed crosses through this lineFormula segment.
    public boolean doesLineCross(LineFormula lf){
        boolean cross = true;
        if(getSide(lf.startx,lf.starty) == getSide(lf.endx,lf.endy) || lf.getSide(startx,starty) == lf.getSide(endx,endy)){
            cross = false;
        }
        return cross;
    }

    //returns true if (x,y) is on the line, other wise returns false
    public boolean onTheLine(float x, float y){
        boolean online = false;
        if(!straightLine){
            if(y == ((m*x)+b)){
                online = true;
            }
        }
        else{
            if(constX && startx == x){
                online = true;
            }
            else{
                if(constY && starty == y){
                    online = true;
                }
            }
        }
        return online;
    }

    //fox stands for f of x
    //if the line has a slope, and fox is true then return Pair(p.first,y=f(p.first))
    ////if the line has a slope, and fox is false then return Pair(x=f(p.second),p.second)
    //if x is constant then returns Pair(constant x value, p.second)
    //if y is constant then returns Pair(p.first,constant y value)
    private Pair<Float,Float> getpoint(Pair<Float,Float> p,boolean fox){
        float x = p.first;
        float y = p.second;
        if(!straightLine){
            if(fox) {
                y = (m * p.first) + b;
            }
            else{
                x = (y-b)/m;
            }
        }
        else{
            if(constX){
                x = startx;
            }
            else{
                y = starty;
            }
        }
        return new Pair<Float,Float>(x,y);
    }
    private Pair<Float,Float> getpoint(Pair<Float,Float> p){
        return getpoint(p, true);
    }
    //if the line has a slope, then return Pair(p.first,y=f(p.first))
    //if x is constant then returns Pair(constant x value, p.second)
    //if y is constant then returns Pair(p.first,constant y value)
    public Pair<Float,Float> FoX(float x, float y) {
        return FoX(new Pair<Float, Float>(x, y));
    }

    private Pair<Float,Float> FoX(Pair<Float,Float> p){

        return getpoint(p, true);
    }

    //if the line has a slope, then return Pair(x=f(p.second),p.second)
    //if x is constant then returns Pair(constant x value, p.second)
    //if y is constant then returns Pair(p.first,constant y value)
    private Pair<Float,Float> FoY(float x, float y){
        return FoY(new Pair<Float, Float>(x, y));
    }

    private Pair<Float,Float> FoY(Pair<Float,Float> p){
       return getpoint(p, false);
    }

    //return y = f(x)
    public float FoX(float x){
        return FoX(x,starty).second;
    }

    //return x = f(y)
    public float FoY(float y){
        return FoX(startx,y).first;
    }

    //find point on line(start,end) that is distance away from start
    //if inrange is true, find point that is distance away from (startx,starty) going towards (endx,endy)
    //if inrange is false, fine point that is distance away from (startx,starty) going away from (endx,endy);
    public Pair<Float,Float> findDistantPoint(double distance, boolean inrange ){
        float x1 = startx;
        float x2 = endx;
        //x value along line y=mx+b that is n distance away from x1,y1 is x1+sqrt((n^2)/(1+(m^2)));
        double newx = x1 + Math.sqrt((Math.pow(distance, 2)) / (1 + (m * m)));
        double newy = (m * newx) + b;

        if(inrange) {
            if((x2 < x1 && newx > x1) || (x2 > x1 && newx < x1)){
                newx = x1 - Math.sqrt((Math.pow(distance, 2)) / (1 + (m * m)));
                newy = (m * newx) + b;
            }
        }
        else{
            if((x2 < x1 && newx < x1) || (x2 > x1 && newx > x1)){
                newx = x1 - Math.sqrt((Math.pow(distance, 2)) / (1 + (m * m)));
                newy = (m * newx) + b;
            }
        }
        return  new Pair<>((float)newx,(float)newy);
    }
    public Pair<Float,Float> findDistantPoint(double distance ){
        return findDistantPoint(distance,true);
    }

    //returns true if (x,y) is one the main or golden points on this line, else return false
    public boolean isPoint(float x,float y){
        boolean ispoint = false;
        for(PointFormula pf:gpoints){
            if(pf.x == x && pf.y == y){
                ispoint = true;
                break;
            }
        }
        return ispoint;
    }

    //returns true if p is on the same side as the variable side corresponds to
    public boolean inside(Pair<Float,Float> p){
        boolean in = false;
        try {
            Pair<Float, Float> testX = FoX(p);// getpoint(p);
            Pair<Float, Float> testY = FoY(p);
            double xdiff = Math.abs(testY.first - p.first);
            double ydiff = Math.abs(testX.second - p.second);
            double cuttoff = .001;
            switch (side) {
                case above:
                    in = (p.second <= testX.second || cuttoff >= ydiff);
                    break;
                case below:
                    in = (p.second >= testX.second || cuttoff >= ydiff);
                    break;
                case left:
                    in = (p.first <= testY.first || cuttoff >= xdiff);
                    break;
                case right:
                    in = (p.first >= testY.first || cuttoff >= xdiff);
                    break;
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return in;
    }

    //returns true if (x,y) is equal to the starting or ending points of the line, else returns false
    public boolean isKeyPoint(float x, float y){
        boolean isKey = false;
        for(Pair<Float,Float> p:keyPoints){
            if(p.first == x && p.second == y){
                isKey = true;
                break;
            }
        }
        return isKey;
    }
    public Pair<Float,Float>[] GetKeyPoints(){
        return keyPoints;
    }

    public ShapeType getShapeType() {
        return ShapeType.line;
    }
}



