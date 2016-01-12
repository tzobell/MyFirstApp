package com.mycompany.myfirstapp;

/**
 * Created by Owner on 11/23/2015.
 */
public class ShapeSummary {
    public final float startx;
    public final float starty;
    public final float endx;
    public final float endy;
    public final ShapeType shape;
    public final Formula sf;
    public final ShapeFormula addedto;
    public final ShapeFormula startShape;

    public ShapeSummary(float startx, float starty,float endx,float endy,  ShapeType shape, Formula sf, ShapeFormula addedto,ShapeFormula startShape){
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.shape = shape;
        this.sf = sf;
        this.addedto = addedto;
        this.startShape = startShape;
    }

}
