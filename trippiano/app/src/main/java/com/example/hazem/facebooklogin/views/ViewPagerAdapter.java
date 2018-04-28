package com.example.hazem.facebooklogin.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hazem.facebooklogin.entity.Trip;

/**
 * Created by Mina on 3/4/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfPages = 3;
    private HistoryTripsFragment historyTripsFragment;
    private String tripStatus;

    private int tripId = 0;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment returningFragment = null;
        switch (position) {
            case 0:
                returningFragment = new UpComingTripsFragment();
                if (tripId != 0 && tripStatus .equals("home_trip")) {
                    ((UpComingTripsFragment) returningFragment).setTripId(tripId);
                }
                break;
            case 1:
                returningFragment = new HistoryTripsFragment();
                historyTripsFragment = (HistoryTripsFragment) returningFragment;
                if (tripId != 0 && tripStatus .equals( "not_home_trip")) {
                    ((HistoryTripsFragment) returningFragment).setTripId(tripId);
                }
                break;
            case 2:
                returningFragment = new ProfileFragment();
                break;
        }
        return returningFragment;
    }

    @Override
    public int getCount() {
        return numberOfPages;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void addTripToHistory(Trip trip) {
        if (historyTripsFragment != null) {
            historyTripsFragment.addTrip(trip);
        }
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }
}
