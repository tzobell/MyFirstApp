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
    {
        super(context);
    }



    public MySpinner(Context context, AttributeSet attrs, int defStyle)
    {

        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int position, boolean animate)
    {
        try {
            int pos = getSelectedItemPosition();
            super.setSelection(position, animate);

            if (position == pos) {
                getOnItemSelectedListener().onItemSelected(MySpinner.this, getSelectedView(), position, getSelectedItemId());
            }
            if (first) {
                first = false;
            }
        }
        catch(Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }


    @Override
    public void setSelected(boolean selected){
        super.setSelected(selected);
    }

    @Override
    public void setSelection(int position)
    {
        try {
            int pos = getSelectedItemPosition();
            View view = getSelectedView();
            view.setSelected(true);
            if (first) {
                first = false;
            }
            if (pos != position) {
                super.setSelection(position);

            }
        }
        catch(Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }

    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        this.setAlpha(enabled ? 1.0f : 0.4f);;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            View view = getSelectedView();
            boolean selected = view != null ? view.isSelected() : false;
            super.onLayout(changed, l, t, r, b);

            if (view != null) {
                view.setSelected(selected);
            }
        }
        catch(Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }

    }
}