package com.mycompany.ShapeSounds;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MySpinner extends Spinner {



    boolean first = true;
    public MySpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MySpinner(Context context)
    { super(context); }



    public MySpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override
    public void setSelection(int position, boolean animate)
    {
        int pos = getSelectedItemPosition();
        super.setSelection(position, animate);

        if (position == pos)
        {
            getOnItemSelectedListener().onItemSelected(MySpinner.this, getSelectedView(), position, getSelectedItemId());
        }
        if(first){
            first = false;
        }
    }


    @Override
    public void setSelection(int position)
    {
        int pos = getSelectedItemPosition();
        View view = getSelectedView();
        view.setSelected(true);
        if(first){
            first = false;
        }
        if(pos!=position){
            super.setSelection(position);
            int abc= 123;
            abc+=3;
        }
        //if(first){
      //      view.invalidate();
      //      this.invalidate();
      //      first = false;
       // }

        /*if (position == pos)
        {
            getOnItemSelectedListener().onItemSelected(MySpinner.this, getSelectedView(), position, getSelectedItemId());
            boolean s = getSelectedView().isSelected();

        }*/
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(first){
            getSelectedView().setSelected(true);

        }
    }
}