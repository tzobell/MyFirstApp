package com.mycompany.ShapeSounds;

import android.util.Pair;

import java.util.Vector;

/**
 contains methods for determining "golden points" on a line using the golden ratio of (1 + Math.sqrt(5)) / 2 as a basis
 */


public class GoldenPoints {
    private static final int goldennum = 9;
    public static PointFormula[] getGoldenPoints(float startx, float starty, float endx, float endy){
        return getGoldenPoints(startx,starty,endx,endy,false);
    }

    //returns golden points for the line going from (startx,starty) to (endx,endy) if main == true, then only array of 3 main golden points are
    //returned, if main== false, then an array of 9 golden points are returned.
    //points are sorted in the array in order of closest to (startx,stary) to furthest away from (startx,starty)
    public static PointFormula[] getGoldenPoints(float startx, float starty, float endx, float endy, boolean main){
        int index = 0;
        PointFormula gpoints[] = new PointFormula[goldennum-4];
        try {
            if(main == true) {
                gpoints = new PointFormula[3];
                gpoints[0] = new PointFormula(startx,starty);
                gpoints[1] = new PointFormula(((startx+endx)/2),((starty+endy)/2));
                gpoints[2] = new PointFormula(endx,endy);
                //gpoints = calcGoldenPoints(startx, starty, endx, endy);
            }
            else {
                PointFormula gpointsA[] = calcGoldenPoints(startx, starty, endx, endy);
                PointFormula gpointsB[] = calcGoldenPoints(startx, starty, gpointsA[2].x, gpointsA[2].y);
                PointFormula gpointsC[] = calcGoldenPoints(gpointsA[0].x, gpointsA[0].y, endx, endy);
                Vector<Pair<PointFormula,Double>> distances = new Vector<>();
                /*for (int i = 0; i < gpointsB.length-1; ++i) {
                    if (gpointsB[i] != null) {
                        gpoints[index++] = gpointsB[i];
                    }
                }*/

                gpoints[index++] = gpointsB[0];


                for (int i = 0; i < gpointsA.length; ++i) {
                    if (gpointsA[i] != null) {
                        gpoints[index++] = gpointsA[i];
                    }
                }

                /*for (int i = 1; i < gpointsC.length; ++i) {
                    if (gpointsC[i] != null) {
                        gpoints[index++] = gpointsC[i];
                    }
                }*/
                gpoints[index++] = gpointsC[2];

                for(int i = 0; i < gpoints.length; ++i) {
                    double tempdis = Maths.GetDistance(startx, starty, gpoints[i]);

                    int j = 0;
                    while (j < distances.size() && tempdis > distances.get(j).second) {
                        ++j;
                    }
                    distances.insertElementAt(new Pair(gpoints[i],tempdis),j);
                }

                for(int i = 0; i < gpoints.length; ++i){
                    gpoints[i] = distances.get(i).first;
                }

            }

        }
        catch (Exception e){
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
        return gpoints;

    }


    //returns array of 3 main golden points for line going from (startx,stary) to (endx,endy)
    //points are sorted in the array in order of closest to (startx,stary) to furthest away from (startx,starty)
    public static PointFormula[] getMainGoldenPoints(float startx, float starty, float endx, float endy){
        PointFormula gpoints[] = new PointFormula[goldennum];
        try {
             gpoints = calcGoldenPoints(startx, starty, endx, endy);
        }
        catch (Exception e){
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
        return gpoints;
    }

    //returns array of 3 main golden points for line going from (startx,stary) to (endx,endy)
    // the center point between (startx,starty) and (endx,endy) and the points on either side of the center
    //that have a golden ratio to the remainder of the line segment
    //points are sorted in the array in order of closest to (startx,stary) to furthest away from (startx,starty)
    private static PointFormula[] calcGoldenPoints(float startx, float starty, float endx, float endy){
        PointFormula gpoints[] = new PointFormula[3];
        try {
            double gx1 = startx;
            double gx2 = startx;
            double gy1 = starty;
            double gy2 = starty;
            if (endy != starty) {
                double l2 = endy - starty;
                gy1 = (l2 / Maths.PHI);
                gy2 = l2 - gy1;
                gy1 += starty;
                gy2 += starty;
            }
            if (endx != startx) {
                double l1 = endx - startx;
                gx1 = (l1 / Maths.PHI);
                gx2 = l1 - gx1;
                gx1 += startx;
                gx2 += startx;
            }

            if (Maths.GetDistance(gx1, gy1, startx, starty) < Maths.GetDistance(gx2, gy2, startx, starty)) {
                gpoints[0] = new PointFormula((float) gx1, (float) gy1);
                gpoints[1] = new PointFormula((startx + endx) / 2, (starty + endy) / 2);
                gpoints[2] = new PointFormula((float) gx2, (float) gy2);
            } else {
                gpoints[0] = new PointFormula((float) gx2, (float) gy2);
                gpoints[1] = new PointFormula((startx + endx) / 2, (starty + endy) / 2);
                gpoints[2] = new PointFormula((float) gx1, (float) gy1);
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return gpoints;
    }

    //returns an array with the the goldenpoints and the starting and ending points
    //if main == true, then only 3 main golden points are included in the array
    //if main== false, then an 9 golden points are included in the array
    //points are arranged in the array so that index 0 is (startx,starty) and the then adds points in order of closest to (startx,stary) to furthest away from (startx,starty)
    //with (endx,endy) being at the end of the array
    public static PointFormula[] getAllPoints(float startx,float starty,float endx,float endy, boolean main){

        PointFormula gpoints[] = null;
        PointFormula points[] = null;
        try {
            gpoints = getGoldenPoints(startx, starty, endx, endy, main);

            points= new PointFormula[gpoints.length + 2];
            for (int i = 1; i < points.length - 1; ++i) {
                points[i] = gpoints[i - 1];
            }
            points[0] = new PointFormula(startx, starty);
            points[points.length - 1] = new PointFormula(endx, endy);
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return points;
    }
    public static PointFormula[] getAllPoints(float startx,float starty,float endx,float endy) {
        return getAllPoints(startx,starty,endx,endy,false);
    }
}
