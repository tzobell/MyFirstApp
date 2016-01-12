package com.mycompany.myfirstapp;

import android.graphics.Point;
import android.util.Pair;

import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Owner on 9/2/2015.
 */

public class PointNode {
    Point value;
    Map<NodeDirection,PointNode> nodes = new HashMap<NodeDirection,PointNode>();
    public static final NodeDirection[] nodedirections = {NodeDirection.NorthEast,NodeDirection.NorthWest,NodeDirection.SouthEast,NodeDirection.SouthWest};
    public PointNode(Point p){
        value = p;

        nodes.put(NodeDirection.NorthEast,null);
        nodes.put(NodeDirection.NorthWest,null);
        nodes.put(NodeDirection.SouthEast,null);
        nodes.put(NodeDirection.SouthWest,null);

    }
    public Point getPoint(){
        return value;
    }

    private void setNortheast(PointNode Northeast) {  nodes.put(NodeDirection.NorthEast,Northeast);}
    private void setSoutheast(PointNode Southeast){nodes.put(NodeDirection.SouthEast,Southeast);}
    private void setSouthwest(PointNode Southwest){nodes.put(NodeDirection.SouthWest,Southwest);}
    private void setNorthwest(PointNode Northwest){nodes.put(NodeDirection.NorthWest,Northwest);}

    public PointNode getNortheast(){       return nodes.get(NodeDirection.NorthEast);   }
    public PointNode getSoutheast(){       return nodes.get(NodeDirection.SouthEast);     }
    public PointNode getSouthwest(){        return nodes.get(NodeDirection.SouthWest);      }
    public PointNode getNorthwest(){        return nodes.get(NodeDirection.NorthWest);      }


    public PointNode getNode(NodeDirection nd){
        return nodes.get(nd);
    }

    private void setNode(NodeDirection nd, PointNode pn){
        nodes.put(nd,pn);

    }
    private void setNode(NodeDirection nd, Point pn){
        nodes.put(nd,new PointNode(pn));
    }



    //test if node is at direction nd is null or a further distance from value
    //than Point pn. If Pn is closer to value, then insert pn at direction nd.
    public boolean insert(NodeDirection nd, Point p){
        PointNode pn = new PointNode(p);
        return insert(nd, pn);
    }

private boolean AllNull(){
    return (nodes.get(NodeDirection.NorthEast) == null && nodes.get(NodeDirection.NorthWest) == null && nodes.get(NodeDirection.SouthEast) == null && nodes.get(NodeDirection.SouthWest) == null);
}
    private boolean insert(NodeDirection nd, PointNode pn){
        Double d = new Double(0.0);
        Pair<Boolean, Double> caninsert;
        if(nodes.get(nd) != null) {
            caninsert = insertTest(nd, pn.getPoint());
        }
        else{
            caninsert = new Pair(true,Maths.GetDistance(value,pn.getPoint()));
        }
        if(caninsert.first == true){
           /* PointNode temp = getNode(nd);
            PointNode n = new PointNode(pn);
            n.setNode(nd, temp);
            setNode(nd, n);*/
            boolean isnull = pn.AllNull();
            if(getNode(nd)!= null){
                pn.insert(nd, getNode(nd));
                getNode(nd).insert(pn);
            }


            setNode(nd, pn);
            for(int i = 0; i < nodedirections.length; ++i){
                NodeDirection tnd = nodedirections[i];
                if(tnd != nd && nodes.get(tnd)!= null){
                    nodes.get(tnd).insert(pn);
                }
            }


        }
        return caninsert.first;
    }

    public boolean insert(PointNode pn){



        int x1 = pn.getPoint().x;
        int y1 = pn.getPoint().y;
        int x = value.x;
        int y = value.y;
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
        return insert(t,pn);
    }


    //connect new node with value pn to existing nodes
    private void insertNode(NodeDirection nd, PointNode pn, Pair<Boolean, Double> caninsert){

        NodeDirection a = NodeDirection.NorthEast;
        NodeDirection b = NodeDirection.SouthWest;
        NodeDirection c = NodeDirection.NorthWest;




        Vector<NodeDirection> v;
        switch (nd){
            case SouthEast:
                break;
            case SouthWest:
                a = NodeDirection.NorthWest;
                b = NodeDirection.SouthEast;
                c = NodeDirection.NorthEast;
                break;
            case NorthEast:
                a = NodeDirection.SouthEast;
                b = NodeDirection.NorthWest;
                c = NodeDirection.SouthWest;
                break;
            case NorthWest:
                a = NodeDirection.SouthWest;
                b = NodeDirection.NorthEast;
                c = NodeDirection.SouthEast;
                break;
        }

        v = new Vector<NodeDirection>(2);
        if(nodes.get(a) == null || pn.getPoint().x<= nodes.get(a).value.x) {
            pn.setNode(a, nodes.get(a));
        }
        else{
            v.addElement(a);
        }

        if(nodes.get(b) == null || pn.getPoint().y < nodes.get(b).value.y) {
            pn.setNode(b, nodes.get(b));
        }
        else{
            v.addElement(b);
        }
        if(v.size() == 0){
            pn.setNode(c, this);
        }
        else{
            PointNode nw = this;
            double dis = caninsert.second;
            for(int i = 0; i < v.size(); ++i){
                double tempdis = Maths.GetDistance(pn.getPoint(), nodes.get(v.get(i)).value);
                if(tempdis < dis){
                    nw = nodes.get(v.get(i));
                }
            }
            pn.setNode(c,nw); }


    }




    private Pair<Boolean,Double> insertTest(NodeDirection nd, Point test){

        Pair<Boolean,Double> caninsert = new Pair<Boolean,Double>(new Boolean(false),new Double(0));
        Point p = getNode(nd).getPoint();
        double distanceTest = Maths.GetDistance(value,test);
        double distance = Maths.GetDistance(value,p);
        if(distanceTest < distance){
            caninsert = new Pair<Boolean,Double>(new Boolean(true),new Double(distanceTest));
        }
        return caninsert;
    }



}

