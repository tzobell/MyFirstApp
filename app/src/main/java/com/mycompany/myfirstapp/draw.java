package com.mycompany.myfirstapp;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;


public class draw extends Activity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    CustomDrawableView mCustomDrawableView;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomDrawableView = new CustomDrawableView(this);

        setContentView(mCustomDrawableView);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDown: " + event.toString());
        mCustomDrawableView.setStart(Math.round(event.getX()),Math.round(event.getY()));
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        mCustomDrawableView.setEnd(Math.round(event2.getX()),Math.round(event2.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mCustomDrawableView.setEnd(Math.round(e2.getX()), Math.round(e2.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString() + e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        mCustomDrawableView.setEnd(Math.round(event.getX()),Math.round(event.getY()));
        mCustomDrawableView.draw();
        mCustomDrawableView.invalidate();

        return true;
    }
}