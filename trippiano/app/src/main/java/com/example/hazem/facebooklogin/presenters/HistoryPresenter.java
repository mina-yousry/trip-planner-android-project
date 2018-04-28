package com.example.hazem.facebooklogin.presenters;

import com.example.hazem.facebooklogin.contract.HistoryInterface;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mina Yousry on 3/14/2018.
 */

public class HistoryPresenter implements HistoryInterface.Presenter {

    private HistoryInterface.Model historyModel;
    private HistoryInterface.View historyViewer;

    public HistoryPresenter(HistoryInterface.Model historyModel, HistoryInterface.View historyViewer) {
        this.historyModel = historyModel;
        this.historyViewer = historyViewer;
    }

    @Override
    public void fetchHistoryTrips() {
        historyModel.setPresenter(this);
        historyModel.getHistoryTrips();
    }

    @Override
    public void onDataFinish(List<Trip> allTrips) {
        historyViewer.setHistoryTrips(allTrips);
    }

    @Override
    public void getTripNotes(String tripID) {
        historyModel.getTripNotes(tripID);
    }

    @Override
    public void notifiyViewNotesFetched(ArrayList<Note> notes) {
        historyViewer.setNotes(notes);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes , Trip t) {
        historyModel.updateNotes(notes,t);
    }

    @Override
    public void deleteTrip(Trip trip) {
        historyModel.deleteTrip(trip);
    }
}
