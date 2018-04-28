package com.example.hazem.facebooklogin.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hazem.facebooklogin.contract.TripDetails;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mina Yousry on 3/17/2018.
 */

public class TripDetailsModel implements TripDetails.Model {

    private AppDatabase database;
    private TripDetails.Presenter myPresenter;
    SharedPreferences pref;
    private transient FirebaseUser user;
    private transient FirebaseAuth mAuth;
    private DatabaseReference updateOrDeleteRef;

    public TripDetailsModel(Context context) {
        database = AppDatabase.getDatabase(context);
    }

    @Override
    public boolean deleteTrip(Trip trip) {
        database.getTripDao().delete(trip);
        return true;
    }

    @Override
    public void getTripNotes(String tripId) {
        ArrayList<Note> notes = (ArrayList<Note>) database.getNoteDao().getAll(tripId);
        myPresenter.notifiyViewNotesFetched(notes);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes, Trip trip) {
        for (int i=0;i<notes.size();i++){
            database.getNoteDao().addNote(notes.get(i));
        }
        //update to fire base

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String firebaseId = trip.getFirebaseKey();
        if(trip.getStatus() == 1){
            String ref = user.getUid() + "/" + firebaseId;
            updateOrDeleteRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child(firebaseId);

            HashMap<String,Object> myMap = new HashMap<>();
            myMap.put("trip",trip);

            String tID = String.valueOf(trip.getYear()) + String.valueOf(trip.getMonth()) + String.valueOf(trip.getDay()) +
                    String.valueOf(trip.getHour()) + String.valueOf(trip.getMinute()) ;
            myMap.put("notes",database.getNoteDao().getAll(tID));


            updateOrDeleteRef.setValue(myMap);
        }
    }

    @Override
    public void updateTrip(Trip trip) {
        database.getTripDao().addTrip(trip);
    }

    public void setMyPresenter(TripDetails.Presenter myPresenter) {
        this.myPresenter = myPresenter;
    }
}
