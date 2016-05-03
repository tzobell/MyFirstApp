package com.mycompany.ShapeSounds;

import java.util.Vector;

/**
 * Created by Owner on 4/26/2016.
 */
public interface DrawActions {

    void execute(Vector<ShapeFormula> shapes);
    void undo(Vector<ShapeFormula> shapes);

}
