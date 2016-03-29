package com.mycompany.ShapeSounds;

import android.util.Pair;

import java.util.HashMap;

/**
  Contains methods for finding and converting between notes, octaves and frequencies
 */
public class DiatonicScale {
    public static final int MAXNOTENUM= 88;//max note number
    public static  final double MAXFREQENCY = 1 * Math.pow(2,Octave.toInt(Octave.MAXOCTAVE));
    public static final int NOTESPEROCTAVE = 12;//number of notes in a octave
    public static final int NOTECOUNTOFFSET = 9;//offset for counting notes.
    public static final HashMap<Notes,Double> noteFrequencies; //contains pairs of Notes and the frequency of that note at octave 0
    public static final HashMap<Double,Notes> frequenciesNote; //contains pairs of freqencies and the note of that frequency
    public static final  Notes notes[] = {Notes.C, Notes.D, Notes.E,Notes.F,Notes.G,Notes.A,Notes.B};
     static{
         noteFrequencies = new HashMap<>();
         frequenciesNote = new HashMap<>();
         noteFrequencies.put(Notes.C,(1.0));
         noteFrequencies.put(Notes.D,(9.0/8.0));
         noteFrequencies.put(Notes.E,(5.0/4.0));
         noteFrequencies.put(Notes.F,(4.0/3.0));
         noteFrequencies.put(Notes.G,(3.0/2.0));
         noteFrequencies.put(Notes.A,(5.0/3.0));
         noteFrequencies.put(Notes.B,(15.0/8.0));
         frequenciesNote.put((1.0),Notes.C);
         frequenciesNote.put((9.0/8.0),Notes.D);
         frequenciesNote.put((5.0/4.0),Notes.E);
         frequenciesNote.put((4.0/3.0),Notes.F);
         frequenciesNote.put((3.0/2.0),Notes.G);
         frequenciesNote.put((5.0 / 3.0), Notes.A);
         frequenciesNote.put((15.0/8.0),Notes.B);

     }
    // 88 notes on piano from A0 to C8, A0=1....C8=88
    //so the note number the value of the note where Value(C) = 1...Value(B) = ValueofNote + (12 * octave) - 9 (sense octave 0 only has 3 notes)
    public static int NoteNum(Notes n,int octave){
        int val = 0;
        try {
            if (octave > Octave.MAXOCTAVEVAL || octave < Octave.MINOCTAVEVAL) {
                if (octave > Octave.MAXOCTAVEVAL) {
                    octave = Octave.MAXOCTAVEVAL;
                }
                if (octave < Octave.MINOCTAVEVAL) {
                    octave = Octave.MINOCTAVEVAL;
                }
            }
            int noteval = Notes.intVal(n);
            val = (noteval + (NOTESPEROCTAVE * (octave))) - NOTECOUNTOFFSET;
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return val;
    }
    
    public static int NoteNum(Notes n,Octave octave){
        return NoteNum(n, Octave.toInt(octave));
    }
   public static int NoteNum(Pair<Notes,Octave> n){
        return NoteNum(n.first, n.second);

    }
    //return the note that is one note higher than the note passed.
    //if note is B at the highest octave then it will return C at an octave of 0
    public static Pair<Notes,Octave> getHigherNote(Notes note, Octave octave){
        Notes higherN = Notes.C;
        try {
            //if note is C at octave 8, then this is the highest note, so get note D at octave 1
            if (note == Notes.C && octave == Octave.eight) {
                higherN = Notes.D;
                octave = Octave.one;
            } else {
                for (int i = 0; i < notes.length; ++i) {
                    Notes n = notes[i];
                    if (n == note) {
                        //if note is not B then (i+1) will be less than notes.length and the note below the note passed is just the next note in the notes array
                        if ((i + 1) < notes.length) {
                            higherN = notes[i + i];
                        }
                        //else the Note is B and the note lower than the note passed will be C at the next lowest octave
                        else {
                            higherN = notes[0];
                            octave = Octave.add(octave);
                        }
                        i = notes.length;
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return new Pair<>(higherN,octave);
    }

    //return the note that is one note lower than the note passed
    //if note is C at the lowest octave then it will return B at an octave of 8
    public static Pair<Notes,Octave> getLowerNote(Notes note, Octave octave){
        Notes lowerN = Notes.C;
        try {
            //if note is A at octave 0, then this is the lowest note, so get note D at octave 7
            if (note == Notes.A && octave == Octave.zero) {
                lowerN = Notes.D;
                octave = Octave.seven;
            }
            for (int i = 0; i < notes.length; ++i) {
                Notes n = notes[i];
                //if note is not C then (i-1) will be greater than 0 and the note above the note passed is just the previous note in the notes array
                if (n == note) {
                    if ((i - 1) >= 0) {
                        lowerN = notes[i - 1];
                    }
                    //else the Note is C and the note above than the note passed will be B at the previous octave
                    else {
                        lowerN = notes[notes.length - 1];
                        octave = Octave.sub(octave);
                    }
                    i = notes.length;
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return new Pair<>(lowerN,octave);
    }

    public static Notes getLowerNote(Notes note){
        return getLowerNote(note, Octave.zero).first;
    }

    public static Notes getHigherNote(Notes note){
        return getHigherNote(note, Octave.one).first;
    }
    public static Pair<Notes,Octave> getLowerNote(Pair<Notes,Octave> p){
        return getLowerNote(p.first, p.second);
    }
    public static Pair<Notes,Octave> getHigherNote(Pair<Notes,Octave> p){
        return getHigherNote(p.first, p.second);
    }

    //returns the frequency of the note below the note at the octave passed
    public static double getLowerNoteFrequency(Pair<Notes,Octave> p){
        Pair<Notes,Octave> p1  =  getLowerNote(p.first, p.second);
        return noteFrequencies.get(p1.first) * Math.pow(2,Octave.toInt(p1.second));
    }
    //returns the frequency of the note above the note at the octave passed
    public static double getHigherNoteFrequency(Pair<Notes,Octave> p){
        Pair<Notes,Octave> p1 =  getHigherNote(p.first, p.second);
        return noteFrequencies.get(p1.first) * Math.pow(2,Octave.toInt(p1.second));
    }

     //returns the frequency of the note at the octive in the Pair(note,octive) passed
    public static double getNoteFrequency(Pair<Notes,Octave> p){
        return noteFrequencies.get(p.first) * Math.pow(2,Octave.toInt(p.second));
    }

    //returns the note that the frequency passed corresponds to or is closest to
    public static Notes findNote(double frequency){
        return findOctave(frequency).first;
    }

    //returns the note that the frequency passed corresponds to or is closest to, and the octave that it is at
    public static Pair<Notes,Octave> findOctave(double frequency){
        Notes note = Notes.A;
        int oct = 0;
        Octave o = Octave.zero;
        try {
            double diff = Double.POSITIVE_INFINITY;
            //find the note that is closest to the frequency passed
            for (Notes n : notes) {
                double notefreq = noteFrequencies.get(n);

                double basefreq = frequency;
                int tempOctave = 0;
                //a note has a frequency twice that of the same note one octave lower
                //so dividing the basefreqency by 2 until its greater than or equal to the note frequency
                //increases the octave by one each time basefreq is divided by two
                while ((basefreq / 2) >= notefreq) {
                    basefreq = basefreq / 2;
                    ++tempOctave;
                }
                //if the difference between the  resulting basefreq  and notefreq is less than the currence lowest recorded difference then set dif
                //to tempDiff and note to notes[i] and oct to tempOctave
                double tempDiff = Math.abs(basefreq - notefreq);
                if (tempDiff < diff) {
                    diff = tempDiff;
                    note = n;
                    oct = tempOctave;
                }
            }

             o = Octave.toOctave(oct);
            int noteval = NoteNum(note, o);
            //if the note at octave o fallout outside of the range of  88 notes then get note at either an octave up or below so that it is a Note with in the range of 88 notes

        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        //return the note Octave pair that had the smallest difference
        return new Pair<>(note,o);
    }

    //return the radius (r) needed  for  ((pi* radius*radius)/ (pi*r*r)) == freqency of note
    public static float radiusCircle(Notes note,float radius ){
        double r = 0;
        try {
            switch (note) {
                //D = 9/8
                case D:
                    r = Math.sqrt(8) / Math.sqrt(9);
                    break;
                //E = 5/4
                case E:
                    r = Math.sqrt(4) / Math.sqrt(5);
                    break;
                //F = 4/3
                case F:
                    r = Math.sqrt(3) / Math.sqrt(4);
                    break;
                //G = 3/2
                case G:
                    r = Math.sqrt(2) / Math.sqrt(3);
                    break;
                //A = 5/3
                case A:
                    r = Math.sqrt(3) / Math.sqrt(5);
                    break;
                //B = 15/8
                case B:
                    r = Math.sqrt(8) / Math.sqrt(15);
                    break;
                //C = 2
                case C:
                    r = 1 / Math.sqrt(2);
                    break;
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return (float)(radius *r);
    }
}
