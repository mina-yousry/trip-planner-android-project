package com.example.hazem.facebooklogin.utility;

import android.util.Log;

import com.example.hazem.facebooklogin.views.HomeScreen;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

/**
 * Created by Mina Yousry on 3/14/2018.
 */

public class ScrollHandler implements ObservableScrollViewCallbacks {
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            Log.i("scroll", "up");
            if (HomeScreen.getHomeActionBar().isShowing()) {
                HomeScreen.getHomeActionBar().setDisplayShowHomeEnabled(false);
                HomeScreen.getHomeActionBar().setDisplayShowTitleEnabled(false);
            }
        } else if (scrollState == ScrollState.DOWN) {
            HomeScreen.getHomeActionBar().setDisplayShowHomeEnabled(true);
            HomeScreen.getHomeActionBar().setDisplayShowTitleEnabled(true);
        }

    }
}
