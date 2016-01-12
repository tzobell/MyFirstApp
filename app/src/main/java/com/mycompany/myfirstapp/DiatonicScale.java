package com.mycompany.myfirstapp;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 9/17/2015.
 */
public class DiatonicScale {

    //find the radius of the enscribed circle (rec) in the radius of the passed circle (rpc)
    // that would make area(rpc)/area(rec) == note
    public static final HashMap<Notes,Double> noteFrequencies;
    public static final HashMap<Double,Notes> frequenciesNote;
    //public static final  Notes notes[] = {Notes.C,Notes.Cm, Notes.D,Notes.Dm, Notes.E,Notes.F,Notes.Fm, Notes.G,Notes.Gm, Notes.A,Notes.Am, Notes.B};
    public static final  Notes notes[] = {Notes.C, Notes.D, Notes.E,Notes.F,Notes.G,Notes.A,Notes.B};
     static{
         noteFrequencies = new HashMap<Notes,Double>();
         frequenciesNote = new HashMap<Double,Notes>();


         noteFrequencies.put(Notes.C,(1.0));
         //C# = 16/15
        // noteFrequencies.put(Notes.Cm,(16.0/15.0));
         noteFrequencies.put(Notes.D,(9.0/8.0));
         // //D# = 6/5
        // noteFrequencies.put(Notes.Dm,(6.0/5.0));
         noteFrequencies.put(Notes.E,(5.0/4.0));
         noteFrequencies.put(Notes.F,(4.0/3.0));
         //  //F# 45/32
        // noteFrequencies.put(Notes.Fm,(45.0/32.0));
         noteFrequencies.put(Notes.G,(3.0/2.0));
         // // G#= 8/5
        // noteFrequencies.put(Notes.Gm,(8.0/5.0));
         noteFrequencies.put(Notes.A,(5.0/3.0));
         //  //A# = 9/5
         //noteFrequencies.put(Notes.Am,(9.0/5.0));
         noteFrequencies.put(Notes.B,(15.0/8.0));





         // C# =
         frequenciesNote.put((1.0),Notes.C);
         //frequenciesNote.put((16.0/15),Notes.Cm);
         frequenciesNote.put((9.0/8.0),Notes.D);
         // //D# = 6/5
         //frequenciesNote.put((6.0/5.0),Notes.Dm);
         frequenciesNote.put((5.0/4.0),Notes.E);
         frequenciesNote.put((4.0/3.0),Notes.F);
         //  //F# 45/32
         //frequenciesNote.put((45.0/32.0),Notes.Fm);
         frequenciesNote.put((3.0/2.0),Notes.G);
         // G#= 8/5
        // frequenciesNote.put((8.0/5.0),Notes.Gm);
         frequenciesNote.put((5.0 / 3.0), Notes.A);
         //A# = 9/5
         //frequenciesNote.put((9.0/5.0),Notes.Am);
         frequenciesNote.put((15.0/8.0),Notes.B);

     }//  Notes n[] = {Notes.C,Notes.D,Notes.E,Notes.F,Notes.G,Notes.A,Notes.B};




    //return the note that is above or below the note passed and the octive the upper/lower note is in
    public static Pair<Notes,Octave> getLowerNote(Notes note, Octave octave){
        Notes lowerN = Notes.C;

        for(int i = 0; i < notes.length; ++i){
            Notes n = notes[i];

            if(n == note){
                if((i+1) < notes.length){
                    lowerN = notes[i+i];
                }
                else{
                    lowerN = notes[0];
                    octave = Octave.add(octave);
                }
                i = notes.length;
            }
        }

        return new Pair<Notes,Octave>(lowerN,octave);
    }

    public static Pair<Notes,Octave> getUpperNote(Notes note, Octave octave){
        Notes upperN = Notes.C;

        for(int i = 0; i < notes.length; ++i){
            Notes n = notes[i];

            if(n == note){
                if((i-1) >= 0){
                    upperN = notes[i-i];
                }
                else{
                    upperN = notes[notes.length -1];
                    octave = Octave.sub(octave);
                }
                i = notes.length;
            }
        }

        return new Pair<Notes,Octave>(upperN,octave);
    }



    public static Notes getLowerNote(Notes note){
        return getLowerNote(note,Octave.zero).first;
    }

    public static Notes getUpperNote(Notes note){

        return getUpperNote(note, Octave.one).first;
    }
    public static Pair<Notes,Octave> getLowerNote(Pair<Notes,Octave> p){
        return getLowerNote(p.first,p.second);
    }
    public static Pair<Notes,Octave> getUpperNote(Pair<Notes,Octave> p){
        return getUpperNote(p.first, p.second);
    }

    //returns the frequency of the note above/below the (note,octive) passed
    public static double getLowerNoteFrequency(Pair<Notes,Octave> p){
        Pair<Notes,Octave> p1  =  getLowerNote(p.first,p.second);
        double freq = noteFrequencies.get(p1.first) * Math.pow(2,Octave.toInt(p1.second));
        return freq;
    }
    public static double getUpperNoteFrequency(Pair<Notes,Octave> p){
        Pair<Notes,Octave> p1 =  getUpperNote(p.first, p.second);
        double freq = noteFrequencies.get(p1.first) * Math.pow(2,Octave.toInt(p1.second));
        return freq;
    }


    //returns the frequency of the note at the octive in the Pair(note,octive) passed
    public static double getNoteFrequency(Pair<Notes,Octave> p){
        double freq = noteFrequencies.get(p.first) * Math.pow(2,Octave.toInt(p.second));
        return freq;
    }






    public static Notes findNote(double frequency){

        return findOctive(frequency).first;
    }

    public static Pair<Notes,Octave> findOctive(double frequency){
        Notes note = Notes.A;
        int octive = 0;

        double diff = Double.POSITIVE_INFINITY;
        for(int i = 0; i < notes.length; ++i){
            double notefreq = noteFrequencies.get(notes[i]);

            double basefreq = frequency;
            int tempOctive = 0;
            while((basefreq/2) >= notefreq){
                basefreq=basefreq/2;
                ++tempOctive;
            }
            double tempDiff =   Math.abs(basefreq - notefreq);
            if(tempDiff < diff){
                diff = tempDiff;
                note = notes[i];
                octive = tempOctive;

            }
        }

        return new Pair<Notes,Octave>(note,Octave.toOctave(octive));
    }

    public static float radiusCircle(Notes note,float radius ){
        double r = 0;
        switch (note){
            //  //C# = 16/15
           /* case Cm:
                r = Math.sqrt(15)/Math.sqrt(16);
                break;*/
            //D = 9/8
            case D:
                r = Math.sqrt(8)/Math.sqrt(9);
                break;

            //D# = 6/5
            /* case Dm:
                r = Math.sqrt(5)/Math.sqrt(6);
                break;*/
            //E = 5/4
            case E:
                r = Math.sqrt(4)/Math.sqrt(5);
                break;
            //F = 4/3
            case F:
                r = Math.sqrt(3)/Math.sqrt(4);
                break;
            //F# 45/32
            /*case Fm:
                r = Math.sqrt(32)/Math.sqrt(45);
                break;*/
            //G = 3/2
            case G:
                r = Math.sqrt(2)/Math.sqrt(3);
                break;
            // G#= 8/5
           /* case Gm:
                r = Math.sqrt(5)/Math.sqrt(8);
                break;*/
            //A = 5/3
            case A:
                r = Math.sqrt(3)/Math.sqrt(5);
                break;
            //A# = 9/5
            /*case Am:
                r = Math.sqrt(5)/Math.sqrt(9);
                break;*/
            //B = 15/8
            case B:
                r = Math.sqrt(8)/Math.sqrt(15);
                break;
            //C = 2
            case C:
                r = 1/Math.sqrt(2);
                break;
        }
        float tempR = (float) (radius*r);
        return (float)(radius *r);
    }
}
