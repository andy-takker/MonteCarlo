package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hikki.sergey.montecarlo.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class ErrorTextView extends android.support.v7.widget.AppCompatTextView {
    private Resources mResources;

    public ErrorTextView(Context context) {
        super(context, null);
        mResources = getResources();

        this.setText(mResources.getString(R.string.error_duration_mess));
        this.setTextSize(COMPLEX_UNIT_SP, 8);
        this.setTextColor(mResources.getColor(R.color.color_error_text_view_text));
        this.setBackgroundColor(mResources.getColor(R.color.color_error_text_view_foreground));
        this.setPadding(getDps(4),getDps(4),getDps(4),getDps(4));
        this.setVisibility(GONE);
    }

    public ErrorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
}
