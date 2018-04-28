package com.example.hazem.facebooklogin.utility;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.hazem.facebooklogin.R;

/**
 * Created by Hazem on 3/10/2018.
 */

public class LoadMP3 extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    MediaPlayer mp;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    /** method for clients */
    public void start(){
        mp = MediaPlayer.create(this, R.raw.loudalarm);
        mp.setLooping(false);
        mp.start();
    }

    public void stop()
    {
        mp.stop();
    }


    public class LocalBinder extends Binder {
        public LoadMP3 getService() {
            // Return this instance of LocalService so clients can call public methods
            return LoadMP3.this;
        }

    }
}
