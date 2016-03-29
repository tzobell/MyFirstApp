package com.mycompany.ShapeSounds;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.mycompany.ShapeSounds.CustomCountDownTimer;

import java.util.Timer;
import java.util.TimerTask;

/**
 plays a media resource for a period of time and then gradually fades out of the audio when done playing it
 */
public class MyMediaPlayer  {

    MediaPlayer mp;
    long playms;//amount of time to play mediaPlayer
    long fadems;// period of time to fade out audio over
    int vol; // initial volume
    final int MAXVOL = 100;

    MyMediaPlayer(Context context,int resid, long playms,long fadems, boolean startNow){
        try {
            mp = MediaPlayer.create(context, resid);
            this.playms = playms;
            this.fadems = fadems;
            vol = MAXVOL;
            if(startNow){
                start();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    MyMediaPlayer(Context context,int resid, long playms,long fadems){
       this(context,resid,playms,fadems,false);
    }

    //starts playing mediaplayer file, then after playms millisecs the volume is gradually faded out over fadems millisecs
    public  void start(){
        try {
            int tsub = 1;

            //if fadems less then the amount that vol is decreased by on every call to run needs to be increased
            if(fadems < MAXVOL){
                tsub = (int)(MAXVOL/fadems);
            }

            int freq = (int)((fadems*tsub) / MAXVOL);//how often to call run in timertask
            final int sub = tsub;
            final Timer timer = new Timer(true);


            TimerTask timertask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        vol -= sub;
                        float volLevel = 1 - (float) (Math.log(MAXVOL - vol) / Math.log(MAXVOL));
                        if (volLevel > 1) {
                            volLevel = 1;
                        }
                        if (volLevel < 0) {
                            volLevel = 0;
                        }
                        mp.setVolume(volLevel, volLevel);

                        if (vol <= 0) {
                            mp.pause();
                            mp.release();
                            timer.cancel();
                            timer.purge();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            };
            mp.start();
            timer.schedule(timertask, playms, freq);


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
