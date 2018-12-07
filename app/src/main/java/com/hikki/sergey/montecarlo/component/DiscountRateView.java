package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hikki.sergey.montecarlo.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class DiscountRateView extends LinearLayout {
    private Resources mResources;
    private Context mContext;

    private InputStdLayout mInputLayout;
    private IsConstantView mIsConstant;

    public DiscountRateView(Context context) {
        super(context, null);
        mContext = context;
        mResources = getResources();

        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
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

        TextView titleTextView = new TextView(mContext);
        titleTextView.setTextSize(COMPLEX_UNIT_SP, 18);
        titleTextView.setTextColor(Color.BLACK);
        titleTextView.setText(mResources.getString(R.string.discount_rate));
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        titleLayout.addView(titleTextView);

        mInputLayout = new InputStdLayout(mContext);
        titleLayout.addView(mInputLayout);

        Divider divider = new Divider(mContext, Divider.HORIZONTAL);
        this.addView(divider);
    }

    public DiscountRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getDps(int pixels){
        float scale = mResources.getDisplayMetrics().density;
        int dpAsPixels = (int) (pixels*scale + 0.5f);
        return dpAsPixels;
    }
    public InputStdLayout getRate(){
        return mInputLayout;
    }

    public Variable getDiskountRate(){
        Variable var = new Variable();
        if (mIsConstant.getConstant()){
            var.setDevs(0, 0);
        } else {
            var.setDevs(mInputLayout.getMaxValue(),mInputLayout.getMinValue());
        }
        var.setValue(mInputLayout.getMainValue());

        return var;
    }
}
