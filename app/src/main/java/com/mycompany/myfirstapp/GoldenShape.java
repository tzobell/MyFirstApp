package com.mycompany.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;

/**
 * Created by Owner on 9/11/2015.
 */
public interface GoldenShape {

    public Formula[] GetGoldenPoints();
    //public Formula[] GetGoldenLines();
    public Formula GetFormula();
    public void DrawCircumCircle(boolean circumCircle);
    public void DrawGoldenPoints(boolean drawGpoints);

}
