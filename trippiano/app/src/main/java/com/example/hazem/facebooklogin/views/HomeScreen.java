package com.example.hazem.facebooklogin.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.entity.Trip;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;


public class HomeScreen extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager myViewPager;
    private static ActionBar homeActionBar;
    private HomeTabListener homeTabListener;
    private ObservableScrollView myScrollView;
    private static FloatingActionButton newTrip;
    private int tripId;
    public static final String HOME_TAB = "Home";
    public static final String HISTORY_TAB = "History";
    public static final String PROFILE_TAB = "Profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        myViewPager = (ViewPager) findViewById(R.id.home_pager);
        myViewPager.setAdapter(viewPagerAdapter);

        homeActionBar = getSupportActionBar();
        homeActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        homeTabListener = new HomeTabListener(myViewPager);
        homeActionBar.addTab(homeActionBar.newTab().setText(HomeScreen.HOME_TAB).setTabListener(homeTabListener));
        homeActionBar.addTab(homeActionBar.newTab().setText(HomeScreen.HISTORY_TAB).setTabListener(homeTabListener));
        homeActionBar.addTab(homeActionBar.newTab().setText(HomeScreen.PROFILE_TAB).setTabListener(homeTabListener));

        myViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                homeActionBar.setSelectedNavigationItem(position);
            }
        });

        newTrip = findViewById(R.id.fab);
        newTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this,AddTrip.class);
                startActivity(intent);
            }
        });

        Intent i = getIntent();
        if(i != null) {
            int tripId = i.getIntExtra(TripDetailsFragment.DETAILED_TRIP, 0);
            if (tripId != 0) {
                String tripState = i.getStringExtra(TripDetailsFragment.DETAILED_TRIP_STATUS);
                viewPagerAdapter.setTripId(tripId);
                if(tripState!=null){
                    if(tripState .equals("not_home_trip") ){
                        viewPagerAdapter.setTripStatus("not_home_trip");
                        myViewPager.setCurrentItem(1);
                    }else {
                        viewPagerAdapter.setTripStatus("home_trip");
                        myViewPager.setCurrentItem(0);
                    }
                }
            }
        }

    }

    public static ActionBar getHomeActionBar() {
        return homeActionBar;
    }
    public static FloatingActionButton getHomeActionButton(){return newTrip;};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getTripId() {
        return tripId;
    }

    public void refreshHistory(Trip trip){
        viewPagerAdapter.addTripToHistory(trip);
    }
}
