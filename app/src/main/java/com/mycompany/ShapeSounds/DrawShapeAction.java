package com.mycompany.ShapeSounds;

import java.util.Vector;

/**
 * Created by Owner on 4/26/2016.
 */
public class DrawShapeAction implements DrawActions {
    ShapeSummary shapeSum;
    DrawShapeAction(ShapeSummary shapeSummary){
        shapeSum = shapeSummary;
    }

    public void execute(Vector<ShapeFormula> shapes){
        Formula f = shapeSum.sf;
        if (f instanceof ShapeFormula) {
            Vector<ShapeFormula> associated = shapeSum.GetAssociatedShapes();
            if(shapeSum.addedto!=null){
                shapeSum.addedto.AddShape((ShapeFormula)f);
            }
            else{
                if(!shapes.contains(f)){
                    shapes.add((ShapeFormula)f);
                }
            }
            for (ShapeFormula shapeformula : associated) {
                shapeformula.AddConnectedShape((ShapeFormula) shapeSum.sf);
            }

        }

    }
    public void undo(Vector<ShapeFormula> shapes){
        Formula f = shapeSum.sf;
        if (f instanceof ShapeFormula) {
            Vector<ShapeFormula> associated = shapeSum.GetAssociatedShapes();
            for (ShapeFormula shapeformula : associated) {
                shapeformula.RemoveShape((ShapeFormula) shapeSum.sf);
            }
            int size = shapes.size();
            for (int i = 0; i < size; ++i) {
                if (shapes.get(i) == shapeSum.sf) {
                    shapes.remove(i);
                    i = size;
                }
            }
        }
    }
}
