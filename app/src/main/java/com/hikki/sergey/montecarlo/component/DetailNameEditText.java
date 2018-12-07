package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hikki.sergey.montecarlo.R;

public class DetailNameEditText extends EditText {
    private Resources mResources;
    private Context mContext;

    public DetailNameEditText(Context context) {
        super(context, null);

        mResources = getResources();
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setHint(mResources.getString(R.string.inputName));
        this.setEms(4);
        this.setSingleLine();
        this.setInputType(InputType.TYPE_CLASS_TEXT);
        this.setScrollX(OVER_SCROLL_IF_CONTENT_SCROLLS);
        this.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
    }

    public DetailNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
