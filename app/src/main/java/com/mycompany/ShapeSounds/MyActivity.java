package com.mycompany.ShapeSounds;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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


public class MyActivity extends ActionBarActivity {


    CustomDrawableView mCustomDrawableView;
    private ImageButton drawButton;
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton navButton;
    private ImageButton clearButton;
    private ImageButton undoButton;
    private ImageButton selectedButton;
    private InterstitialAd mInterstitialAd;
    private CustomCountDownTimer player = null;
    MenuItem menuitem;
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .build();

        mInterstitialAd.loadAd(adRequest);*/

try {
    /*AdView mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build();
    mAdView.loadAd(adRequest);*/


    Sound.initSound(this);

   // mCustomDrawableView = new CustomDrawableView(this);
    setContentView(R.layout.activity_my);
    mCustomDrawableView = (CustomDrawableView) findViewById(R.id.drawView);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

    setSupportActionBar(myToolbar);

    getSupportActionBar().setDisplayShowTitleEnabled(false);

    drawButton = (ImageButton) findViewById(R.id.drawButton);
    playButton = (ImageButton) findViewById(R.id.playButton);
    stopButton = (ImageButton) findViewById(R.id.stopButton);
    navButton = (ImageButton) findViewById(R.id.navButton);
    clearButton = (ImageButton) findViewById(R.id.clearButton);
    undoButton = (ImageButton) findViewById(R.id.undoButton);


    playButton.setBackgroundResource(R.drawable.button_custom);
    stopButton.setBackgroundResource(R.drawable.button_custom);
    navButton.setBackgroundResource(R.drawable.button_custom);
    clearButton.setBackgroundResource(R.drawable.button_custom);
    undoButton.setBackgroundResource(R.drawable.button_custom);
    drawButton.setBackgroundResource(R.drawable.button_custom);

    drawButton.setSelected(true);
    selectedButton = drawButton;

    clearButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCustomDrawableView.Clear();
        }
    });

    undoButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCustomDrawableView.Undo();
        }
    });


    stopButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(playButton.isSelected() && player!=null){
                player.stop();
                player = null;
            }
        }
    });

    playButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mCustomDrawableView.QueUpForPlay();
            player = Sound.playAll(Arrays.asList(new ImageButton[]{selectedButton, drawButton, playButton, clearButton, undoButton}), new Ifunction() {
                ImageButton selectBtn = selectedButton;
                ImageButton playBtn = playButton;
                CustomDrawableView cdv = mCustomDrawableView;

                @Override
                public void execute(Object o) {
                    ImageButton btn = (ImageButton) o;
                    if(btn == playBtn){
                        playBtn.setSelected(false);
                        cdv.CanDraw(true);
                    }
                    else {
                        btn.setEnabled(true);
                        if (btn == selectBtn) {
                            selectBtn.setSelected(true);
                        }
                    }

                }
            });
            if(player!=null) {
                playButton.setSelected(true);
                selectedButton.setSelected(false);
                drawButton.setEnabled(false);
                clearButton.setEnabled(false);
                undoButton.setEnabled(false);
                mCustomDrawableView.CanDraw(false);
                player.start();
            }
        }
    });

    navButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            raiseLowerButtons(navButton);
            mCustomDrawableView.Zoom(true);
        }
    });


    drawButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(MyActivity.this, drawButton);
            setForceShowIcon(popup);

            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.shape_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    boolean result = false;
                    switch (item.getItemId()) {
                        case R.id.circle:
                            drawButton.setImageDrawable(item.getIcon());
                            mCustomDrawableView.SetShape(ShapeType.circle);
                            result = true;
                            break;
                        case R.id.square:
                            drawButton.setImageDrawable(item.getIcon());
                            mCustomDrawableView.SetShape(ShapeType.square);
                            result = true;
                            break;
                        case R.id.line:
                            drawButton.setImageDrawable(item.getIcon());
                            mCustomDrawableView.SetShape(ShapeType.line);
                            result = true;
                            break;
                        case R.id.triangle:
                            drawButton.setImageDrawable(item.getIcon());
                            mCustomDrawableView.SetShape(ShapeType.triangle);
                            result = true;
                            break;
                        case R.id.pentagon:
                            drawButton.setImageDrawable(item.getIcon());
                            mCustomDrawableView.SetShape(ShapeType.pentagon);
                            result = true;
                            break;
                        case R.id.hexagon:
                            drawButton.setImageDrawable(item.getIcon());
                            mCustomDrawableView.SetShape(ShapeType.hexagon);
                            result = true;
                            break;
                    }
                    return result;
                }
            });

            if (selectedButton == drawButton) {
                popup.show(); //showing popup menu
            } else {
                raiseLowerButtons(drawButton);
            }

        }
    });

    //setContentView(mCustomDrawableView);
   // getSupportActionBar().setDisplayShowTitleEnabled(false);

}

catch(Exception e){
    String a  = e.getMessage();
    System.out.println(a);
}
    }


    private void raiseLowerButtons(ImageButton clickedButton){
        if(selectedButton!=clickedButton){
            clickedButton.setSelected(true);
            if(selectedButton!=null){
                if(selectedButton == navButton){
                    mCustomDrawableView.Zoom(false);
                }
                selectedButton.setSelected(false);
            }
            selectedButton = clickedButton;
        }
    }

   /* @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (mCustomDrawableView.zoom == true) {
            Drawable icon = menu.findItem(R.id.polygon).getIcon();


        }
        else{
            menu.findItem(R.id.polygon).setEnabled(true);
        }
        return true;
    }*/
   public static void setForceShowIcon(PopupMenu popupMenu) {
       try {
           Field[] fields = popupMenu.getClass().getDeclaredFields();
           for (Field field : fields) {
               if ("mPopup".equals(field.getName())) {
                   field.setAccessible(true);
                   Object menuPopupHelper = field.get(popupMenu);
                   Class<?> classPopupHelper = Class.forName(menuPopupHelper
                           .getClass().getName());
                   Method setForceIcons = classPopupHelper.getMethod(
                           "setForceShowIcon", boolean.class);
                   setForceIcons.invoke(menuPopupHelper, true);
                   break;
               }
           }
       } catch (Throwable e) {
           e.printStackTrace();
       }
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        boolean result = false;
       try {

       /*    switch (item.getItemId()) {

               case R.id.play:

                   mCustomDrawableView.Play();
                   result = true;

                   break;
               case R.id.Navigate:
                  mCustomDrawableView.zoom = !mCustomDrawableView.zoom;
                   invalidateOptionsMenu();

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
           */
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
