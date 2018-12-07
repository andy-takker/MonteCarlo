package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class Divider extends View {
    private Resources mResources;
    public static int HORIZONTAL = -1;
    public static int VERTICAL = -2;

    public Divider(Context context, int type) {
        super(context, null);

        mResources = getResources();
        if (type == HORIZONTAL){
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, getDps(1)));
            this.setPadding(0,getDps(2), 0, getDps(2));
        } else {
            this.setLayoutParams(new LinearLayout.LayoutParams(getDps(2), LinearLayoutCompat.LayoutParams.MATCH_PARENT));
            this.setPadding(0, getDps(2), getDps(2),0);
        }
        this.setBackgroundColor(Color.LTGRAY);
    }

    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
}
