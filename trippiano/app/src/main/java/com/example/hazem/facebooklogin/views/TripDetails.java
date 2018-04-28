package com.example.hazem.facebooklogin.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.entity.Trip;

public class TripDetails extends AppCompatActivity implements ViewTripInt{

    private TripDetailsFragment detailsFragment;
    private UpComingTripsFragment upComingTripsFragment;

    public static final String CLICKED_TRIP = "clicked_trip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        detailsFragment = (TripDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        Intent i = getIntent();
        Bundle clickedTripBundle = i.getBundleExtra(TripDetails.CLICKED_TRIP);
        Trip clickedTrip = (Trip) clickedTripBundle.get(TripDetails.CLICKED_TRIP);
        upComingTripsFragment = (UpComingTripsFragment)clickedTripBundle.get(UpComingTripsFragment.UPCOMING_TRIPS_FRAGMENT);
        sendTripToFragment(clickedTrip);
    }

    @Override
    public void sendTripToFragment(Trip t) {
        detailsFragment.showTrip(t,upComingTripsFragment);
    }
}
