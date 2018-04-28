package com.example.hazem.facebooklogin.contract;

import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mina Yousry on 3/17/2018.
 */

public interface TripDetails {

    interface View{
        void showNotesPopUp(final ArrayList<Note> notes);
    }
    interface Model{
        boolean deleteTrip(Trip trip);
        void getTripNotes(String tripId);
        void updateNotes(ArrayList<Note> notes,Trip t);

        void updateTrip(Trip trip);
    }
    interface Presenter{
        void notifiyViewNotesFetched(ArrayList<Note> notes);
        void updateNotes(ArrayList<Note> notes,Trip t);
        void deleteTrip(Trip trip);
        void updateTrip(Trip trip);
        void fetchNotes(String tid);
    }
}
