package com.retailer.oneops.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NavigationViewPagerAdapter extends ViewPager {
 private boolean enabled;

 public NavigationViewPagerAdapter(Context context, AttributeSet attrs) {
   super(context, attrs);
   this.enabled = true;
 }

 @Override
 public boolean onTouchEvent(MotionEvent event) {
   if (this.enabled) {
     return super.onTouchEvent(event);
   }
   return false;
 }

 @Override
 public boolean onInterceptTouchEvent(MotionEvent event) {
   if (this.enabled) {
     return super.onInterceptTouchEvent(event);
   }
   return false;
 }

 public void setPagingEnabled(boolean enabled) {
   this.enabled = enabled;
 }
}