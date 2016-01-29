package com.mycompany.ShapeSounds;

/**
 interface to be implemented by classes that draw formula's
 */
public interface GoldenShape {
    Formula[] GetGoldenPoints();
    Formula GetFormula();
    void DrawCircumCircle(boolean circumCircle);
    void DrawGoldenPoints(boolean drawGpoints);
}
