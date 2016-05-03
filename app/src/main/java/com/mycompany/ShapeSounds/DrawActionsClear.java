package com.mycompany.ShapeSounds;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by Owner on 4/26/2016.
 */
public class DrawActionsClear implements DrawActions{

    private Vector<DrawActions> shapeHistory;

    public DrawActionsClear(Collection<DrawActions> shapehistory){
        shapeHistory = new Vector<>(shapehistory);

    }

    public void execute(Vector<ShapeFormula> shapes){
        Collections.reverse(shapeHistory);
        for(DrawActions da:shapeHistory){
            da.undo(shapes);
        }
        Collections.reverse(shapeHistory);
    }
    public void undo(Vector<ShapeFormula> shapes){
        for(DrawActions da:shapeHistory){
            da.execute(shapes);
        }
    }
}
