package com.mycompany.ShapeSounds;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class MyActivity extends ActionBarActivity {


    CustomDrawableView mCustomDrawableView;
   ;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private InterstitialAd mInterstitialAd;
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

    mCustomDrawableView = new CustomDrawableView(this);


    setContentView(mCustomDrawableView);
}

catch(Exception e){
    String a  = e.getMessage();
    System.out.println(a);
}



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        boolean result = false;
       try {
           switch (item.getItemId()) {

               case R.id.play:

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
