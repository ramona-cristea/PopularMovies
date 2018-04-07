package com.popular.movies.details;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Custom View Pager for which swipe can be disabled
 * {@link "http://blog.iamsuleiman.com/bottom-navigation-bar-android-tutorial/"}
 */

public class DisabledSwipeViewPager extends ViewPager {
    private boolean enabled;

    public DisabledSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.enabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}