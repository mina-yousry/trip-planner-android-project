package com.example.hazem.facebooklogin.model;

import android.content.Context;

import com.example.hazem.facebooklogin.contract.FloatingServiceInterface;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.presenters.FloatingServicePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mina Yousry on 3/13/2018.
 */

public class FloatingServiceModel implements FloatingServiceInterface.Model {

    private AppDatabase database;
    private Context c;
    private ArrayList<Note> notes;
    private FloatingServiceInterface.Presenter presenter;

    private transient FirebaseUser user;
    private transient FirebaseAuth mAuth;
    private DatabaseReference updateOrDeleteRef;

    public FloatingServiceModel(Context c) {
        this.c = c;
        database = AppDatabase.getDatabase(c);
    }

    @Override
    public void fetchNotes(String id) {
        notes = (ArrayList<Note>) database.getNoteDao().getAll(id);
        presenter.notifyFloatingServiceNotesFitched(notes);
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
    public void setPresenter(FloatingServicePresenter floatingServicePresenter) {
        this.presenter = floatingServicePresenter;
    }
}
