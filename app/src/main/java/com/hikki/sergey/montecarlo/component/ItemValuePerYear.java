package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.util.TypedValue;
import android.widget.Toast;

public class ItemValuePerYear extends LinearLayout {
    private Context mContext;
    private TextView mYear;
    private ValuePerYearEditText mValue;

    public ItemValuePerYear(Context context) {
        super(context, null);

        mContext = context;

        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

        mYear = new TextView(mContext);
        mYear.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
        mYear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.addView(mYear);

        mValue = new ValuePerYearEditText(mContext);
        this.addView(mValue);
    }

    public ItemValuePerYear(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setYear(int year){
        mYear.setText(""+year);
    }

    public double getValue(){
        double n = 0;
        try{
            Double d = Double.valueOf(mValue.getText().toString());
            n = d.doubleValue();
        } catch (Exception e){
            //Toast.makeText(mContext, "Value:"+mValue.getText().toString()+"\nError: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            n = 0;
        }
        return n;
    }
}
