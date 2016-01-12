package com.mycompany.myfirstapp;

import android.graphics.Point;
import android.util.Pair;

import java.util.List;
import java.util.Vector;


/**
 * Created by Owner on 8/31/2015.
 */
public class PointTree {

    PointNode head = null;

    Vector<Formula> points = new Vector<Formula>();
    public PointTree(){}

    public PointTree(float height, float width){
        head = new PointNode(new Point(Math.round(width/2), Math.round(height/2)) );
    }


    public void Add(Formula newpoint){
        points.add(newpoint);

    }


    public Pair<Float,Float> Search(float x, float y){
        Pair<Float,Float> closest = new Pair<Float,Float> (x,y);
        Pair<Float,Float> point = new Pair(x,y);
        double distance = Double.POSITIVE_INFINITY;
        for(Formula fpoint:points){
            Pair<Float,Float>p = fpoint.GetClosestPoint(x,y);
            double tempdis = Maths.GetDistance(p,point);
            if(tempdis < distance){
                distance = tempdis;
                closest = new Pair<Float,Float> ( p.first,p.second);
            }
        }
        return closest;
        //return Search(new Point(Math.round(x),Math.round(y)));
    }




    /*

       private void Add(Point point, PointNode pointnode) {

        int x1 = point.x;
        int y1 = point.y;
        int x = pointnode.getPoint().x;
        int y = pointnode.getPoint().y;

        NodeDirection t;
        if (x1 != x && y1 != y) {
            //north
            if (y >= y1) {
                //northeast
                if (x <= x1) {
                    t = NodeDirection.NorthEast;
                }
                //northwest
                else {
                    t = NodeDirection.NorthWest;
                }
            }
            //south
            else {
                //southeast
                if (x <= x1) {
                    t = NodeDirection.SouthEast;
                }
                //southwest
                else {
                    t = NodeDirection.SouthWest;
                }
            }

            if (pointnode.insert(t, point) == false) {
                Add(point, pointnode.getNode(t));
            }
        }
    }
    public void Add(Point point){
        if(head==null){
            head = new PointNode(point);
        }
        else{
            Add(point, head);
        }
    }

    public Point Search(Point point){
        Point closest = new Point(head.value.x,head.value.y);
        double distance = PointNode.GetDistance(point, closest);
        PointSearch(point, closest,distance, head);
        return closest;
    }
    private void PointSearch(Point point,Point closestsofar,double curdis, PointNode pointnode){

        int x1 = point.x;
        int y1 = point.y;
        int x = pointnode.getPoint().x;
        int y = pointnode.getPoint().y;
        NodeDirection t;
        //north
        if(y>=y1){
            //northeast
            if(x<=x1){
                t = NodeDirection.NorthEast;
            }
            //northwest
            else{
                t =  NodeDirection.NorthWest;
            }
        }
        //south
        else{
            //southeast
            if(x<=x1){
                t = NodeDirection.SouthEast;
            }
            //southwest
            else{
                t =  NodeDirection.SouthWest;
            }
        }

        PointNode tnode = pointnode.getNode(t);
        if(tnode != null){
            double tempdis = PointNode.GetDistance(point, tnode.value);
            if(curdis > tempdis){
                closestsofar.x = tnode.value.x;
                closestsofar.y = tnode.value.y;
                PointSearch(point,closestsofar,tempdis,tnode);
            }
        }
    }
    */
}
