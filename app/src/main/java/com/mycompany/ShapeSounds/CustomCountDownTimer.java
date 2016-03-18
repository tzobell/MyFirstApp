package com.mycompany.ShapeSounds;

import android.os.CountDownTimer;

/**
 * Created by Owner on 3/18/2016.
 */
public abstract class CustomCountDownTimer extends CountDownTimer {

    public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture,countDownInterval);
    }
    //method to impliement to have special behavior when the coundown is done.
    public abstract void stop();

}
