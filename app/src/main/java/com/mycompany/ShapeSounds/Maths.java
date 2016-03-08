package com.mycompany.ShapeSounds;

import android.graphics.Point;
import android.util.Pair;

/*contains various static methods that perform mathmatical functions*/
public final class Maths {

    public static final double PHI = (1 + Math.sqrt(5)) / 2;

    //finds mid point between x and y
    public static double Middle (int x, int y){
        double mid = 0;
        if( x > y){
            int tempy = y;
            y = x;
            x = tempy;
        }

        int diff = (y-x)+1;
        if(diff%2 == 0){
            mid = (diff/2)+.5;
        }
        else{
            int tmid = diff/2;
            mid = (double)tmid;
        }

        return mid;
    }

    //returns distance betweeing (x1,y1) and (x2,y2)
    public static double GetDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
    public static double GetDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
    public static double GetDistance(Point a, Point b) {
        return GetDistance(a.x, a.y, b.x, b.y);
    }
    public static double GetDistance(Pair<Float,Float> a, Pair<Float,Float> b) {
        return GetDistance(a.first, a.second, b.first, b.second);
    }
    public static double GetDistance(float x1, float y1, Pair<Float,Float> b) {
        return GetDistance(x1, y1, b.first, b.second);
    }
    public static double GetDistance(Pair<Float,Float> a, float x2, float y2) {
        return GetDistance(a.first, a.second, x2, y2);
    }
    public static double GetDistance(PointFormula a, float x2, float y2) {
        return GetDistance(a.x, a.y, x2, y2);
    }
    public static double GetDistance(float x1, float y1, PointFormula b) {
        return GetDistance(x1, y1, b.x, b.y);
    }
    public static double GetDistance(PointFormula a, PointFormula b) {
        return GetDistance(a.x, a.y, b.x, b.y);
    }

    //returns true if testnumber between a and b or equal to a or b, else returns false
    public static boolean inRange(float a, float b, float testnumber) {
        boolean within = false;
        if (a > b) {
            float tempa = a;
            a = b;
            b = tempa;
        }
        if (testnumber >= a && testnumber <= b) {
            within = true;
        }
        return within;
    }

    public static Pair<Float,Float> findDistantPoint(Pair<Float,Float> start, Pair<Float,Float> end, double distance) {
        return findDistantPoint(start, end, distance, true);
    }

    //find point on line(start,end) that is distance away from start
    //if inrange is true, find point that is distance away from start going towards (endx,endy)
    //if inrange is false, fine point that is distance away from start going away from (endx,endy);
    public static Pair<Float,Float> findDistantPoint(Pair<Float,Float> start, Pair<Float,Float> end, double distance, boolean inrange) {
        LineFormula lf = new LineFormula(start.first, start.second, end.first, end.second);
        return lf.findDistantPoint(distance, inrange);
    }

    //determine if circle a and b intersect and if so the points at which they intersect
    public static Pair<Boolean, Pair<Pair<Float,Float>, Pair<Float,Float>>> CirclesIntersectPoints(CircleFormula a, CircleFormula b) {
        boolean intersect = true;
        double y1 = Double.NaN;
        double y2 = Double.NaN;
        double x1 = Double.NaN;
        double x2 = Double.NaN;

        //extend circle formulas
        //circle a = (x-ha)^2 + (y-ka)^2 = ra^2 => x^2 - 2hax + ha^2 + y^2 -2hay + ka^2 = ra^2
        //circle b = (x-hb)^2 + (y-kb)^2 = rb^2 => x^2 - 2hbx + hb^2 + y^2 -2hby + kb^2 = rb^2

        // times first formula by -1
        // -1(x^2 - 2hax + ha^2 + y^2 -2hay + ka^2 = ra^2) =>
        // -x^2 + 2hax - ha^2 - y^2 + 2hay - ka^2 = -ra^2

        //add formulas together to eliminate x^2
        //   -x^2 + 2hax - ha^2 - y^2 + 2hay - ka^2 = -ra^2
        //   +
        //   x^2 - 2hbx + hb^2 + y^2 - 2hby + kb^2 =  rb^2
        // ____________________________________________________
        // 2hax -2hbx -ha^2 + hb^2 +2hay - 2hby - ka^2 + kb^2 = rb^2 - ra^2 =>
        // 2hax -2hbx -ha^2 + hb^2 +2hay - 2hby - ka^2 + kb^2 - rb^2 + ra^2 = 0

        // double nx = (2 * ha * x) - (2 * hb * x)
        // double iy = (2*ha*y) - (2*hb*y)
        // double num =  hb^2 - ha^2  + kb^2 - ka^2 + ra^2 - rb^2

        double n = 2 * (a.h - b.h);
        double i = 2 * (a.k - b.k);
        double num = Math.pow(b.h, 2) - Math.pow(a.h, 2) + Math.pow(b.k, 2) - Math.pow(a.k, 2) + Math.pow(a.radius, 2) - Math.pow(b.radius, 2);

        // nx + iy + num = 0
        // x = ((-iy-num)/n)

        //sub x for (-iy-num)/n in (x^2 - 2hax + ha^2 + y^2 -2hay + ka^2 = ra^2)
        //x^2 - 2hax + ha^2 + y^2 -2hay + ka^2 = ra^2=>
        //((-iy-num)/n)^2 - 2((-iy-num)/n)ha + ha^2 + y^2 -2hay + ka^2 = ra^2 =>
        //(iy^2)/n^2 +(((2*i*num)y)/n^2 + (num^2)/n^2 +  (2*ha*i)y/n + 2*ha/n  + ha^2 + y^2 -2hay + ka^2 -ra^2=0

        //Ay^2 = (iy^2)/n^2 + y^2
        //By = (((2*i*num)y)/n^2 +  (2*ha*i)y/n - 2hay
        //C = (num^2)/n^2  + 2*ha/n  + ha^2   + ka^2 -ra^2

        double A = (Math.pow(i, 2) / Math.pow(n, 2)) + 1;
        double B = (2 * i * num / Math.pow(n, 2) / Math.pow(n, 2)) + ((2 * a.h * i) / n) - (2 * a.k);
        double C = (Math.pow(num, 2) / Math.pow(n, 2)) + ((2 * a.h) / n) + Math.pow(a.h, 2) + Math.pow(a.k, 2) - Math.pow(a.radius, 2);

        //Ay^2 + By + C = 0
        //find answers to equation with quadradic formula
        //region inside sqaure root of quadradic formula B^2 - 4AC
        double quadsquareroot = (B * B) - (4 * A * C);

        //if solution to quadradic formula
        if (quadsquareroot >= 0) {
            y1 = (-B + Math.sqrt((B * B) - (4 * A * C))) / (2 * A);
            y2 = (-B - Math.sqrt((B * B) - (4 * A * C))) / (2 * A);
            //plug y values into x = ((-iy-num)/n) to get x answers
            x1 = ((-1 * i * y1) - num) / n;
            x2 = ((-1 * i * y2) - num) / n;
        }
        //if not
        else {
            intersect = false;
        }
        // Pair<Boolean,Pair<Pair<Float,Float>,Pair<Float,Float>>>
        return new Pair<>(intersect, new Pair<>(new Pair<Float,Float>((float) x1, (float) y1), new Pair<Float,Float>((float) x2, (float) y2)));
    }


    //returns pair of boolean values, first value is true/falst for if and only if circles partially overlap,
    // second value true if circles completly overlap (one circle is completly with in or equal to the other circle)
    public static Pair<Boolean, Boolean> CirclesOverlap(CircleFormula a, CircleFormula b) {
        boolean overlap = false;
        Pair<Float,Float> A = new Pair<Float,Float>(a.h, a.k);
        Pair<Float,Float> B = new Pair<Float,Float>(b.h, b.k);
        double d = Math.abs(GetDistance(A, B));
        return CirclesOverlap(a, b, d);
    }

    ///returns pair of boolean values, first value is true/falst for if and only if circles partially overlap,
    // second value true if circles completly overlap (one circle is completly with in or equal to the other circle)
    private static Pair<Boolean, Boolean> CirclesOverlap(CircleFormula a, CircleFormula b, double d) {
        boolean partiallyOverlap = false;
        boolean completlyOverlap = false;
        if (a.radius < b.radius) {
            CircleFormula temp = b;
            b = a;
            a = temp;
        }
        if (d < (a.radius + b.radius)) {
            //if distance between center of a and center of b is less than radius of a - radius of b, then circle b is completely inside circle a
            if (d < Math.abs(a.radius - b.radius)) {
                completlyOverlap = true;
                partiallyOverlap = false;

            }
            //else circles partially overlap
            else {
                completlyOverlap = false;
                partiallyOverlap = true;
            }
        }
        return new Pair<>(partiallyOverlap, completlyOverlap);
    }

    //find the area of the overlapping sections of the two circles
    public static double FindAreaOverlappingCircles(CircleFormula a, CircleFormula b) {
        double area = 0;
        try {
            Pair<Float,Float> A = new Pair<Float,Float>(a.h, a.k);
            Pair<Float,Float> B = new Pair<Float,Float>(b.h, b.k);
            double d = Math.abs(GetDistance(A, B));//distance from center of circle a to center of circle c
            Pair<Boolean, Boolean> overlap = CirclesOverlap(a, b, d);
            if (overlap.first || overlap.second) {
                //if distance between center of a and center of b is less than radius of a - radius of b, then circle b is completely inside circle a
                if (overlap.second) {
                    area = b.Area();
                }
                //circles partially overlap
                else {
                    if (overlap.first) {
                        //area of circle sector = (theta/2) * r^2
                        //point C and D are the points at which circle a and b intersect
                        //theta = angle CAD of triangle ACD
                        //find theta
                        //divide  triangle ACD into two right triangles ADG and AEG
                        //cos = adjacent/hypotenuse
                        //adjacent = d/2
                        //hypotenuse = radius of circle a or line segment DA
                        //cos(theta/2) = (d/2)/r => theta/2 = cox^-1(d/2r) => theta = 2cos^-1(d/2r)
                        double thetaA = 2 * Math.acos(d / (2 * a.radius));
                        //compute area of circle sector for circle a
                        double ACDSector = (thetaA / 2) * a.radius * a.radius;
                        //area of triangle = (r^2)/2 * sin(theta)
                        double triangle_ACD_Area = .5 * Math.pow(a.radius, 2) * Math.sin(thetaA);
                        double areasegmentA = ACDSector - triangle_ACD_Area;
                        //repeat for circle b
                        double thetaB = 2 * Math.acos(d / (2 * b.radius));
                        double BCDSector = (thetaB / 2) * b.radius * b.radius;
                        double triangle_BCD_Area = .5 * Math.pow(b.radius, 2) * Math.sin(thetaB);
                        double areasegmentB = BCDSector - triangle_BCD_Area;
                        area = areasegmentA + areasegmentB;
                    }
                }
            }
        } catch (Exception e) {
            String z = e.getMessage();
            System.out.println(z);
        }
        return area;
    }

    //returns the distance between the circumferences of the two circles
    public static double CircleDistance(CircleFormula a, CircleFormula b) {
        return GetDistance(a.h, a.k, b.h, b.k) - (a.radius + b.radius);
    }

    //returns the center point of the triangle
    public static Pair<Float,Float> FindTriangleCenter(Pair<Float,Float> a, Pair<Float,Float> b, Pair<Float,Float> c) {
        float x = (a.first + b.first + c.first) / 3;
        float y = (a.second + b.second + c.second) / 3;
        return new Pair<Float,Float>(x, y);
    }


    //returns the external tangent that both the circles lie on
    public static LineFormula ExternalTangent(CircleFormula a, CircleFormula b) {
        LineFormula tangent = null;

        if (b.radius > a.radius) {
            CircleFormula temp = a;
            a = b;
            b = temp;
        }
        double cd = GetDistance(a.h,a.k,b.h,b.k);

        if ( cd >= 0) {
            double distance = GetDistance(a.h, a.k, b.h, b.k);

            double h = Math.sqrt((distance * distance) - Math.pow((a.radius - b.radius), 2));

            double y = Math.sqrt((h * h) + (b.radius * b.radius));

            double theta = Math.acos(
                    ((a.radius * a.radius) + (distance * distance) - (y * y)) / (2 * a.radius * distance)
            );

            theta = theta + Math.atan2(b.k - a.k, b.h - a.h);
            float x1 = (float) (a.h + (a.radius * Math.cos(theta)));
            float y1 = (float) (a.k + (a.radius * Math.sin(theta)));
            LineFormula lf = null;
            if ((x1 != (a.radius + a.h)) && x1 != a.h) {
                double slope = (-1 * (x1 - a.h)) / Math.sqrt((a.radius * a.radius) - Math.pow((x1 - a.h), 2));
                double yint = y1 - (slope * x1);
                if (y1 != ((slope * x1) + yint)) {
                    slope = slope * -1;
                }
                lf = new LineFormula(x1, y1, b.h, (float) ((b.h * slope) + yint));
            } else {
                //slope = infinity
                if (x1 == (a.radius + a.h)) {
                    lf = new LineFormula(x1, y1, x1, b.k);
                }
                //slope = 0
                if (x1 == a.h) {
                    lf = new LineFormula(x1, y1, b.h, y1);
                }
            }
            Pair<Float,Float> secondTanPoint = lf.findDistantPoint(h);
            tangent = new LineFormula(x1, y1, secondTanPoint.first, secondTanPoint.second);
        }
        return tangent;
    }
}
