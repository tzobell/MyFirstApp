package com.mycompany.ShapeSounds;

import android.util.Pair;

/**
 formula that described a point
 */
public class PointFormula implements Formula {
    public  float x,y;
    public PointFormula(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Pair<Float,Float> GetClosestPoint(float X,float Y){
        return new Pair<Float,Float>(x,y);
    }

    public Pair<Float,Float> GetBasicClosestPoint(float x,float y){
        return new Pair<Float,Float>(x,y);
    }
    public Pair<Float,Float> GetBasicClosestPoint(Pair<Float,Float> p){
        return new Pair<Float,Float>(x,y);
    }
    public Formula[] GetGoldenPoints(){
        return new Formula[]{this};
    }
    public Pair<Float,Float>[] GetKeyPoints(){
        //Pair<Float,Float>[] keypoints =new Pair[1];
       // keypoints[0] = new Pair<>(x,y);
        return new Pair[]{new Pair<>(x,y)};
    }

    public boolean doesLineCross(LineFormula lf){
        return lf.onTheLine(x,y);
    }
    
    public boolean equals(float X,float Y){
        return (x == X && y == Y);
    }
    public Pair<Float,Float> GetStart(){
        return new Pair<Float,Float>(x,y);
    }
    public Pair<Float,Float> GetEnd(){
        return new Pair<Float,Float>(x,y);
    }
}
