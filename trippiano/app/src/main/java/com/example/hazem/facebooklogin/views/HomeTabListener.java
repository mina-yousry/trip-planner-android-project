package com.example.hazem.facebooklogin.views;


import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

/**
 * Created by Mina on 3/4/2018.
 */

public class HomeTabListener implements ActionBar.TabListener {

    private ViewPager myAdapter;

    HomeTabListener(ViewPager adapter) {
        myAdapter=adapter;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

        myAdapter.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
