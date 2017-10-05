package com.havrylyuk.dou.ui.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * SalariesViewPager custom not swiped view pager
 * Created by Igor Havrylyuk on 04.10.2017.
 */

public class SalariesViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public SalariesViewPager(Context context) {
        super(context);
    }

    public SalariesViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

}
