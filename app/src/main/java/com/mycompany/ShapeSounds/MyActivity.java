package com.mycompany.ShapeSounds;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
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
import android.view.WindowManager;
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
    private MyImageButton navButton;

    private MyImageButton clearButton;
    private MyImageButton undoButton;
    private MyImageButton redoButton;
    boolean first = true;
    private View selectedButton;
    private View lastSelectedShape = null;

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
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    setContentView(R.layout.activity_my);
    mCustomDrawableView = (CustomDrawableView) findViewById(R.id.drawView);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    drawButton = (MySpinner) findViewById(R.id.draw_spinner);
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
       Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        int offset = toolbar.getMinimumHeight();
        drawButton.setDropDownVerticalOffset(offset);
    }


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
    navButton = (MyImageButton) findViewById(R.id.navButton);
    clearButton = (MyImageButton) findViewById(R.id.clearButton);
    undoButton = (MyImageButton) findViewById(R.id.undoButton);
    redoButton = (MyImageButton) findViewById(R.id.redoButton);
    playButton.setBackgroundResource(R.drawable.button_custom);
    navButton.setBackgroundResource(R.drawable.button_custom);
    clearButton.setBackgroundResource(R.drawable.button_custom);
    undoButton.setBackgroundResource(R.drawable.button_custom);
    redoButton.setBackgroundResource(R.drawable.button_custom);
    drawButton.setBackgroundResource(R.drawable.button_custom);
    drawButton.setBackgroundResource(R.drawable.button_custom);

    selectedButton = null;


    clearButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mCustomDrawableView.Clear();
            } catch (Exception e) {
                String a = e.getMessage();
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


    redoButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mCustomDrawableView.Redo();
            }
            catch(Exception e){
                String a  = e.getMessage();
                System.out.println(a);
            }
        }
    });


    mCustomDrawableView.setHistoryOnEmptyListener(new OnEmptyListener() {
        @Override
        public void onEmpty() {
            undoButton.setEnabled(false);
        }
    });

    mCustomDrawableView.setHistoryOnFirstElementAddedListener(new OnFirstElementAdded() {
        @Override
        public void FirstAdded() {
            undoButton.setEnabled(true);
        }
    });



    mCustomDrawableView.setFutureOnEmptyListener(new OnEmptyListener() {
        @Override
        public void onEmpty() {
            redoButton.setEnabled(false);
        }
    });




    mCustomDrawableView.setFutureOnFirstElementAddedListener(new OnFirstElementAdded() {
        @Override
        public void FirstAdded() {
            redoButton.setEnabled(true);
        }
    });

    mCustomDrawableView.setShapesOnFirstElementAddedListener(new OnFirstElementAdded() {
        @Override
        public void FirstAdded() {
            playButton.setEnabled(true);
            clearButton.setEnabled(true);
        }
    });

    mCustomDrawableView.setShapesOnEmptyListener(new OnEmptyListener() {
        @Override
        public void onEmpty() {
            playButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    });


    undoButton.setEnabled(false);
    redoButton.setEnabled(false);
    playButton.setEnabled(false);
    clearButton.setEnabled(false);

    playButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (!playButton.isSelected()) {
                    mCustomDrawableView.QueUpForPlay();
                    player = Sound.playAll(Arrays.asList(new VBpair[]{new VBpair(selectedButton,selectedButton.isEnabled()), new VBpair(drawButton,drawButton.isEnabled()), new VBpair(playButton,playButton.isEnabled()), new VBpair(clearButton,clearButton.isEnabled()), new VBpair(undoButton,undoButton.isEnabled()),new VBpair(redoButton,redoButton.isEnabled()), new VBpair(navButton,navButton.isEnabled())}), new Ifunction() {
                        View selectBtn = selectedButton;
                        ImageButton playBtn = playButton;
                        CustomDrawableView cdv = mCustomDrawableView;
                        boolean allowtimout = false;

                        @Override
                        public void execute(Object o) {
                            if(!allowtimout){
                                MyActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//notes done playing so allow screen to time out.
                            }
                            VBpair btn = (VBpair) o;
                            if (btn.view == playBtn) {
                                playBtn.setSelected(false);
                                cdv.CanDraw(true);
                            } else {
                                btn.view.setEnabled(btn.enabled);
                                if (btn.view == selectBtn) {
                                    selectBtn.setSelected(true);
                                    boolean focused = selectBtn.requestFocus();
                                    boolean isselected = selectBtn.isSelected();
                                }
                            }

                        }
                    });
                    if (player != null) {
                        MyActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//keep screen from timing out while playing notes
                        selectedButton.setSelected(false);
                        playButton.setSelected(true);
                        drawButton.setEnabled(false);
                        clearButton.setEnabled(false);
                        undoButton.setEnabled(false);
                        redoButton.setEnabled(false);
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

    AdView mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder()

            .build();
    mAdView.loadAd(adRequest);
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

    }
    public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    protected void onPause(){
        super.onPause();
        if (player != null) {
            player.stop();
            player = null;
        }
    }


}
