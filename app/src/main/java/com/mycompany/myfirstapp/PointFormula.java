package com.mycompany.myfirstapp;

import android.util.Pair;

import com.mycompany.myfirstapp.Formula;

/**
 * Created by Owner on 9/17/2015.
 */
public class PointFormula implements Formula {
    public final float x,y;
    public PointFormula(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Pair<Float,Float> GetClosestPoint(float X,float Y){
        return new Pair<Float,Float>(x,y);
    }

    public Formula[] GetGoldenPoints(){
        Formula[] f = {this};
        return f;
    }
    public Pair<Float,Float>[] GetKeyPoints(){
        Pair<Float,Float>[] keypoints =new Pair[1];
        keypoints[0] = new Pair<Float,Float>(x,y);
        return keypoints;
    }

    public boolean doesLineCross(LineFormula lf){
        return lf.onTheLine(x,y);
    }
    public boolean equals(Pair<Float,Float> p){
        return equals(p.first,p.second);
    }
    public boolean equals(PointFormula p){
        return equals(p.x,p.y);
    }
    public boolean equals(float X,float Y){
        return (x == X && y == Y);
    }

    public Pair<Float,Float> GetStart(){
        return new Pair<>(x,y);
    }
    public Pair<Float,Float> GetEnd(){
        return new Pair<>(x,y);
    }
}
