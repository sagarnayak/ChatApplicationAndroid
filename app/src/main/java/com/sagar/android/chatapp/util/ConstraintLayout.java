package com.sagar.android.chatapp.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ConstraintLayout extends androidx.constraintlayout.widget.ConstraintLayout {
    public ConstraintLayout(Context context) {
        super(context);
    }

    public ConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
