package com.example.hazem.facebooklogin.views;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.contract.FloatingServiceInterface;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.model.FloatingServiceModel;
import com.example.hazem.facebooklogin.presenters.FloatingServicePresenter;

import java.util.ArrayList;


public class FloatingService extends IntentService implements FloatingServiceInterface.Viewer {


    private WindowManager mWindowManager;
    private View mFloatingView;
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private ArrayList<Note> notes;
    private FloatingNotesRecyclerViewAdapter floatingNotesRecyclerViewAdapter;
    private View notesLayout;
    private Intent myIntent;
    private Trip clickedTrip;
    private FloatingServicePresenter floatingServicePresenter;
    private View xView;
    private WindowManager xWindowManager;
    private ImageView xImageView;
    private ImageView floatingImage;


    public FloatingService() {
        super("myIntentService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("FLOATING_SERVICE", "oncreate called");

        myIntent = intent;
        Bundle clickedTripBundle = null;
        if (intent != null) {
            clickedTripBundle = myIntent.getBundleExtra(TripDetails.CLICKED_TRIP);
            if (clickedTripBundle != null) {
                clickedTrip = (Trip) clickedTripBundle.get(TripDetails.CLICKED_TRIP);
                floatingServicePresenter = new FloatingServicePresenter(this, new FloatingServiceModel(getApplicationContext()));
                String tID = String.valueOf(clickedTrip.getYear()) + String.valueOf(clickedTrip.getMonth()) + String.valueOf(clickedTrip.getDay()) +
                        String.valueOf(clickedTrip.getHour()) + String.valueOf(clickedTrip.getMinute());
                floatingServicePresenter.fetchNotes(tID);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("FLOATING_SERVICE", "on create called");
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_wedgit_layout, null);
        notesLayout = (LinearLayout) mFloatingView.findViewById(R.id.notes_layout);

        floatingImage = (ImageView)mFloatingView.findViewById(R.id.floating_icon);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        params.gravity = Gravity.TOP | Gravity.END;
//        params.x = (width*-1);
        params.y = 0;
        mWindowManager.addView(mFloatingView, params);


        xView = LayoutInflater.from(this).inflate(R.layout.x_service_layout, null);
        xImageView = (ImageView) xView.findViewById(R.id.x_image);

        xWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams xParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        xParams.gravity = Gravity.BOTTOM;
//        xParams.x = width / 2;
//        xParams.y = 0;
        xWindowManager.addView(xView, xParams);

        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {

            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    /////////////////////////////////////////////up//////////////////////////////////////
                    case MotionEvent.ACTION_UP:

                        if (xView != null) {
                            xImageView.setVisibility(View.GONE);
                        }
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (notesLayout.getVisibility() == View.GONE) {
                                notesLayout.setVisibility(View.VISIBLE);
                                Log.i("click", "show");
                            } else if (notesLayout.getVisibility() == View.VISIBLE) {
                                notesLayout.setVisibility(View.GONE);
                                Log.i("click", "hide");
                            }
                        }
                        return true;
                    ////////////////////////////////////////down///////////////////////////////////////////
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    ////////////////////////////////////////move///////////////////////////////////////////
                    case MotionEvent.ACTION_MOVE:

                        xImageView.setVisibility(View.VISIBLE);

                        params.x = initialX + (int) (initialTouchX - event.getRawX());
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        //event.getRawY() >= height - 100 &&
                        //xImageView.getLayoutParams().equals(mFloatingView.getLayoutParams())
//                        int[] xImageXy = new int[2];
//                        xImageView.getLocationOnScreen(xImageXy);
//
//                        int[] floatingImageXy = new int[2];
//                        floatingImage.getLocationOnScreen(floatingImageXy);

                        int left = xImageView.getLeft();
                        int right = xImageView.getRight();
                        int top = xImageView.getTop();
                        int bottom = xImageView.getBottom();
                        Rect rect = new Rect(left, top, right, bottom);

                        int left1 = mFloatingView.getLeft();
                        int right1 = mFloatingView.getRight();
                        int top1 = mFloatingView.getTop();
                        int bottom1 = mFloatingView.getBottom();
                        Rect rect1 = new Rect(left1, top1, right1, bottom1);

                        if (event.getRawY() >= height - 140 ) {
                            if (mFloatingView != null)
                                mWindowManager.removeView(mFloatingView);
                            xWindowManager.removeView(xView);
                            if (floatingServicePresenter != null)
                                floatingServicePresenter.updateNotes(notes, clickedTrip);
                            stopSelf();
                        }
                        return true;
                }
                return false;
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
        myRecyclerView = (RecyclerView) mFloatingView.findViewById(R.id.notes);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(myLayoutManager);
        floatingNotesRecyclerViewAdapter = new FloatingNotesRecyclerViewAdapter(notes, this);
        myRecyclerView.setAdapter(floatingNotesRecyclerViewAdapter);
    }

    public void updateNote(Note note, int i) {
        notes.remove(i);
        notes.add(i, note);
        floatingNotesRecyclerViewAdapter.notifyDataSetChanged();
    }


}
