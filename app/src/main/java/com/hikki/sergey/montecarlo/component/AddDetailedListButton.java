package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.hikki.sergey.montecarlo.R;

public class AddDetailedListButton extends ImageButton {
    private Resources mResources;
    private Context mContext;
    private boolean mIsShown = false;

    public AddDetailedListButton(Context context) {
        super(context, null);
        mContext = context;
        mResources = getResources();
        setImage();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        this.setLayoutParams(layoutParams);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShown = !mIsShown;
                setImage();
            }
        });
    }
    public AddDetailedListButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setImage(){
        if (mIsShown){
            this.setImageDrawable(mResources.getDrawable(R.drawable.image_drop_up));
        } else {
            this.setImageDrawable(mResources.getDrawable(R.drawable.image_drop_down));
        }

    }

    @Override
    public boolean isShown() {
        return mIsShown;
    }

    public void setShown(boolean shown) {
        mIsShown = shown;
    }
}
