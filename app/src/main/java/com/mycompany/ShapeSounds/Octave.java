package com.mycompany.ShapeSounds;

/**
 enumeration for octaves
 */
public enum Octave {
    zero,one,two,three,four,five,six,seven,eight;

    public static final Octave octaves[] = { zero,one,two,three,four,five,six,seven,eight};
    public static final Octave middleOctave = four;
    public static final Octave MAXOCTAVE = eight;
    public static final Octave MINOCTAVE = zero;
    public static final int MAXOCTAVEVAL = 8;
    public static final int MINOCTAVEVAL = 0;
    //returns the Octave that is one higher than octave
    public static Octave add(Octave octave){
        Octave newoctave = Octave.zero;
        switch(octave){
            case zero:
                newoctave = one;
                break;
            case one:
                newoctave = two;
                break;
            case two:
                newoctave = three;
                break;
            case three:
                newoctave = four;
                break;
            case four:
                newoctave = five;
                break;
            case five:
                newoctave = six;
                break;
            case six:
                newoctave = seven;
                break;
            case seven:
                newoctave = eight;
                break;
            case eight:
                newoctave = zero;
                break;
        }
        return newoctave;
    }

    //returns the Octave that is one lower than octave
    public static Octave sub(Octave octave){
        Octave newoctave = Octave.zero;
        switch(octave){
            case zero:
                newoctave = eight;
                break;
            case one:
                newoctave = zero;
                break;
            case two:
                newoctave = one;
                break;
            case three:
                newoctave = two;
                break;
            case four:
                newoctave = three;
                break;
            case five:
                newoctave = four;
                break;
            case six:
                newoctave = five;
                break;
            case seven:
                newoctave = six;
                break;
            case eight:
                newoctave = seven;
                break;
        }
        return newoctave;
    }

    //convert from Octave to int
    public static int toInt(Octave octave){
        int intoct = 0;
        switch(octave){
            case zero:
                intoct = 0;
                break;
            case one:
                intoct = 1;
                break;
            case two:
                intoct = 2;
                break;
            case three:
                intoct = 3;
                break;
            case four:
                intoct = 4;
                break;
            case five:
                intoct = 5;
                break;
            case six:
                intoct = 6;
                break;
            case seven:
                intoct = 7;
                break;
            case eight:
                intoct = 8;
                break;
        }
        return intoct;
    }


    //convert from int to Octave
    public static Octave toOctave(int octave){
        Octave intoct = zero;
        octave = octave <=8? octave >=0?octave:0:8;
        switch(octave){
            case 0:
                intoct = zero;
                break;
            case 1:
                intoct = one;
                break;
            case 2:
                intoct = two;
                break;
            case 3:
                intoct = three;
                break;
            case 4:
                intoct = four;
                break;
            case 5:
                intoct = five;
                break;
            case 6:
                intoct = six;
                break;
            case 7:
                intoct = seven;
                break;
            case 8:
                intoct = eight;
                break;


        }
        return intoct;
    }

    //convert to Octave to string
    public static String toString(Octave octave) {
        String val = Integer.toString(toInt(octave));
        return val;
    }

}
