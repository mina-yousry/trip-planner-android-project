package com.example.hazem.facebooklogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.utility.AlarmReceivedActivity;
import com.example.hazem.facebooklogin.utility.LoadMP3;

/**
 * Created by Hazem on 3/6/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    MediaPlayer mp;
    Context c;
    LoadMP3 mp3;
    boolean isBound = false;
    private AppDatabase database;
    @Override
    public void onReceive(Context context, Intent intent) {
        //mp=MediaPlayer.create(context, R.id.lou);
        //mp.start();

        int id = intent.getIntExtra("tripid",1);
        database = AppDatabase.getDatabase(context);
        Trip trip = database.getTripDao().getTripByID(id);

        c = context;
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        Log.i("listen","ok");


        //user dialouge
        Intent alarmRespond = new Intent(context, AlarmReceivedActivity.class);
        alarmRespond.putExtra("tripid",id);
        alarmRespond.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(alarmRespond);




    }
}
