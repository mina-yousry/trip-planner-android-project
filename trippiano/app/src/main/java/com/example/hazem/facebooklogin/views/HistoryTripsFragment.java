package com.example.hazem.facebooklogin.views;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.hazem.facebooklogin.MyBroadcastReceiver;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.contract.HistoryInterface;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.model.HistoryModel;
import com.example.hazem.facebooklogin.presenters.HistoryPresenter;
import com.example.hazem.facebooklogin.utility.BottomOffsetDecoration;
import com.example.hazem.facebooklogin.utility.ScrollHandler;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTripsFragment extends Fragment implements HistoryInterface.View, CancelTripForTripDetails {

    private View historyTripsView;
    private ObservableRecyclerView historyTripsRecycleView;
    private LinearLayoutManager historyTripsLayoutManager;
    private ArrayList<Trip> historyTrips;
    private HistoryTripsViewAdapter historyTripsRecycleViewAdapter;
    private HistoryInterface.Presenter historyTripsPresenter;
    private Trip trip;
    private boolean landscape;
    private int rotation;
    private HistoryDetailsFragment tripDetailsFragment;
    private FragmentManager fmngr;
    private boolean landScape = false;
    private int tripId;

    private DatabaseReference updateOrDeleteRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    public HistoryTripsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Display display = ((WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE)).getDefaultDisplay();
        rotation = display.getRotation();

        if (rotation == Surface.ROTATION_0) {
            historyTripsView = inflater.inflate(R.layout.fragment_history_trips, container, false);
            landScape = false;

        } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            landScape = true;
            historyTripsView = inflater.inflate(R.layout.history_fragment_landscape, container, false);
            tripDetailsFragment = new HistoryDetailsFragment();
            fmngr = getFragmentManager();
            FragmentTransaction ft = fmngr.beginTransaction();
            ft.replace(R.id.history_trip_details_fragment, tripDetailsFragment, "TripDetails");
            ft.commit();
        }

        historyTripsRecycleView = (ObservableRecyclerView) historyTripsView.findViewById(R.id.history_trips_recycler_view);
        historyTripsRecycleView.setScrollViewCallbacks(new ScrollHandler());
        historyTripsRecycleView.setHasFixedSize(true);
        historyTripsLayoutManager = new LinearLayoutManager(getContext());
        historyTripsRecycleView.setLayoutManager(historyTripsLayoutManager);
        float offsetPx = 300;
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        historyTripsRecycleView.addItemDecoration(bottomOffsetDecoration);
        Log.i("historytest", "inside on create");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        return historyTripsView;

    }

    @Override
    public void onStart() {
        super.onStart();
//        historyTrips = new ArrayList<>();
//        historyTripsPresenter = new HistoryPresenter(new HistoryModel(getContext()), this);
//        historyTripsRecycleViewAdapter = new HistoryTripsViewAdapter(this, historyTrips, getContext());
//        historyTripsRecycleView.setAdapter(historyTripsRecycleViewAdapter);
        Log.i("historytest", "inside on start");
    }

    @Override
    public void onResume() {
        super.onResume();
        historyTrips = new ArrayList<>();
        historyTripsPresenter = new HistoryPresenter(new HistoryModel(getContext()), this);
        historyTripsRecycleViewAdapter = new HistoryTripsViewAdapter(this, historyTrips, getContext());
        historyTripsRecycleView.setAdapter(historyTripsRecycleViewAdapter);
        historyTripsPresenter.fetchHistoryTrips();
        Log.i("historytest", "inside on resume");
    }

    @Override
    public void setHistoryTrips(List<Trip> allTrips) {
        for (int i = 0; i < allTrips.size(); i++) {
            historyTrips.add(allTrips.get(i));
            historyTripsRecycleViewAdapter.notifyDataSetChanged();
        }

        if (tripId != 0) {
            Trip detailedTrip = null;
            for (int i = 0; i < historyTrips.size(); i++) {
                if (historyTrips.get(i).getTid() == tripId) {
                    detailedTrip = historyTrips.get(i);
                    showTripDetails(detailedTrip);
                }
            }
        }
    }

    @Override
    public void setNotes(ArrayList<Note> notes) {
        historyTripsRecycleViewAdapter.setNotesToPopUPMenu(notes);
    }


    public void fetchNotes(String id) {
        historyTripsPresenter.getTripNotes(id);
    }

    public void updateNotes(ArrayList<Note> notes, Trip t) {
        historyTripsPresenter.updateNotes(notes, t);
    }

    public void detletTrip(Trip trip) {
        historyTripsPresenter.deleteTrip(trip);
        Intent intent = new Intent(getContext(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(), trip.getTid(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager1 = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager1.cancel(pendingIntent1);
        String firebaseId = trip.getFirebaseKey();
        if (trip.getStatus() == 1) {
            String ref = user.getUid() + "/" + firebaseId;
            updateOrDeleteRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child(firebaseId);
            updateOrDeleteRef.setValue(null);
        }
    }

    public void addTrip(Trip trip) {
        historyTrips.add(trip);
        historyTripsRecycleViewAdapter.notifyDataSetChanged();
    }

    public boolean isLandScape() {
        return landScape;
    }

    public void showTripDetails(Trip trip) {
        tripDetailsFragment.showTrip(trip, this);
    }

    @Override
    public void cancelTrip(Trip trip) {
    }

    public int getRotation() {
        return rotation;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}

