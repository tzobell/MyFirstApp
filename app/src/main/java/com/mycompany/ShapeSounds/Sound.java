package com.mycompany.ShapeSounds;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Pair;
import android.util.SparseIntArray;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/*
 handles the playing of different notes
 */
public class Sound {
    private Map<Integer,MediaPlayer> mp;//map of notevalues to sound
    //private Map<Integer,Integer> audiofiles;
    private SparseIntArray audiofiles; //map of note values to sound number files
    private Queue<Pair<Notes,Octave>> noteQ; //queue of notes to play

    Context context;
    private static Sound sound = null; //for singleton
    private Octave highestOctave;
    private Octave lowestOctave;
   
    private Sound(Context context){
        this.context = context;
        noteQ = new LinkedList<>();
        //audiofiles = new HashMap<Integer,Integer>();
        audiofiles = new SparseIntArray(DiatonicScale.notes.length);
        mp = new HashMap<>();
        highestOctave = null;
        lowestOctave = null;

        //find the sound file index in the raw folder that corresponds to each note value to fill in audiofiles array and mp map
        for(Notes note:DiatonicScale.notes){
            for(Octave octave:Octave.octaves){
                
                int noteval = DiatonicScale.NoteNum(note,octave);
                if(noteval >= 0 && noteval <= DiatonicScale.MAXNOTENUM){
                    String noteString = NoteToString(note,octave);
                    Resources res = context.getResources();
                    int soundFileId = res.getIdentifier(noteString, "raw", context.getPackageName());
                    audiofiles.put(noteval,soundFileId);
                    mp.put(noteval,MediaPlayer.create(context,soundFileId));
                }
            }
        }
    }

    //re-initilize mp map
    private void initNotes(){
        try {
            mp = new HashMap<>();
            //find the sound file index in the raw folder that corresponds to each note value to fill in mp map
            for(Notes note:DiatonicScale.notes){
                for(Octave octave:Octave.octaves){
                    int noteval = DiatonicScale.NoteNum(note,octave);
                    if(noteval >= 0 && noteval <= DiatonicScale.MAXNOTENUM){
                        int soundFileId = audiofiles.get(noteval);
                        mp.put(noteval, MediaPlayer.create(context, soundFileId));
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //releases notes from mp Map
    private void releaseNotes(){
        try {
            for(Notes note:DiatonicScale.notes){
                for(Octave octave:Octave.octaves){
                    int noteval = DiatonicScale.NoteNum(note,octave);
                    if(noteval >= 0 && noteval <= DiatonicScale.MAXNOTENUM){
                        mp.get(noteval).release();
                    }
                }
            }
            mp = null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

  

    //returns the string value of Note n at Octave octave
    private static String NoteToString(Notes n, Octave octave){
        return  Notes.toString(n).toLowerCase() + Octave.toString(octave);
    }

    //removes Notes note at Octave octave from Map mp and then puts in back in
    private void renewNote(Notes note, Octave octave){
        try {
            int noteval = DiatonicScale.NoteNum(note,octave);
            mp.get(noteval).release();
            mp.remove(noteval);
            mp.put(noteval, MediaPlayer.create(context, audiofiles.get(noteval)));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void renewNote(Pair<Notes,Octave> n){
        renewNote(n.first,n.second);
    }


    //centers the range of notes in the noteQ around the middle octave as much as possible
    private void EqualizeNoteQueue(){
        int octaveOffset = 0;
        int octaveMid = (int)Maths.Middle(Octave.toInt(lowestOctave),Octave.toInt(highestOctave));
        octaveOffset = Octave.MIDOCTAVEVAL - octaveMid;
        Queue<Pair<Notes,Octave>> tempnoteQ = new LinkedList<>();
        for(Pair<Notes,Octave> n: noteQ) {
            Notes note = n.first;
            Octave octave = Octave.toOctave(Octave.toInt(n.second) + octaveOffset);
            int noteval = DiatonicScale.NoteNum(note, octave);
            //if note at the Octave octave lies outside of the range of notes available, then adjust the Octave so that it is within the range of notes available
            if (noteval > DiatonicScale.MAXNOTENUM || noteval < 1) {
                if (noteval > DiatonicScale.MAXNOTENUM) {
                    while (DiatonicScale.NoteNum(note, octave) > DiatonicScale.MAXNOTENUM) {
                        octave = Octave.sub(octave);
                    }
                }
                if (noteval < 1) {
                    while (DiatonicScale.NoteNum(note, octave) < 1) {
                        octave = Octave.add(octave);
                    }
                }
            }
            tempnoteQ.add(new Pair<>(note,octave));

        }
        noteQ = tempnoteQ;

    }

    //play the notes in the queue noteQ
    public static CustomCountDownTimer  playAll(final Collection collection,final Ifunction finishFunc) {
        CustomCountDownTimer cd = null;
        try {
            if (sound != null && sound.noteQ.size() > 0) {
                sound.EqualizeNoteQueue();
                final int play = 500;//ammount of time to play note
                final int fade = 150;//ammount of time to fade out when done playing
                int ms = (sound.noteQ.size() + 1) * (play+fade);//number of milliseconds to play all the notes in NoteQ
                cd = new CustomCountDownTimer(ms, (play+fade)) {
                    Ifunction f = finishFunc;
                    Collection c = collection;
                    MyMediaPlayer mediaNote = null;
                    Pair<Notes,Octave> note = null;
                    @Override
                    public void onTick(long millisUntilFinished) {
                        try {
                            //if note is not null then stop the sound file from playing by pausing the sound, and then "rewind" the sound file by calling SeekTo(0) and renewing the note
                            note = sound.noteQ.poll(); //get the next note in the queue
                            if (note != null) {
                                int noteval = DiatonicScale.NoteNum(note);
                                mediaNote = new MyMediaPlayer(sound.context, sound.audiofiles.get(noteval), play, fade, true);
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    @Override
                    public void onFinish() {
                        try {
                            sound.noteQ.clear();
                            sound.highestOctave = null;
                            sound.lowestOctave = null;
                            execute();
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    //execute Ifunction f on collection c when coundown stops or finishes
                    private void execute(){
                        if(f!=null && c!= null){
                            for(Object o:c){
                                f.execute(o);
                            }
                        }
                    }
                    @Override
                    public void stop(){
                        cancel();
                        onFinish();

                    }
                };
            }
        }
       catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return cd;
    }
    public static CustomCountDownTimer  playAll(){
        return playAll(null,null);
    }
    //initilizes singleton
    public static void initSound(Context context){
        if(sound == null){
            sound = new Sound(context);
        }
    }
    //add note to the noteQ to be played when playall is called
    public static void AddNote(Notes note, Octave octave){
        try {

            if( sound.highestOctave == null || (Octave.toInt(octave) > Octave.toInt(sound.highestOctave))){
                sound.highestOctave = octave;
            }

            if(sound.lowestOctave == null || (Octave.toInt(octave) < Octave.toInt(sound.lowestOctave)) ){
                sound.lowestOctave = octave;
            }
            sound.noteQ.add(new Pair<>(note,octave));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void AddNote(Notes note) {
        AddNote(note, Octave.middleOctave);
    }
    public static void AddNote(Pair<Notes,Octave> note) {
        AddNote(note.first,note.second);
    }
}
