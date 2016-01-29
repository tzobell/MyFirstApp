package com.mycompany.ShapeSounds;

import android.util.Pair;

/**
 holds a pair of floats
 */
public class Fpoint extends Pair<Float,Float> {

    public Fpoint(float first,float second){
        super(first,second);

    }
    public Fpoint(double first,double second){
        super((float)first,(float)second);
    }
    public Fpoint(int first,int second){
        super((float)first,(float)second);
    }
}
