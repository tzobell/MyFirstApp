package com.mycompany.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class MyActivity extends ActionBarActivity {


    CustomDrawableView mCustomDrawableView;
   ;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    MenuItem menuitem;
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sound.initSound(this);
       mCustomDrawableView = new CustomDrawableView(this);


        setContentView(mCustomDrawableView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        boolean result = false;
       try {
           switch (item.getItemId()) {

               case R.id.play:
                   // Sound.Play(Notes.A);
                   mCustomDrawableView.Play();
                   result = true;
                   break;

               case R.id.polygon:
                   menuitem = item;
               case R.id.circle:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.circle);
                   result = true;
                   break;
               case R.id.square:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.square);
                   result = true;
                   break;
               case R.id.line:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.line);
                   result = true;
                   break;
               case R.id.triangle:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.triangle);
                   result = true;
                   break;
               case R.id.pentagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.pentagon);
                   result = true;
                   break;

               case R.id.hexagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.hexagon);
                   result = true;
                   break;
              /* case R.id.heptagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.heptagon);
                   result = true;
                   break;
               case R.id.octagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.octagon);
                   result = true;
                   break;
               case R.id.nonagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.nonagon);
                   result = true;
                   break;
               case R.id.decagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.decagon);
                   result = true;
                   break;
               case R.id.hendecagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.hendecagon);
                   result = true;
                   break;
               case R.id.dodecagon:
                   menuitem.setIcon(item.getIcon());
                   mCustomDrawableView.SetShape(ShapeType.dodecagon);
                   result = true;
                   break;*/
               case R.id.clear:
                   mCustomDrawableView.Clear();
                   result = true;
                   break;


               case R.id.playmotion:
                   mCustomDrawableView.PlayMotions();
                   result = true;
                   break;
               case R.id.oneless:
                   mCustomDrawableView.OneLessMotion();
                   result = true;
                   break;
               case R.id.onemore:
                   mCustomDrawableView.OneMoreMotion();
                   result = true;
                   break;

               case R.id.undo:
                   mCustomDrawableView.Undo();
                   result = true;
                   break;
               default:
                   result = super.onOptionsItemSelected(item);
           }
       }
       catch(Exception e){
           System.out.println(e.getMessage());

       }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        if(!BuildConfig.DEBUG){
            MenuItem  mi = menu.findItem(R.id.polygon);
            mi.getSubMenu().removeItem(R.id.line);
            menu.removeItem(R.id.playmotion);
            menu.removeItem(R.id.oneless);
            menu.removeItem(R.id.onemore);


        }

        return super.onCreateOptionsMenu(menu);
    }
}
