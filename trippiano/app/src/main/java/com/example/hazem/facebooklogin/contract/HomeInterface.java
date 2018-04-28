package com.example.hazem.facebooklogin.contract;

import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.views.TripDetailsFragment;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazem on 3/9/2018.
 */

public interface HomeInterface {
    interface View{
        void setTrips(List<Trip> allTrips);
        void setNotes(ArrayList<Note> notes);
    }
    interface Model{

        void getHomeTrips();
        boolean deleteTrip(Trip trip);
        void getTripNotes(String tripId);
        void updateNotes(ArrayList<Note> notes,Trip t);
        void updateTrip(Trip trip);
    }
    interface Presenter{
        void fetchTrips();
        void onDataFinish(List<Trip> allTrips);
        void getTripNotes(String tripID);
        void notifiyViewNotesFetched(ArrayList<Note> notes);
        void updateNotes(ArrayList<Note> notes,Trip t);
        void deleteTrip(Trip trip);
        void updateTrip(Trip trip);

    }

}
