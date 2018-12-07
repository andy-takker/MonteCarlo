package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class IsConstantView extends LinearLayout {
    private Context mContext;
    private CheckBox mIsConstant;
    private boolean mConstant = false;

    public IsConstantView(Context context) {
        super(context, null);
        mContext = context;

        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);


        TextView labelCheckBox = new TextView(mContext);
        labelCheckBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        labelCheckBox.setText("Константа?");
        labelCheckBox.setTextSize(COMPLEX_UNIT_SP, 10);
        this.addView(labelCheckBox);

        mIsConstant = new CheckBox(mContext);
        mIsConstant.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mIsConstant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConstant = isChecked;
            }
        });
        this.addView(mIsConstant);
    }

    public IsConstantView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBox getIsConstant() {
        return mIsConstant;
    }
    public boolean getConstant(){
        return mConstant;
    }

    public void setConstant(boolean constant) {
        mConstant = constant;
    }
}
