package com.mycompany.ShapeSounds;

import android.util.Pair;

import java.util.Vector;

//interface for Formula's that describe shapes
public interface ShapeFormula extends Formula {
    public double Area();

    public Pair<Boolean,ShapeFormula> AddShape(ShapeFormula shape);
    public void RemoveShape(ShapeFormula shape);
    public boolean inBounds(Formula shape);
    public boolean inBounds(Pair<Float,Float> p);
    public boolean inBounds(float x, float y);
    public boolean inCircumCircle(Pair<Float,Float> p);
    public boolean inCircumCircle(float x, float y);
    public boolean inCircumCircle(Formula shape);
    public ShapeFormula FindCircumShape(ShapeFormula shape);
    public ShapeFormula FindCircumShape(Pair<Float,Float> p);
    public ShapeFormula FindCircumShape(float x, float y);


    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y);
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y);
    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y,boolean lockintoKeyPoints);
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y,boolean lockintoKeyPoints);
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> pstart,Pair<Float,Float> p );
    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,Pair<Float,Float> p );
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> pstart,float x,float y );
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> p);

    public Pair<Float,Float> GetClosestToPerimeter(float x,float y);
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,float x,float y);
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> p);
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> pstart,float x,float y);
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,Pair<Float,Float> p);

    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float x,float y);
    public Pair<Float,Float> GetClosestPoint(float x,float y);
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> p);
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,Pair<Float,Float> p );
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,Pair<Float,Float> p );
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,float x,float y );




   // public void SetParent(ShapeFormula sf);

    public double getDiamater();
  //  public Formula[] GetGoldenLines();
    public void Play();

    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y);

    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y,float xend,float yend);
    //public Pair<Float,Float> getEndForArea(Pair<Float,Float> start,double area);
    public Vector<ShapeFormula> GetInsideShapes();
    public CircleFormula GetCircumCircle();

    public CircleFormula GetTangentCircle();
    public void AddConnectedShape(ShapeFormula sf);
    public Vector<ShapeFormula> GetConnectedShapes();
    //public void Play();


}
