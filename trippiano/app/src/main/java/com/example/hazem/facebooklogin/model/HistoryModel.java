package com.example.hazem.facebooklogin.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hazem.facebooklogin.contract.HistoryInterface;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.presenters.HistoryPresenter;
import com.example.hazem.facebooklogin.utility.MyVolleyRequester;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Mina Yousry on 3/14/2018.
 */

public class HistoryModel implements HistoryInterface.Model {

    private Context c;
    private HistoryInterface.Presenter presenter;
    private AppDatabase database;
    SharedPreferences pref;

    private transient FirebaseUser user;
    private transient FirebaseAuth mAuth;
    private DatabaseReference updateOrDeleteRef;
    public HistoryModel(Context c) {
        this.c = c;
        database = AppDatabase.getDatabase(c);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

    }

    @Override
    public void getHistoryTrips() {
        ArrayList<Trip> Historytrips = (ArrayList<Trip>) database.getTripDao().getAllHistoryTrips(pref.getString("user_id",""));
        new MyVolleyRequester(null,this,c).getTripsImages(Historytrips);
    }

    @Override
    public boolean deleteTrip(Trip trip) {
        database.getTripDao().delete(trip);
        return true;
    }

    @Override
    public void getTripNotes(String tripId) {
        ArrayList<Note> tripNotes = (ArrayList<Note>) database.getNoteDao().getAll(tripId);
        presenter.notifiyViewNotesFetched(tripNotes);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes,Trip trip) {
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
    public void setPresenter(HistoryPresenter historyPresenter) {
        presenter = historyPresenter;
    }

    public void notifyPresenter(ArrayList<Trip> tripsWithImages) {
        presenter.onDataFinish(tripsWithImages);
    }
}
