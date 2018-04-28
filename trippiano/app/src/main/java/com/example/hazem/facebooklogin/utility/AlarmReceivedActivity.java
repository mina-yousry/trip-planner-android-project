package com.example.hazem.facebooklogin.utility;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.hazem.facebooklogin.MainActivity;
import com.example.hazem.facebooklogin.MyBroadcastReceiver;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.views.FloatingService;
import com.example.hazem.facebooklogin.views.HomeScreen;
import com.example.hazem.facebooklogin.views.TripDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlarmReceivedActivity extends AppCompatActivity {


    LoadMP3 mp3;
    boolean isBound = false;
    private AppDatabase database;
    private Trip trip;
    private transient FirebaseUser user;
    private transient FirebaseAuth mAuth;
    private DatabaseReference updateOrDeleteRef;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        Intent intent11 = getIntent();
        int id = intent11.getIntExtra("tripid",1);
        database = AppDatabase.getDatabase(getApplicationContext());
        trip = database.getTripDao().getTripByID(id);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //play mp3
        ServiceConnection myConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LoadMP3.LocalBinder myBinder = (LoadMP3.LocalBinder)iBinder;
                mp3 = myBinder.getService();


                mp3.start();


                isBound = true;
                Toast.makeText(AlarmReceivedActivity.this, "onServiceConnected....", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound = false;
            }
        };

        if(pref.getBoolean("first_dialouge",true)){
            Intent intent = new Intent(this,LoadMP3.class);
            bindService(intent,myConnection, Context.BIND_AUTO_CREATE);
        }


        dialouge();
        //setContentView(R.layout.activity_alarm_received);

    }


    public void dialouge(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmReceivedActivity.this);
        builder.setTitle(trip.getName());
        builder.setPositiveButton("start later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                if(pref.getBoolean("first_dialouge",true)){
                    mp3.stop();
                    mp3.onDestroy();
                }

                // User clicked cancel button
                Toast.makeText(AlarmReceivedActivity.this, "start later pressed....", Toast.LENGTH_LONG).show();
                //notification section
                final int _id = trip.getTid();
                Intent notifyRespond = new Intent(AlarmReceivedActivity.this, AlarmReceivedActivity.class);

                notifyRespond.putExtra("tripid",_id);
                //notifyRespond.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // use System.currentTimeMillis() to have a unique ID for the pending intent
                PendingIntent pIntent = PendingIntent.getActivity(AlarmReceivedActivity.this, _id, notifyRespond, PendingIntent.FLAG_ONE_SHOT);
                // build notification
                // the addAction re-use the same intent to keep the example short
                Notification n  = new Notification.Builder(AlarmReceivedActivity.this)
                        .setContentTitle(trip.getName() )
                        .setContentText("do not forget your trip")
                        .setSmallIcon(R.drawable.log2)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true).setOngoing(true)
                        //.addAction(R.drawable.album1, "Call", pIntent)
                        //.addAction(R.drawable.album2, "More", pIntent)
                        //.addAction(R.drawable.album3, "And more", pIntent)
                        .build();

                NotificationManager notificationManager =
                        (NotificationManager) AlarmReceivedActivity.this.getSystemService(NOTIFICATION_SERVICE);


                notificationManager.notify(_id, n);
                editor.putBoolean("first_dialouge", false);
                editor.commit();

                finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if(pref.getBoolean("first_dialouge",true)){
                    mp3.stop();
                    mp3.onDestroy();
                }
                trip.setTripStatus(2);
                database.getTripDao().addTrip(trip);
                // User cancelled the dialog
                //cancel alarm
                String tID = String.valueOf(trip.getYear()) + String.valueOf(trip.getMonth()) + String.valueOf(trip.getDay()) +
                        String.valueOf(trip.getHour()) + String.valueOf(trip.getMinute()) ;
                String firebaseId = trip.getFirebaseKey();
                if(trip.getStatus() == 1){
                    String ref = user.getUid() + "/" + firebaseId;
                    updateOrDeleteRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child(firebaseId);
                    trip.setTripStatus(2);
                    HashMap<String,Object> myMap = new HashMap<>();
                    myMap.put("trip",trip);
                    myMap.put("notes",database.getNoteDao().getAll(tID));


                    updateOrDeleteRef.setValue(myMap);
                    editor.putBoolean("first_dialouge", false);
                    editor.commit();
                }



                Toast.makeText(AlarmReceivedActivity.this, "cancel pressed....", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        builder.setNeutralButton("start now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(pref.getBoolean("first_dialouge",true)){
                    mp3.stop();
                    mp3.onDestroy();
                }
                trip.setTripStatus(1);
                database.getTripDao().addTrip(trip);
                Log.i("trip",String.valueOf(trip.getTid()));
                Log.i("trip",trip.getName());
                Log.i("trip",trip.getEndLocationName());
                Log.i("trip",trip.getNotes());
                editor.putBoolean("first_dialouge", false);
                editor.commit();


                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getApplicationContext().getPackageName()));
                    startActivityForResult(intent, 2084);
                } else {
                    Bundle myBundle = new Bundle();
                    myBundle.putSerializable(TripDetails.CLICKED_TRIP, trip);
                    Intent intent1 = new Intent(getApplicationContext(), FloatingService.class);
                    intent1.putExtra(TripDetails.CLICKED_TRIP, myBundle);
                    getApplicationContext().startService(intent1);
                    Intent intent = new Intent(getApplicationContext(), FloatingService.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getApplicationContext().startService(intent);
                    finish();
                }




            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2084) {
            if (Settings.canDrawOverlays(this)) {
                Bundle myBundle = new Bundle();
                myBundle.putSerializable(TripDetails.CLICKED_TRIP, trip);
                Intent intent1 = new Intent(getApplicationContext(), FloatingService.class);
                intent1.putExtra(TripDetails.CLICKED_TRIP, myBundle);
                getApplicationContext().startService(intent1);
                finish();
            } else { //Permission is not available
                Toast.makeText(getApplicationContext(),
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}