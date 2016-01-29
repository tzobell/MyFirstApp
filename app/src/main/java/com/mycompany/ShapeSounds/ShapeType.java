package com.mycompany.ShapeSounds;

/**
 enumeration for different types of shapes and methods
 */
public enum ShapeType {

    circle,square, triangle, line, pentagon,hexagon,heptagon,octagon,nonagon,decagon,hendecagon,dodecagon;
    private static int MaxNum = 12;
    //returns the number of points each shape has.
    public static int GetNumberOfPoints(ShapeType shape) {
        int polyPoints = 0;
        switch (shape) {
            case circle:
                polyPoints = 0;
                break;
            case line:
                polyPoints = 2;
                break;
            case triangle:
                polyPoints = 3;
                break;

            case square:
                polyPoints = 4;
                break;

            case pentagon:
                polyPoints = 5;
                break;

            case hexagon:
                polyPoints = 6;
                break;

            case heptagon:
                polyPoints = 7;
                break;

            case octagon:
                polyPoints = 8;
                break;

            case nonagon:
                polyPoints = 9;
                break;

            case decagon:
                polyPoints = 10;
                break;

            case hendecagon:
                polyPoints = 11;
                break;

            case dodecagon:
                polyPoints = 12;
                break;
        }
        return polyPoints;
    }

    //returns the ShapeType that has polyPoints number of points
    //if polyPoints is less than 0 or 1 then change polyPoints to 0
    //if polyPoints is greater than MaxNum then change polyPoints to MaxNum
    public static ShapeType GetShapeType(int polyPoints){
        ShapeType st = null;
        if(polyPoints < 0 || polyPoints == 1){
            polyPoints = 0;
        }
        if(polyPoints > MaxNum){
            polyPoints = MaxNum;
        }
        switch (polyPoints){
            case 0:
                st = ShapeType.circle;
                break;
            case 2:
                st = ShapeType.line;
                break;
            case 3:
                st = ShapeType.triangle;
                break;
            case 4:
                st = ShapeType.square;
                break;
            case 5:
                st = ShapeType.pentagon;
                break;
            case 6:
                st = ShapeType.hexagon;
                break;
            case 7:
                st = ShapeType.heptagon;
                break;
            case 8:
                st = ShapeType.octagon;
                break;
            case 9:
                st = ShapeType.nonagon;
                break;
            case 10:
                st = ShapeType.decagon;
                break;
            case 11:
                st = ShapeType.hendecagon;
                break;
            case 12:
                st = ShapeType.dodecagon;
                break;
        }



        return st;
    }
}
