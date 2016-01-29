package com.mycompany.myfirstapp;


import java.util.Vector;

public class ShapeFormulaSummary extends  ShapeSummary {

    public final ShapeFormula shapeformula;

    public ShapeFormulaSummary(float startx, float starty,float endx,float endy,  ShapeType shape, ShapeFormula sf, ShapeFormula addedto,ShapeFormula startShape){
        super(startx,starty,endx,endy,shape,sf,addedto,startShape);
        shapeformula = sf;

    }

    @Override
    public Vector<ShapeFormula> GetAssociatedShapes(){
        Vector<ShapeFormula> accociated = shapeformula.GetConnectedShapes();
        accociated.add(addedto);
        return accociated;
    }


}