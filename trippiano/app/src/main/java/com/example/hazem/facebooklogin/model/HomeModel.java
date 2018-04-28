package com.example.hazem.facebooklogin.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hazem.facebooklogin.contract.HomeInterface;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.utility.MyVolleyRequester;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Hazem on 3/9/2018.
 */

public class HomeModel implements HomeInterface.Model {

    private HomeInterface.Presenter homePresenter;
    //private Trip trip;
    private List<Trip> tripList;
    private AppDatabase database;
    private Context c;
    SharedPreferences pref;
    private transient FirebaseUser user;
    private transient FirebaseAuth mAuth;
    private DatabaseReference updateOrDeleteRef;


    public HomeModel(Context context) {

        database = AppDatabase.getDatabase(context);
        c = context;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    }

    @Override
    public void getHomeTrips() {
        Log.i("home_problrm","get All called from model");
        ArrayList<Trip> trips = (ArrayList<Trip>) database.getTripDao().getAll(pref.getString("user_id",""));
        new MyVolleyRequester(this,null,c).getTripsImages(trips);
    }

    @Override
    public boolean deleteTrip(Trip trip) {
        database.getTripDao().delete(trip);
        return true;
    }

    @Override
    public void getTripNotes(String tripId) {
        ArrayList<Note> tripNotes = (ArrayList<Note>) database.getNoteDao().getAll(tripId);
        homePresenter.notifiyViewNotesFetched(tripNotes);
    }


    public void setHomePresenter(HomeInterface.Presenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public void notifyPresenter(ArrayList<Trip> tripsWithImages) {
        homePresenter.onDataFinish(tripsWithImages);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes ,Trip trip) {
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
}
