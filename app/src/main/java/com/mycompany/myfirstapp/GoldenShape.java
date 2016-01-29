package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;

/**
 interface to be implemented by classes that draw formula's
 */
public interface GoldenShape {
    Formula[] GetGoldenPoints();
    Formula GetFormula();
    void DrawCircumCircle(boolean circumCircle);
    void DrawGoldenPoints(boolean drawGpoints);
}
