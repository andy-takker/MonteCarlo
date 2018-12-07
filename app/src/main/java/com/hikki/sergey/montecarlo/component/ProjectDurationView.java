package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hikki.sergey.montecarlo.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class ProjectDurationView extends LinearLayout {

    private Context mContext;
    private Resources mResources;

    private TextView mTitleView;
    private EditText mYearValue;
    private TextView mErrorTextView;

    public ProjectDurationView(Context context) {
        super(context, null);

        mContext = context;
        mResources = getResources();

        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, getDps(8), 0, 0);

        mTitleView = new TextView(mContext);
        mTitleView.setFocusable(true);
        mTitleView.setText(mResources.getString(R.string.project_duration));
        mTitleView.setTextColor(Color.BLACK);
        mTitleView.setTextSize(COMPLEX_UNIT_SP, 14);
        mTitleView.setPadding(getDps(8),0, getDps(8), 0);

        LinearLayout subLayout = new LinearLayout(mContext);
        subLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        subLayout.setGravity(Gravity.CENTER_VERTICAL);
        subLayout.setPadding(getDps(8),0,getDps(8), 0);

        mYearValue = new EditText(mContext);
        mYearValue.setEms(4);
        mYearValue.setSingleLine();
        mYearValue.setText("1");
        mYearValue.setTextSize(COMPLEX_UNIT_SP, 14);
        mYearValue.setHint("99 лет...");
        mYearValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        mYearValue.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);


        mErrorTextView = new ErrorTextView(mContext);

        Divider divider = new Divider(mContext, Divider.HORIZONTAL);

        this.addView(mTitleView);
        this.addView(subLayout);
        subLayout.addView(mYearValue);
        subLayout.addView(mErrorTextView);
        this.addView(divider);
    }

    public ProjectDurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getYears(){
        int n = 1;
        if (!mYearValue.getText().equals("")){
            try{
                if(mYearValue != null) {
                    Integer i = Integer.valueOf(mYearValue.getText().toString());
                    n = i.intValue();
                }
                mErrorTextView.setVisibility(GONE);
            } catch (Exception e){
                mErrorTextView.setVisibility(VISIBLE);
            }
        }

        return n;
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }

    public EditText getYearValue() {
        return mYearValue;
    }
    public void showHideErrorMess(CharSequence s){
        Log.i("ErrorMess", "ProjectDuration: "+s.toString());
        if (s.toString().equals("0") || s.toString().equals("") || s.charAt(0) == '0'){
            mErrorTextView.setVisibility(VISIBLE);
        } else{
            mErrorTextView.setVisibility(GONE);
        }
    }
}
