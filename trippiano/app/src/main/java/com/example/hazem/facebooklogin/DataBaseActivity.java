package com.example.hazem.facebooklogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataBaseActivity extends AppCompatActivity {


    private Trip trip;
    private AppDatabase database;

    //private DatabaseReference mDatabase;

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase1;
    private DatabaseReference tripref;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser user ;
    private FirebaseAuth mAuth;
    private Query getTrips;
    TextView tripName;
    Button saveLocal;
    Button loadLocal;
    Button saveFireBase;
    Button loadFireBase;
    private ArrayList<Trip> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        tripName = findViewById(R.id.TripName);
        saveLocal = findViewById(R.id.savelocal);
        loadLocal = findViewById(R.id.loadlocal);
        saveFireBase = findViewById(R.id.saveFireBase);
        loadFireBase =findViewById(R.id.loadfirebase);
        tripList = new ArrayList<>();
        database = AppDatabase.getDatabase(getApplicationContext());
        database.getTripDao();


        tripref = FirebaseDatabase.getInstance().getReference(user.getUid());

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Trip Planner");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });



        saveLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = database.getTripDao().getMaxId();
                Trip trip = new Trip(id+1,tripName.getText().toString(),29.914093,31.200921,29.924736,31.198370,"my Notes");
                database.getTripDao().addTrip(trip);
                Toast.makeText(DataBaseActivity.this, "Trip saved" , Toast.LENGTH_SHORT).show();
            }
        });

        loadLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Trip> tr = database.getTripDao().getAll(user.getUid());
                if(tr.size()==0){
                    Toast.makeText(DataBaseActivity.this, "no saved trips" , Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0;i<tr.size();i++){
                        Toast.makeText(DataBaseActivity.this, "trip name is : " +tr.get(i).getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        saveFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDatabase.child("trips").setValue(tripName.getText().toString());
                // get reference to 'users' node
                int id = database.getTripDao().getMaxId();
                Trip trip = new Trip(id,tripName.getText().toString(),29.914093,31.200921,29.924736,31.198370,"Mynotes");
                mAuth = FirebaseAuth.getInstance();

                //mFirebaseDatabase = mFirebaseInstance.getReference(user.getUid());
                Log.i("firebase","" + user.getUid());
                mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                mFirebaseDatabase1 = FirebaseDatabase.getInstance().getReference();
                mFirebaseDatabase1 = mFirebaseDatabase.child(user.getUid()).push();
                mFirebaseDatabase1.setValue(trip);

                Toast.makeText(DataBaseActivity.this, "Trip saved" , Toast.LENGTH_SHORT).show();
            }
        });


        loadFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(tripList.size()==0){
                    Toast.makeText(DataBaseActivity.this, "no saved trips" , Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0;i<tripList.size();i++){
                        Toast.makeText(DataBaseActivity.this, "trip name is : " +tripList.get(i).getName(), Toast.LENGTH_SHORT).show();
                    }
                }

                //mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child(user.getUid());
                //getTrips = mFirebaseDatabase;
                //Toast.makeText(DataBaseActivity.this, getTrips.toString() , Toast.LENGTH_SHORT).show();

            }
        });
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Trip trip2 = dataSnapshot.getValue(Trip.class);
                tripList.add(trip2);
                Log.i("onChildAdded",trip2+"");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        tripref.addChildEventListener(childEventListener);
    }
}
