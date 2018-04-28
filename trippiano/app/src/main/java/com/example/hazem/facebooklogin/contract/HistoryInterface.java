package com.example.hazem.facebooklogin.contract;

import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.presenters.HistoryPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazem on 3/9/2018.
 */

public interface HistoryInterface {
    interface View{
        void setHistoryTrips(List<Trip> allTrips);
        void setNotes(ArrayList<Note> notes);
    }

    interface Model{
        void getHistoryTrips();
        boolean deleteTrip(Trip trip);
        void getTripNotes(String tripId);
        void updateNotes(ArrayList<Note> notes,Trip t);
        void setPresenter(HistoryPresenter historyPresenter);
    }
    interface Presenter{
        void fetchHistoryTrips();
        void onDataFinish(List<Trip> allTrips);
        void getTripNotes(String tripID);
        void notifiyViewNotesFetched(ArrayList<Note> notes);
        void updateNotes(ArrayList<Note> notes,Trip t);
        void deleteTrip(Trip trip);
    }
}
