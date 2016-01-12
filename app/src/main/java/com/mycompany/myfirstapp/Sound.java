package com.mycompany.myfirstapp;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Owner on 9/23/2015.
 */
public class Sound {
    private Map<Integer,MediaPlayer> mp;
    private Map<Integer,Integer> audiofiles;
    private Queue<Pair<Notes,Octave>> noteQ;


    Context context;
    //private  Notes n[] = {Notes.C,Notes.D,Notes.E,Notes.F,Notes.G,Notes.A,Notes.B};
    private static Sound sound = null;
    private static boolean play = false;


    public static final int MAXNOTENUM= 88;
    public static final int NOTESPEROCTAVE = 12;
    public static final int NOTECOUNTOFFSET = 9;
    private Sound(Context context){
        this.context = context;
        noteQ = new LinkedList<Pair<Notes,Octave>>();
        audiofiles = new HashMap<Integer,Integer>();
        mp = new HashMap<Integer,MediaPlayer>();
        for(Notes note:DiatonicScale.notes){
            for(Octave octave:Octave.octaves){
                int noteval = intNoteNum(note,octave);
                if(noteval >= 0 && noteval <= MAXNOTENUM){
                    String noteString = NoteToString(note,octave);
                    Resources res = context.getResources();
                    int soundFileId = res.getIdentifier(noteString, "raw", context.getPackageName());
                    audiofiles.put(noteval,soundFileId);
                    mp.put(noteval,MediaPlayer.create(context,soundFileId));
                }
            }
        }
    }



    private void initNotes(){
        try {

            mp = new HashMap<Integer,MediaPlayer>();
            for(Notes note:DiatonicScale.notes){
                for(Octave octave:Octave.octaves){
                    int noteval = intNoteNum(note,octave);
                    if(noteval >= 0 && noteval <= MAXNOTENUM){
                        int soundFileId = audiofiles.get(noteval);
                        mp.put(noteval,MediaPlayer.create(context,soundFileId));
                    }
                }
            }


        }
        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
    }





    private void releaseNotes(){
        try {
            for(Notes note:DiatonicScale.notes){
                for(Octave octave:Octave.octaves){
                    int noteval = intNoteNum(note,octave);
                    if(noteval >= 0 && noteval <= MAXNOTENUM){
                        mp.get(noteval).release();

                    }
                }
            }

            mp = null;
        }
        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
    }

    private static int intNoteNum(Pair<Notes,Octave> n){
        return intNoteNum(n.first, n.second);

    }


    // 88 notes on piano from A0 to C8, A0=1....C8=88
    //so the note number the value of the note where Value(C) = 1...Value(B) = ValueofNote + (12 * octave) - 9 (sense octave 0 only has 3 notes)
    private static int intNoteNum(Notes n,Octave octave){
        int noteval = Notes.inVal(n);

        int  notenum =  (noteval + (NOTESPEROCTAVE *(Octave.toInt(octave)))) - NOTECOUNTOFFSET;
        return notenum;

    }
    private static String NoteToString(Pair<Notes,Octave> n){
        return NoteToString(n.first,n.second);
    }
    private static String NoteToString(Notes n, Octave octave){
        String s  = Notes.toString(n).toLowerCase() + Octave.toString(octave);
        return s;



    }

    private void renewNote(Pair<Notes,Octave> n){
        renewNote(n.first,n.second);
    }
    private void renewNote(Notes note, Octave octave){
        try {
            int noteval = intNoteNum(note,octave);
            mp.get(noteval).release();
            mp.remove(noteval);
            mp.put(noteval, MediaPlayer.create(context, audiofiles.get(noteval)));
        }
        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
    }

    public static void playAll() {
        try {
            if (sound != null && sound.noteQ.size() > 0) {
                int ms = (sound.noteQ.size() + 1) * 1000;
                CountDownTimer cd = new CountDownTimer(ms, 1000) {
                    boolean started = false;
                    Pair<Notes,Octave> note = null;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        try {

                            if (note != null) {
                                int noteval = intNoteNum(note);
                                sound.mp.get(noteval).pause();
                                sound.mp.get(noteval).seekTo(0);
                                sound.renewNote(note);
                            }
                            note = sound.noteQ.poll();
                            if (note != null) {
                                int noteval = intNoteNum(note);
                                sound.mp.get(noteval).start();
                            }
                        } catch (Exception e) {
                            String a = e.getMessage();
                            System.out.println(e.getMessage());
                        }
                    }

                    @Override
                    public void onFinish() {
                        try {
                            if(note!=null) {
                                sound.mp.get(intNoteNum(note)).pause();
                            }
                            sound.releaseNotes();
                            sound.initNotes();
                            sound.noteQ.clear();
                        }
                        catch (Exception e) {
                            String a = e.getMessage();
                            System.out.println(e.getMessage());
                        }

                    }
                };
                cd.start();
            }
        }
       catch (Exception e) {
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
    }
    public static void initSound(Context context){
        if(sound == null){
            sound = new Sound(context);
        }
    }

    public static void AddNote(Notes note) {
        AddNote(note, Octave.middleOctave);
    }
    public static void AddNote(Pair<Notes,Octave> note) {
        AddNote(note.first,note.second);
    }

    public static void AddNote(Notes note, Octave octave){
        try {
            sound.noteQ.add(new Pair<>(note,octave));
        }
        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(e.getMessage());
        }
    }
}
