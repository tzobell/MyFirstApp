package com.mycompany.ShapeSounds;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by Owner on 4/26/2016.
 */
public class DrawActionsClear implements DrawActions{


    private Vector<ShapeFormula> clearedShapes;

    public DrawActionsClear(Collection<ShapeFormula> shapes){
        try {
            clearedShapes = new Vector<>(shapes);
        }
        catch(Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }

    }

    public void execute(Vector<ShapeFormula> shapes){
       try {
           shapes.clear();
       }
       catch(Exception e){
           String a  = e.getMessage();
           System.out.println(a);
       }
    }
    public void undo(Vector<ShapeFormula> shapes){
        try {
            shapes.clear();
            shapes.addAll(clearedShapes);
        }
        catch(Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }
}
