package com.example.hazem.facebooklogin.presenters;

import android.util.Log;

import com.example.hazem.facebooklogin.contract.HomeInterface;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.model.HomeModel;
import com.example.hazem.facebooklogin.views.TripDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mina on 3/7/2018.
 */

public class UpComingTripsPresenter implements HomeInterface.Presenter {

    private HomeInterface.View upComingTripsViewer;
    private HomeModel upComingTripsModel;
    private TripDetailsFragment tripDetailsFragment;

    public UpComingTripsPresenter(HomeInterface.View upComingTripsViewer, HomeModel upComingTripsModel) {
        this.upComingTripsViewer = upComingTripsViewer;
        this.upComingTripsModel = upComingTripsModel;
    }


    @Override
    public void fetchTrips() {
        upComingTripsModel.setHomePresenter(this);
        Log.i("home_problrm","get home trips called from presenter");
        upComingTripsModel.getHomeTrips();
    }


    @Override
    public void onDataFinish(List<Trip> allTrips) {
        upComingTripsViewer.setTrips(allTrips);
    }

    @Override
    public void getTripNotes(String tripID) {
        upComingTripsModel.getTripNotes(tripID);
    }

    @Override
    public void notifiyViewNotesFetched(ArrayList<Note> notes) {
        upComingTripsViewer.setNotes(notes);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes,Trip t) {
        upComingTripsModel.updateNotes(notes,t);
    }

    @Override
    public void deleteTrip(Trip trip) {
        upComingTripsModel.deleteTrip(trip);
    }

    @Override
    public void updateTrip(Trip trip) {
        upComingTripsModel.updateTrip(trip);
    }

}
