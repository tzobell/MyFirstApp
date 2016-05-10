package com.mycompany.ShapeSounds;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Owner on 5/9/2016.
 */
public class MyImageButton extends ImageButton {


    public MyImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyImageButton(Context context)
    {
        super(context);
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        this.setAlpha(enabled ? 1.0f : 0.4f);
    }

}
