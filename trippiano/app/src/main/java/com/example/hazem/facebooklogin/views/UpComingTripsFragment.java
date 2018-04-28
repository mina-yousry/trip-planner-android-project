package com.example.hazem.facebooklogin.views;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.hazem.facebooklogin.MyBroadcastReceiver;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.contract.HomeInterface;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.model.HomeModel;
import com.example.hazem.facebooklogin.presenters.UpComingTripsPresenter;
import com.example.hazem.facebooklogin.utility.BottomOffsetDecoration;
import com.example.hazem.facebooklogin.utility.ScrollHandler;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingTripsFragment extends Fragment implements HomeInterface.View, CancelTripForTripDetails {

    private View upComingTripsView;
    private ObservableRecyclerView tripsRecycleView;
    private RecyclerView.LayoutManager upComingTripsLayoutManager;
    private ArrayList<Trip> upcomingTrips;
    private UpComingTripsRecycleViewAdapter upComingTripsRecycleViewAdapter;
    private HomeInterface.Presenter upComingTripsPresenter;
    private Trip trip;
    private int rotation;
    private TripDetailsFragment tripDetailsFragment;
    private FragmentManager fmngr;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Boolean landscape;
    public final static String UPCOMING_TRIPS_FRAGMENT = "up_coming_trips_fragment";
    private SharedPreferences pref;
    private ConstraintLayout defaultConstraintLayout;
    private Button defaultAddButton;
    private Boolean noTrips = true;
    //sync
    private DatabaseReference tripsRef;
    private DatabaseReference notesRef;
    private DatabaseReference tripref;
    private DatabaseReference updateOrDeleteRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private List<Trip> fireBaseList;
    private List<Trip> roomBaseList;
    private AppDatabase database;
    private List<Trip> diff;
    //sync
    private int tripId = 0;

    public UpComingTripsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Display display = ((WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE)).getDefaultDisplay();
        rotation = display.getRotation();

        if (rotation == Surface.ROTATION_0) {
            upComingTripsView = inflater.inflate(R.layout.fragment_up_coming_trips, container, false);
            landscape = false;
        } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            landscape = true;
            upComingTripsView = inflater.inflate(R.layout.home_land_scape, container, false);
            tripDetailsFragment = new TripDetailsFragment();
            fmngr = getFragmentManager();
            FragmentTransaction ft = fmngr.beginTransaction();
            ft.replace(R.id.trip_details_fragment, tripDetailsFragment, "TripDetails");
            ft.commit();
        }

        defaultConstraintLayout = (ConstraintLayout) upComingTripsView.findViewById(R.id.default_layout);
        defaultAddButton = (Button) upComingTripsView.findViewById(R.id.btn_add_default);
        tripsRecycleView = (ObservableRecyclerView) upComingTripsView.findViewById(R.id.trips_recycler_view);
        tripsRecycleView.setScrollViewCallbacks(new ScrollHandler());
        tripsRecycleView.setHasFixedSize(true);
        upComingTripsLayoutManager = new LinearLayoutManager(getContext());
        tripsRecycleView.setLayoutManager(upComingTripsLayoutManager);


        defaultAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTrip.class);
                startActivity(intent);
            }
        });

        float offsetPx = 300;
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        tripsRecycleView.addItemDecoration(bottomOffsetDecoration);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        //////////////////////////////////////try refresh////////////////////////////////////////////
        mySwipeRefreshLayout = (SwipeRefreshLayout) upComingTripsView.findViewById(R.id.refresh_upComing_trips);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.com_facebook_blue);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncronize();
            }
        });
        ////////////////////////////////////try refresh//////////////////////////////////////////////////
        return upComingTripsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //sync///////////////////////////////////////////////////////////////
        fireBaseList = new ArrayList<>();
        roomBaseList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        tripref = FirebaseDatabase.getInstance().getReference(user.getUid());
        database = AppDatabase.getDatabase(getContext());
        //sync/////////////////////////////////////////////////////////////////
//        upcomingTrips = new ArrayList<>();
//        upComingTripsPresenter = new UpComingTripsPresenter(this, new HomeModel(getContext()));
//        upComingTripsRecycleViewAdapter = new UpComingTripsRecycleViewAdapter(upcomingTrips, this, getContext());
//        tripsRecycleView.setAdapter(upComingTripsRecycleViewAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("home_problrm","fetch trips called from upcoming fragment");
        upcomingTrips = new ArrayList<>();
        upComingTripsPresenter = new UpComingTripsPresenter(this, new HomeModel(getContext()));
        upComingTripsRecycleViewAdapter = new UpComingTripsRecycleViewAdapter(upcomingTrips, this, getContext());
        tripsRecycleView.setAdapter(upComingTripsRecycleViewAdapter);
        upComingTripsPresenter.fetchTrips();
    }

    @Override
    public void setTrips(List<Trip> allTrips) {
        Log.i("home_problrm","set trips called");
        if (allTrips.size() == 0 && noTrips == true) {
            defaultConstraintLayout.setVisibility(View.VISIBLE);
            tripsRecycleView.setVisibility(View.GONE);
        } else {
            noTrips = false;
            defaultConstraintLayout.setVisibility(View.GONE);
            tripsRecycleView.setVisibility(View.VISIBLE);
            mySwipeRefreshLayout.setRefreshing(false);
            for (int i = 0; i < allTrips.size(); i++) {
                upcomingTrips.add(allTrips.get(i));
                upComingTripsRecycleViewAdapter.notifyDataSetChanged();
            }
            if (tripId != 0) {
                Trip detailedTrip = null;
                for (int i = 0; i < upcomingTrips.size(); i++) {
                    if (upcomingTrips.get(i).getTid() == tripId) {
                        detailedTrip = upcomingTrips.get(i);
                        showTripDetails(detailedTrip);
                    }
                }
            }
        }
    }

    @Override
    public void setNotes(ArrayList<Note> notes) {
        upComingTripsRecycleViewAdapter.setNotesToPopUPMenu(notes);
    }

    public void checkPermission(Trip t) {
        trip = t;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + t.getEndLat() + "," + t.getEndLong());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, 2084);
        } else {
            trip.setTripStatus(1);
            upcomingTrips.remove(trip);
            upComingTripsRecycleViewAdapter.notifyDataSetChanged();
            if (upcomingTrips.size() == 0) {
                noTrips = true;
            }
            updateTrip(trip);
            Bundle myBundle = new Bundle();
            myBundle.putSerializable(TripDetails.CLICKED_TRIP, t);
            Intent i = new Intent(getApplicationContext(), FloatingService.class);
            i.putExtra(TripDetails.CLICKED_TRIP, myBundle);
            getApplicationContext().startService(i);
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + t.getEndLat() + "," + t.getEndLong());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2084) {
            if (Settings.canDrawOverlays(getContext())) {
                trip.setTripStatus(1);
                upcomingTrips.remove(trip);
                upComingTripsRecycleViewAdapter.notifyDataSetChanged();
                if (upcomingTrips.size() == 0) {
                    noTrips = true;
                }
                updateTrip(trip);
                Bundle myBundle = new Bundle();
                myBundle.putSerializable(TripDetails.CLICKED_TRIP, trip);
                Intent i = new Intent(getApplicationContext(), FloatingService.class);
                i.putExtra(TripDetails.CLICKED_TRIP, myBundle);
                getApplicationContext().startService(i);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void fetchNotes(String id) {
        upComingTripsPresenter.getTripNotes(id);
    }

    public void updateNotes(ArrayList<Note> notes, Trip t) {
        upComingTripsPresenter.updateNotes(notes, t);
    }
    public void detletTrip(Trip trip) {

        upComingTripsPresenter.deleteTrip(trip);
        upcomingTrips.remove(trip);
        upComingTripsRecycleViewAdapter.notifyDataSetChanged();
        if (upcomingTrips.size() == 0) {
            noTrips = true;
        }
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

    public List<Trip> syncronize() {

        tripref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataS : dataSnapshot.getChildren()) {

                    tripsRef = FirebaseDatabase.getInstance().getReference(user.getUid() + "/" + dataS.getKey() + "/trip");
                    notesRef = FirebaseDatabase.getInstance().getReference(user.getUid() + "/" + dataS.getKey() + "/notes");

                    notesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //int tid = database.getNoteDao().getMaxId();
                            List<Note> roomNotes = database.getNoteDao().getAllNotes();
                            for (DataSnapshot dataS : dataSnapshot.getChildren()) {
                                Note note = dataS.getValue(Note.class);
                                boolean addNote = true;
                                for(int i=0;i<roomNotes.size();i++){
                                    if(note.getTid().equals(roomNotes.get(i).getTid())){
                                        addNote = false;
                                    }
                                }
                                if(addNote){
                                    int nid = database.getNoteDao().getMaxNoteId();
                                    //note.setTid(tid+1);
                                    note.setNid(nid + 1);
                                    database.getNoteDao().addNote(note);
                                    Log.i("onChildAdded", note.toString() + " notes to be added ");
                                }


                            }
                            notesRef.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    Log.i("onChildAdded",tripsRef + " outside the listner ");
                    tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            diff = new ArrayList<Trip>();
                            Trip firbaseTrip = dataSnapshot.getValue(Trip.class);
//                            Log.i("onChildAdded",firbaseTrip.toString() + " inside the listner ");
                            roomBaseList = database.getTripDao().getAllTrips(pref.getString("user_id", ""));
//                            Log.i("onChildAdded",firbaseTrip.toString() + " inside the listner ");
                            boolean add = true;
                            for (int j = 0; j < roomBaseList.size(); j++) {
                                Trip roomTrip = roomBaseList.get(j);
                                if (firbaseTrip.getYear() == roomTrip.getYear() && firbaseTrip.getMonth() == roomTrip.getMonth()
                                        && firbaseTrip.getDay() == roomTrip.getDay() && firbaseTrip.getHour() == roomTrip.getHour()
                                        && firbaseTrip.getMinute() == roomTrip.getMinute()) {
                                    add = false;
                                }
                            }
                            if (add) {
//                                Log.i("onChildAdded","inside add");
                                int id = database.getTripDao().getMaxId();
                                firbaseTrip.setTid(id + 1);
                                if (firbaseTrip.getTripStatus() == 0) {
                                    firbaseTrip = saveAlarm(firbaseTrip);
                                    if (firbaseTrip.getTripStatus() == 0) {
                                        diff.add(firbaseTrip);
                                    } else {
                                        ((HomeScreen) getActivity()).refreshHistory(firbaseTrip);
                                    }
                                } else {
                                    ((HomeScreen) getActivity()).refreshHistory(firbaseTrip);
                                }
                                database.getTripDao().addTrip(firbaseTrip);

                                Log.i("onChildAdded", firbaseTrip.toString() + " trip to be added ");
                            }
                            setTrips(diff);
                            tripsRef.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }

                        public Trip saveAlarm(Trip tr) {
                            int _id = tr.getTid();
                            Intent intent = new Intent(getContext(), MyBroadcastReceiver.class);
                            intent.putExtra("tripid", _id);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                            long currentTime = System.currentTimeMillis();
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.YEAR, tr.getYear());
                            c.set(Calendar.MONTH, tr.getMonth());
                            c.set(Calendar.DAY_OF_MONTH, tr.getDay());
                            c.set(Calendar.HOUR_OF_DAY, tr.getHour());
                            c.set(Calendar.MINUTE, tr.getMinute());

                            long interval = c.getTimeInMillis() - currentTime;
                            if (interval > 0) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);

                                Toast.makeText(getContext(), "trip saved", Toast.LENGTH_SHORT).show();

                                Toast.makeText(getContext(), "ctms: " + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();

                                Toast.makeText(getContext(), "interval: " + interval, Toast.LENGTH_SHORT).show();

                            } else {
                                tr.setTripStatus(1);
                            }
                            return tr;
                        }
                    });
                }
                tripref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return diff;
    }

    @Override
    public void cancelTrip(Trip trip) {
        upcomingTrips.remove(trip);
        upComingTripsRecycleViewAdapter.notifyDataSetChanged();
        if (upcomingTrips.size() == 0) {
            noTrips = true;
        }
        trip.setTripStatus(2);

        //cancel alarm
        Intent intent = new Intent(getContext(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(), trip.getTid(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager1 = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager1.cancel(pendingIntent1);
        String firebaseId = trip.getFirebaseKey();
        if (trip.getStatus() == 1) {
            String ref = user.getUid() + "/" + firebaseId;
            updateOrDeleteRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child(firebaseId);

            HashMap<String, Object> myMap = new HashMap<>();
            myMap.put("trip", trip);

            String tID = String.valueOf(trip.getYear()) + String.valueOf(trip.getMonth()) + String.valueOf(trip.getDay()) +
                    String.valueOf(trip.getHour()) + String.valueOf(trip.getMinute());
            myMap.put("notes", database.getNoteDao().getAll(tID));


            updateOrDeleteRef.setValue(myMap);
        }


        updateTrip(trip);
    }

    public void updateTrip(Trip trip) {
        upComingTripsPresenter.updateTrip(trip);
    }

    public void showTripDetails(Trip t) {
        if (rotation == Surface.ROTATION_0) {
            Bundle myBundle = new Bundle();
            myBundle.putSerializable(TripDetails.CLICKED_TRIP, t);
            Intent myIntent = new Intent(getContext(), TripDetails.class);
            myIntent.putExtra(TripDetails.CLICKED_TRIP, myBundle);
            getContext().startActivity(myIntent);
        } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            tripDetailsFragment.showTrip(t, this);
        }

    }

    public Boolean isLandScape() {
        return landscape;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

}
