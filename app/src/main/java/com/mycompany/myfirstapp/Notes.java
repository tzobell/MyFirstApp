package com.mycompany.myfirstapp;

/**
 * Created by Owner on 9/17/2015.
 */
public enum Notes {
    C,D,E,F,G,A,B;


    public static int inVal(Notes note){
        int val = 1;
        /*C = 1 Cm = 2  D = 3  Dm = 4  E = 5  F = 6  Fm = 7  G = 8  Gm = 9  A = 10  Am = 11  B = 12*/
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


    public static String toString(Notes note){
        String val = "";
        /*C = 1 Cm = 2  D = 3  Dm = 4  E = 5  F = 6  Fm = 7  G = 8  Gm = 9  A = 10  Am = 11  B = 12*/
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
