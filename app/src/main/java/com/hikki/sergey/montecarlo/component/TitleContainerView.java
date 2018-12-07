package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TitleContainerView extends LinearLayout {
    private Context mContext;
    private Resources mResources;

    private IteratorCountView mCountView;
    private ProjectDurationView mProjectDuration;

    public TitleContainerView(Context context) {
        super(context, null);
        mContext = context;
        mResources = getResources();

        mProjectDuration = new ProjectDurationView(mContext);
        LinearLayout.LayoutParams lpd = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lpd.weight = 1;
        mProjectDuration.setLayoutParams(lpd);

        mCountView = new IteratorCountView(mContext);
        LinearLayout.LayoutParams lpi = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lpi.weight = 1;
        mCountView.setLayoutParams(lpi);


        this.addView(mProjectDuration);
        this.addView(new Divider(mContext, Divider.VERTICAL));
        this.addView(mCountView);
    }

    public TitleContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditText getYearEditText(){
        return mProjectDuration.getYearValue();
    }
    public int getYears(){
        return mProjectDuration.getYears();
    }
    public void showHideErrorMessYear(CharSequence s){
        mProjectDuration.showHideErrorMess(s);
    }

    public EditText getItersEditText(){
        return mCountView.getIteratorValue();
    }
    public int getIters(){
        return mCountView.getIters();
    }
    public void showHideErrorMessIter(CharSequence s){
        mCountView.showHideErrorMess(s);
    }

}
