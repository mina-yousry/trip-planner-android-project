package com.example.hazem.facebooklogin.presenters;

import com.example.hazem.facebooklogin.contract.TripDetails;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;

import java.util.ArrayList;

/**
 * Created by Mina Yousry on 3/17/2018.
 */


public class TripDetailsPresenter implements TripDetails.Presenter {

    private TripDetails.Model myModel;
    private TripDetails.View myView;

    public TripDetailsPresenter(TripDetails.Model myModel,TripDetails.View myView) {
        this.myModel = myModel;
        this.myView = myView;
    }


    @Override
    public void notifiyViewNotesFetched(ArrayList<Note> notes) {
        myView.showNotesPopUp(notes);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes, Trip t) {
        myModel.updateNotes(notes,t);
    }

    @Override
    public void deleteTrip(Trip trip) {
        myModel.deleteTrip(trip);
    }

    @Override
    public void updateTrip(Trip trip) {
        myModel.updateTrip(trip);
    }

    @Override
    public void fetchNotes(String tid) {
        myModel.getTripNotes(tid);
    }
}
