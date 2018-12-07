package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hikki.sergey.montecarlo.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class IteratorCountView extends LinearLayout {
    private Context mContext;
    private Resources mResources;
    private TextView mTitleView;
    private EditText mIteratorValue;
    private ErrorTextView mErrorTextView;

    public IteratorCountView(Context context) {
        super(context, null);

        mContext = context;
        mResources = getResources();

        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, getDps(8),0,0);

        mTitleView = new TextView(mContext);
        mTitleView.setFocusable(true);
        mTitleView.setText(mResources.getString(R.string.project_iters));
        mTitleView.setTextColor(Color.BLACK);
        mTitleView.setTextSize(COMPLEX_UNIT_SP, 14);
        mTitleView.setPadding(getDps(8),0, getDps(8), 0);

        LinearLayout subLayout = new LinearLayout(mContext);
        subLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        subLayout.setGravity(Gravity.CENTER_VERTICAL);
        subLayout.setPadding(getDps(8),0,getDps(8), 0);

        mIteratorValue = new EditText(mContext);
        mIteratorValue.setEms(4);
        mIteratorValue.setSingleLine();
        mIteratorValue.setText("100");
        mIteratorValue.setTextSize(COMPLEX_UNIT_SP, 14);
        mIteratorValue.setHint("100 раз...");
        mIteratorValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        mIteratorValue.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);

        mErrorTextView = new ErrorTextView(mContext);

        this.addView(mTitleView);
        subLayout.addView(mIteratorValue);
        subLayout.addView(mErrorTextView);
        this.addView(subLayout);
        this.addView(new Divider(mContext, Divider.HORIZONTAL));
    }

    public IteratorCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }

    public int getIters(){
        int n = 1;
        if (!mIteratorValue.getText().equals("")){
            try{
                if(mIteratorValue != null) {
                    Integer i = Integer.valueOf(mIteratorValue.getText().toString());
                    n = i.intValue();
                }
                mErrorTextView.setVisibility(GONE);
            } catch (Exception e){
                mErrorTextView.setVisibility(VISIBLE);
            }
        }

        return n;
    }

    public EditText getIteratorValue(){
        return  mIteratorValue;
    }

    public void showHideErrorMess(CharSequence s){
        Log.i("ErrorMess", "IteratorValue: "+s.toString());
        if (s.toString().equals("") || s.toString().equals("0") || s.charAt(0) == '0'){
            mErrorTextView.setVisibility(VISIBLE);
        } else {
            mErrorTextView.setVisibility(GONE);
        }
    }
}
