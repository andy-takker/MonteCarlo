package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailedListView extends LinearLayout {
    private Resources mResources;
    private Context mContext;
    private int mYears;
    private ValueWithDetailListView mParent;
    private RowImageButton mAddButton;

    private List<RowDetailList> mRows;

    public DetailedListView(Context context, ValueWithDetailListView parent) {
        super(context,null);
        mContext = context;
        mResources = getResources();

        mParent = parent;

        mRows = new ArrayList<>();

        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, getDps(8),0,0);

        mAddButton = new RowImageButton(mContext, RowImageButton.ADD);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createRow();
            }
        });
        this.addView(mAddButton);

        createRow();

    }

    private void removeRow(RowDetailList row){
        if (row != null){
            this.removeView(row);
            mRows.remove(row);
        }
    }

    public void createRow(){
        final RowDetailList row = new RowDetailList(mContext);
        row.createYearsValue(mYears);
        row.getRemoveButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRows.size() == 1){
                    mParent.clickDetailButton();
                } else {
                    removeRow(row);
                }
            }
        });
        mRows.add(row);
        this.removeView(mAddButton);
        this.addView(row);
        this.addView(mAddButton);
    }

    public List<List<Double>> getValuesRows(){
        List<List<Double>> v = new ArrayList<>();
        for (int i = 0; i < mRows.size(); i++){
            v.add(mRows.get(i).getYearsValue());
        }
        return v;
    }

    public void updateRows(){
        for (int i = 0; i < mRows.size();i++){
            mRows.get(i).updateYearsValue(mYears);
        }
    }

    public void setYears(int years){
        mYears = years;
    }

    public DetailedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
}
