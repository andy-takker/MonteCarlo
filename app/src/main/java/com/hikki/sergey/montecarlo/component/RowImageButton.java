package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hikki.sergey.montecarlo.R;

public class RowImageButton extends ImageButton {
    public static int ADD = 1;
    public static int REMOVE = 2;

    private Resources mResources;
    private Context mContext;

    public RowImageButton(Context context, int type) {
        super(context, null);
        mContext = context;
        mResources = getResources();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getDps(44), getDps(44));
        lp.setMargins(getDps(8), getDps(4), getDps(4),getDps(4));
        this.setLayoutParams(lp);


        if (type == ADD){
            this.setImageDrawable(mResources.getDrawable(R.drawable.image_add_button));
            this.setBackgroundColor(mResources.getColor(R.color.color_add_button));
        } else if (type == REMOVE){
            this.setImageDrawable(mResources.getDrawable(R.drawable.image_remove_button));
            this.setBackgroundColor(mResources.getColor(R.color.color_remove_button));
        }
    }

    public RowImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }

}
