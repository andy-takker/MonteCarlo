package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class RowDetailList extends LinearLayout {
    private Context mContext;
    private Resources mResources;
    private RowImageButton mRemoveButton;
    private DetailNameEditText mRowName;
    private LinearLayout mPerYearsValue;
    private List<ItemValuePerYear> mYears;

    public RowDetailList(Context context) {
        super(context, null);
        mContext = context;
        mResources = getResources();

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setPadding(0, getDps(2),0, getDps(2));
        this.setGravity(Gravity.CENTER_VERTICAL);

        mRemoveButton = new RowImageButton(mContext, RowImageButton.REMOVE);
        this.addView(mRemoveButton);

        mRowName = new DetailNameEditText(mContext);
        this.addView(mRowName);

        HorizontalScrollView scrollBase = new HorizontalScrollView(mContext);
        scrollBase.setScrollbarFadingEnabled(true);
        scrollBase.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.addView(scrollBase);

        mPerYearsValue = new LinearLayout(context);
        mPerYearsValue.setGravity(Gravity.LEFT);
        mPerYearsValue.setOrientation(HORIZONTAL);
        mPerYearsValue.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT));
        mPerYearsValue.setHorizontalScrollBarEnabled(true);
        mPerYearsValue.setPadding(0,0,0,getDps(2));
        scrollBase.addView(mPerYearsValue);
        mYears = new ArrayList<>();
    }

    public void createYearsValue(int years){
        for (int i = 0; i < years; i++){
            ItemValuePerYear item = new ItemValuePerYear(mContext);
            item.setYear(i);
            mPerYearsValue.addView(item);
            mYears.add(item);
        }
    }

    public void updateYearsValue(int newYears){
        while (newYears < mYears.size()){
            mPerYearsValue.removeViewAt(mYears.size()-1);
            mYears.remove(mYears.size()-1);
        }
        for (int i = mYears.size(); i < newYears; i++){
            ItemValuePerYear item = new ItemValuePerYear(mContext);
            item.setYear(i);
            mPerYearsValue.addView(item);
            mYears.add(item);
        }
    }

    public RowImageButton getRemoveButton() {
        return mRemoveButton;
    }

    public RowDetailList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<Double> getYearsValue(){
        List<Double> r = new ArrayList<>();
        for(int i = 0; i < mYears.size(); i++){
            r.add(mYears.get(i).getValue());
        }
        return r;
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
}
