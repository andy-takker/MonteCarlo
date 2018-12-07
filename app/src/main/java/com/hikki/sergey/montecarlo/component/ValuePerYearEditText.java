package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hikki.sergey.montecarlo.R;

public class ValuePerYearEditText extends EditText {
    private Resources mResources;
    private Context mContext;

    public ValuePerYearEditText(Context context) {
        super(context, null);
        mResources = getResources();
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setHint(mResources.getString(R.string.inputValue));
        this.setEms(3);
        this.setSingleLine();
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        this.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    public ValuePerYearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
