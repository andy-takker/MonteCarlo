package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hikki.sergey.montecarlo.R;

public class InputStdLayout extends LinearLayout {
    private Context mContext;
    private Resources mResources;

    private EditText mValue;
    private TextView mUnits;
    private DropDownListView mDropDownListView;
    private EditText mMin;
    private EditText mMax;

    public InputStdLayout(Context context) {
        super(context, null);
        mContext = context;
        mResources = getResources();

        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.LEFT);

        mValue = new EditText(mContext);
        mValue.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mValue.setHint(mResources.getString(R.string.inputValue));
        mValue.setEms(4);
        mValue.setSingleLine();
        mValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        mValue.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);
        mValue.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        this.addView(mValue);

        mUnits = new TextView(mContext);
        mUnits.setText("%");
        mUnits.setPadding(0,0, getDps(4),0);
        mUnits.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        this.addView(mUnits);

        mDropDownListView = new DropDownListView(mContext);
        mDropDownListView.setVisibility(GONE);
        this.addView(mDropDownListView);

        mMin = new EditText(mContext);
        mMin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mMin.setHint(mResources.getString(R.string.inputMin));
        mMin.setEms(2);
        mMin.setSingleLine();
        mMin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        mMin.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);
        this.addView(mMin);

        mMax = new EditText(mContext);
        mMax.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mMax.setHint(mResources.getString(R.string.inputMax));
        mMax.setEms(2);
        mMax.setSingleLine();
        mMax.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        mMax.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);
        this.addView(mMax);

        TextView proc = new TextView(mContext);
        proc.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        proc.setText("%");
        this.addView(proc);
    }

    public InputStdLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
    public EditText getMax() {
        return mMax;
    }
    public EditText getValue() {
        return mValue;
    }
    public EditText getMin() {
        return mMin;
    }

    public void setEnabledValue(boolean isEnabled){
        mValue.setEnabled(isEnabled);
    }

    public void setConstantValue(boolean isConstant){
        mMax.setEnabled(isConstant);
        mMin.setEnabled(isConstant);
    }

    public TextView getUnits() {
        return mUnits;
    }

    public DropDownListView getListView(){
        return mDropDownListView;
    }

    public double getMainValue(){
        double n = 0;
        try{
            Double d = Double.valueOf(mValue.getText().toString());
            n = d.doubleValue();
        } catch (Exception e){
            n = 0;
        }
        return n;
    }

    public double getMaxValue(){
        double n = 0;
        try{
            Double d = Double.valueOf(mMax.getText().toString());
            n = d.doubleValue();
        } catch (Exception e){
            n = 0;
        }
        return n;
    }

    public double getMinValue(){
        double n = 0;
        try{
            Double d = Double.valueOf(mMin.getText().toString());
            n = d.doubleValue();
        } catch (Exception e){
            n = 0;
        }
        return n;
    }

    public int getValueCoef(){
        return mDropDownListView.getValueCoef();
    }
}
