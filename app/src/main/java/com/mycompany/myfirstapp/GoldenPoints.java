package com.mycompany.myfirstapp;

import android.util.Pair;

import java.util.Vector;

/**
 * Created by Owner on 9/11/2015.
 */


public class GoldenPoints {
    private static final int goldennum = 9;
    private PointFormula harmoniousPoints[];
    private int hp = 0;
    public  final int length;

    public GoldenPoints(int points){
        length = points;
        harmoniousPoints = new PointFormula[points];
    }

    public void AddPoint(Pair<Float,Float> point){
        if(hp < harmoniousPoints.length){
            harmoniousPoints[hp++]  = new PointFormula(point.first,point.second);
        }
    }

    public void AddPoint(Float x, Float y){
        if(hp < harmoniousPoints.length){
            harmoniousPoints[hp++]  = new PointFormula(x,y);
        }
    }

    public PointFormula[] GetGoldenPoints(){
        return harmoniousPoints;

    }

    public Pair<Float,Float> GetPoint(int i){
        Pair<Float,Float> p = null;
        if(i < hp){
            p = new Pair<Float,Float>(harmoniousPoints[i].x,harmoniousPoints[i].y);
        }
        return p;
    }

    public void AddPointsOnLine(Pair<Float,Float> start, Pair<Float,Float> end){
         AddPointsOnLine(start.first,start.second,end.first,end.second);
    }

    public static PointFormula[] getGoldenPoints(float startx, float starty, float endx, float endy){
        return getGoldenPoints(startx,starty,endx,endy,false);
    }

    public static PointFormula[] getGoldenPoints(float startx, float starty, float endx, float endy, boolean main){
        int index = 0;
        PointFormula gpoints[] = new PointFormula[goldennum-2];
        try {

            if(main == true) {
                gpoints = calcGoldenPoints(startx, starty, endx, endy);
            }


            else {
                PointFormula gpointsA[] = calcGoldenPoints(startx, starty, endx, endy);
                PointFormula gpointsB[] = calcGoldenPoints(startx, starty, gpointsA[2].x, gpointsA[2].y);
                PointFormula gpointsC[] = calcGoldenPoints(gpointsA[0].x, gpointsA[0].y, endx, endy);


              /*  PointFormula gpointsBb[] = calcGoldenPoints(startx,starty,gpointsB[2].x,gpointsB[2].y);
                PointFormula gpointsBc[] = calcGoldenPoints(gpointsB[0].x,gpointsB[0].y,gpointsA[2].x, gpointsA[2].y);


                PointFormula gpointsCb[] = calcGoldenPoints(gpointsA[0].x, gpointsA[0].y,gpointsC[2].x,gpointsC[2].y);
                PointFormula gpointsCc[] = calcGoldenPoints(gpointsC[0].x,gpointsC[0].y,endx,endy);*/


                Vector<Pair<PointFormula,Double>> distances = new Vector<>();

               /* for(int i = 0;i < gpointsBb.length-1;++i){
                    if (gpointsBb[i] != null) {
                        gpoints[index++] = gpointsBb[i];
                    } else {
                        int abc = 123;
                    }
                }*/
                for (int i = 0; i < gpointsB.length-1; ++i) {
                    if (gpointsB[i] != null) {
                        gpoints[index++] = gpointsB[i];
                    } else {
                        int abc = 123;
                    }
                }
                /*for(int i = 1;i < gpointsBc.length;++i){
                    if (gpointsBc[i] != null) {
                        gpoints[index++] = gpointsBc[i];
                    } else {
                        int abc = 123;
                    }
                }*/

                for (int i = 0; i < gpointsA.length; ++i) {
                    if (gpointsA[i] != null) {
                        gpoints[index++] = gpointsA[i];
                    } else {
                        int abc = 123;
                    }
                }

               /* for(int i = 0;i < gpointsCb.length-1;++i){
                    if (gpointsCb[i] != null) {
                        gpoints[index++] = gpointsCb[i];
                    } else {
                        int abc = 123;
                    }
                }*/
                for (int i = 1; i < gpointsC.length; ++i) {
                    if (gpointsC[i] != null) {
                        gpoints[index++] = gpointsC[i];
                    } else {
                        int abc = 123;
                    }
                }
                /*for(int i = 1;i < gpointsCc.length;++i){
                    if (gpointsCc[i] != null) {
                        gpoints[index++] = gpointsCc[i];
                    } else {
                        int abc = 123;
                    }
                }*/
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

                int abc = 123;
                abc+=34;
            }

        }
        catch (Exception e){
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
        return gpoints;

    }


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

    private static PointFormula[] calcGoldenPoints(float startx, float starty, float endx, float endy){
        PointFormula gpoints[] = new PointFormula[3];
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

        if(Maths.GetDistance(gx1,gy1,startx,starty) < Maths.GetDistance(gx2,gy2,startx,starty)) {
            gpoints[0] = new PointFormula((float)gx1, (float)gy1);
            gpoints[1]= new PointFormula((startx+endx)/2,(starty+endy)/2);
            gpoints[2] = new PointFormula((float)gx2, (float)gy2);
        }
        else{
            gpoints[0] = new PointFormula((float)gx2, (float)gy2);
            gpoints[1]= new PointFormula((startx+endx)/2,(starty+endy)/2);
            gpoints[2] = new PointFormula((float)gx1, (float)gy1);
        }
        return gpoints;
    }

    public static PointFormula[] getAllPoints(float startx,float starty,float endx,float endy, boolean main){

        PointFormula gpoints[] = null;
        gpoints = getGoldenPoints(startx,starty,endx,endy, main);

        PointFormula points[] = new PointFormula[gpoints.length+2];
        for(int i = 1; i < points.length-1; ++i){
            points[i] = gpoints[i-1];
        }
        points[0] = new PointFormula(startx,starty);
        points[points.length-1] = new PointFormula(endx,endy);
        return points;

    }
    public static PointFormula[] getAllPoints(float startx,float starty,float endx,float endy) {
        return getAllPoints(startx,starty,endx,endy,false);
    }

    public void  AddPointsOnLine(float startx, float starty, float endx, float endy){

        if(hp < harmoniousPoints.length){
            PointFormula gpoints[] = getGoldenPoints(startx,starty,endx,endy);
            for(int i = 0; i < gpoints.length; ++i){
                if(hp < harmoniousPoints.length){
                    harmoniousPoints[hp++] = gpoints[i];
                }
            }
        }
    }


    public GoldenPoints clone(){
        GoldenPoints gp  = new GoldenPoints(length);
        gp.hp = hp;

        for(int i = 0; i < hp; ++i){
            gp.harmoniousPoints[i] =new PointFormula(harmoniousPoints[i].x,harmoniousPoints[i].y);
        }
        return gp;
    }


}
