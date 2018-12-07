package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hikki.sergey.montecarlo.R;

import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class ValueWithDetailListView extends LinearLayout {
    private Resources mResources;
    private Context mContext;

    private IsConstantView mIsConstant;
    private InputStdLayout mInputLayout;
    private AddDetailedListButton mDetailedButton;
    private DetailedListView mDetails;
    private Divider mDivider;
    private TextView mTitleTextView;

    private int mYears;

    public ValueWithDetailListView(Context context) {
        super(context, null);
        mContext = context;
        mResources = getResources();

        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, getDps(8),0,0);

        LinearLayout mainLayout = new LinearLayout(mContext);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mainLayout.setPadding(getDps(8),0, getDps(8),0);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(mainLayout);

        mIsConstant = new IsConstantView(mContext);
        mIsConstant.getIsConstant().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mInputLayout.setConstantValue(!isChecked);
                mIsConstant.setConstant(isChecked);
            }
        });
        mainLayout.addView(mIsConstant);

        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setPadding(getDps(8),0,0,0);
        titleLayout.setGravity(Gravity.TOP);
        mainLayout.addView(titleLayout);

        FrameLayout titleButtonLayout = new FrameLayout(mContext);
        titleButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        titleLayout.addView(titleButtonLayout);

        mTitleTextView = new TextView(mContext);
        mTitleTextView.setTextSize(COMPLEX_UNIT_SP, 18);
        mTitleTextView.setTextColor(Color.BLACK);
        FrameLayout.LayoutParams titleFL = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleFL.gravity = Gravity.LEFT;
        mTitleTextView.setLayoutParams(titleFL);
        titleButtonLayout.addView(mTitleTextView);

        mDetailedButton = new AddDetailedListButton(mContext);
        mDetailedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDetailButton();
            }
        });
        titleButtonLayout.addView(mDetailedButton);

        mInputLayout = new InputStdLayout(mContext);
        mInputLayout.getUnits().setVisibility(GONE);
        mInputLayout.getListView().setVisibility(VISIBLE);
        titleLayout.addView(mInputLayout);

        mDivider = new Divider(mContext, Divider.HORIZONTAL);
        this.addView(mDivider);

    }

    public void setYears(int years) {
        mYears = years;
    }

    public void clickDetailButton(){
        if (mDetails == null){
            mDetails = new DetailedListView(mContext, this);
            mDetails.setYears(mYears);
            this.removeView(mDivider);
            this.addView(mDetails);
            this.addView(mDivider);
            updateDetails();
        } else {
            this.removeView(mDetails);
            mDetails = null;
        }
        mDetailedButton.setShown(!mDetailedButton.isShown());
        mDetailedButton.setImage();
        mInputLayout.getValue().setEnabled(!mDetailedButton.isShown());
    }
    public void updateDetails(){
        if(mDetails != null) {
            mDetails.setYears(mYears);
            mDetails.updateRows();
        }
    }
    public List<List<Double>> getDetailListView(){
        if (mDetails != null){
            List<List<Double>> r = mDetails.getValuesRows();

            for (int i = 0; i< r.size(); i++){
                for (int j = 0; j < r.get(i).size(); j++){
                    Double tmp = r.get(i).get(j)*mInputLayout.getValueCoef();
                    r.get(i).set(j, tmp);
                }
            }
            return r;
        } else {
            return null;
        }
    }
    public Variable getVariable(){
        Variable var = new Variable();
        var.setValue(mDetailedButton.isShown()? 0 : mInputLayout.getMainValue()*mInputLayout.getValueCoef());
        if (mIsConstant.getConstant()){
            var.setDevs(0,0);
        } else {
            var.setDevs(mInputLayout.getMaxValue(), mInputLayout.getMinValue());
        }
        return  var;
    }

    public ValueWithDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
    public void setTitle(int id){
        mTitleTextView.setText(mResources.getString(id));
    }
}
