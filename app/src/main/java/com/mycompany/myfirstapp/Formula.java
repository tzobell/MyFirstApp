package com.mycompany.myfirstapp;

import android.util.Pair;

/**
 interface to be used by classes that describe a category of shapes or lines
 */
public interface Formula {

    Pair<Float,Float>[] GetKeyPoints();
    Pair<Float,Float> GetClosestPoint(float x,float y);
    public Pair<Float,Float> GetBasicClosestPoint(float x,float y);
    public Pair<Float,Float> GetBasicClosestPoint(Pair<Float,Float> p);
    Formula[] GetGoldenPoints();
    Pair<Float,Float> GetStart();
    Pair<Float,Float> GetEnd();
    boolean doesLineCross(LineFormula lf);
}
