package com.mycompany.ShapeSounds;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;


public class MyActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {


    CustomDrawableView mCustomDrawableView;
    private MySpinner drawButton;//Spinner;
    private ImageButton playButton;
    private ImageButton navButton;
    private View lastSelectedShape = null;
    private ImageButton clearButton;
    private ImageButton undoButton;
    boolean first = true;
    private View selectedButton;
    private InterstitialAd mInterstitialAd;
    private CustomCountDownTimer player = null;
    int[] arr_images = {R.drawable.triangle_custom,R.drawable.square_custom,R.drawable.pentagon_custom,R.drawable.hexagon_custom};//,R.drawable.circle_custom};
    String[] shapeNames = {"triangle","square","pentagon","hexagon"};//,"circle"};
    ShapeType[] shapes = {ShapeType.triangle,ShapeType.square,ShapeType.pentagon,ShapeType.hexagon};//,ShapeType.circle};


    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



try {
    Sound.initSound(this);
    setContentView(R.layout.activity_my);
    mCustomDrawableView = (CustomDrawableView) findViewById(R.id.drawView);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    drawButton = (MySpinner) findViewById(R.id.draw_spinner);
    drawButton.setAdapter(new MyAdapter(MyActivity.this, R.layout.draw_spinner_row, shapeNames));
    drawButton.setOnItemSelectedListener(MyActivity.this);
    drawButton.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (lastSelectedShape != null) {
                raiseLowerButtons(lastSelectedShape);
            }
            return false;
        }
    });

    playButton = (ImageButton) findViewById(R.id.playButton);
    navButton = (ImageButton) findViewById(R.id.navButton);
    clearButton = (ImageButton) findViewById(R.id.clearButton);
    undoButton = (ImageButton) findViewById(R.id.undoButton);

    playButton.setBackgroundResource(R.drawable.button_custom);
    navButton.setBackgroundResource(R.drawable.button_custom);
    clearButton.setBackgroundResource(R.drawable.button_custom);
    undoButton.setBackgroundResource(R.drawable.button_custom);
    drawButton.setBackgroundResource(R.drawable.button_custom);
    //drawButton.setSelection(0);
    drawButton.setBackgroundResource(R.drawable.button_custom);

    selectedButton = null;


    clearButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mCustomDrawableView.Clear();
            }
            catch(Exception e){
                String a  = e.getMessage();
                System.out.println(a);
            }
        }
    });

    undoButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mCustomDrawableView.Undo();
            }
            catch(Exception e){
                String a  = e.getMessage();
                System.out.println(a);
            }
        }
    });



    playButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (!playButton.isSelected()) {
                    mCustomDrawableView.QueUpForPlay();
                    player = Sound.playAll(Arrays.asList(new View[]{selectedButton, drawButton, playButton, clearButton, undoButton, navButton}), new Ifunction() {
                        View selectBtn = selectedButton;
                        ImageButton playBtn = playButton;
                        CustomDrawableView cdv = mCustomDrawableView;

                        @Override
                        public void execute(Object o) {
                            View btn = (View) o;
                            if (btn == playBtn) {
                                playBtn.setSelected(false);
                                cdv.CanDraw(true);
                            } else {
                                btn.setEnabled(true);
                                if (btn == selectBtn) {
                                    selectBtn.setSelected(true);
                                }
                            }

                        }
                    });
                    if (player != null) {
                        selectedButton.setSelected(false);
                        playButton.setSelected(true);
                        drawButton.setEnabled(false);
                        clearButton.setEnabled(false);
                        undoButton.setEnabled(false);
                        mCustomDrawableView.CanDraw(false);
                        navButton.setEnabled(false);
                        player.start();
                    }
                } else {
                    if (player != null) {
                        player.stop();
                        player = null;
                    }
                }
            } catch (Exception e) {
                String a = e.getMessage();
                System.out.println(a);
            }
        }
    });

    navButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                boolean s = lastSelectedShape.isSelected();
                raiseLowerButtons(navButton);
                mCustomDrawableView.Zoom(true);
            }
            catch(Exception e){
                String a  = e.getMessage();
                System.out.println(a);
            }
        }
    });
}

catch(Exception e){
    String a  = e.getMessage();
    System.out.println(a);
}
    }


    private void raiseLowerButtons(View clickedButton){
        try {
            if (selectedButton != clickedButton) {
                clickedButton.setSelected(true);
                if (selectedButton != null) {
                    if (selectedButton == navButton) {
                        mCustomDrawableView.Zoom(false);
                    }
                    selectedButton.setSelected(false);
                }
                selectedButton = clickedButton;
            } else {
                clickedButton.setSelected(true);
            }
        }
        catch(Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCustomDrawableView.SetShape(shapes[position]);
        if (lastSelectedShape == null) {
            drawButton.invalidate();
        }
        lastSelectedShape = view;
        raiseLowerButtons(view);
        if(first){
          //  boolean s = view.isSelected();
          //  view.invalidate();
           // drawButton.invalidate();
            first = false;
          //  view.requestFocus();
//            drawButton.requestChildFocus(view,drawButton.getFocusedChild());
        }




    }
    public void onNothingSelected(AdapterView<?> parent) {
        int abc = 123;
        abc+=4;
    }


    public class MyAdapter extends ArrayAdapter<String>{
        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            View row = null;
            try {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.draw_spinner_row, parent, false);
                ImageView icon = (ImageView) row.findViewById(R.id.image);
                icon.setImageResource(arr_images[position]);
                icon.setBackgroundResource(R.drawable.button_custom);
            }
            catch(Exception e){
                String a  = e.getMessage();
                System.out.println(a);
            }

            return row;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        boolean result = false;
       try {

       }
       catch(Exception e){
           System.out.println(e.getMessage());

       }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            // Inflate the menu items for use in the action bar

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_activity_actions, menu);


            /*if (!BuildConfig.DEBUG) {
                MenuItem mi = menu.findItem(R.id.polygon);
                mi.getSubMenu().removeItem(R.id.line);
                menu.removeItem(R.id.playmotion);
                menu.removeItem(R.id.oneless);
                menu.removeItem(R.id.onemore);


            }*/

        }
        catch(Exception e){
            System.out.println(e.getMessage());

        }
        return super.onCreateOptionsMenu(menu);

    }
}
