package com.mycompany.ShapeSounds;

/**
enumeration for Notes on major scale
 */
public enum Notes {
    C,D,E,F,G,A,B;


    //convert from a note to an int
    public static int inVal(Notes note){
        int val = 1;
        /*C = 1   D = 3   E = 5  F = 6    G = 8   A = 10    B = 12*/
        switch (note){
            case C:
                val = 1;
                break;
            case D:
                val = 3;
                break;
            case E:
                val = 5;
                break;
            case F:
                val = 6;
                break;
            case G:
                val = 8;
                break;
            case A:
                val = 10;
                break;
            case B:
                val = 12;
                break;

        }
        return val;
    }


    //convert from note to a string
    public static String toString(Notes note){
        String val = "";
        /*C = 1  D = 3    E = 5  F = 6    G = 8    A = 10    B = 12*/
        switch (note){
            case C:
                val = "C";

                break;
            case D:
                val = "D";
                break;
            case E:
                val = "E";
                break;
            case F:
                val = "F";
                break;
            case G:
                val = "G";
                break;
            case A:
                val = "A";
                break;
            case B:
                val = "B";
                break;

        }
        return val;
    }

}
