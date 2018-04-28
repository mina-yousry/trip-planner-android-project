package com.example.hazem.facebooklogin.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.model.TripDetailsModel;
import com.example.hazem.facebooklogin.presenters.TripDetailsPresenter;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Mina Yousry on 3/18/2018.
 */

public class HistoryDetailsFragment extends Fragment implements com.example.hazem.facebooklogin.contract.TripDetails.View, NotesPopWindowUpdater{

    private ImageView tripPathImage;
    private TextView tripNameTxt;
    private TextView tripDateData;
    private TextView tripTimeData;
    private TextView startPoint;
    private TextView endPointTxt;
    private TextView tripStateData;
    private Button start;
    private Button cancel;
    private Button notes;
    private Button edit;
    private Button delete;
    private LinearLayout dateLayout;
    private LinearLayout timeLayout;
    private LinearLayout fromLayout;
    private LinearLayout toLayout;
    private LinearLayout stateLayout;
    private PopupWindow mPopupWindow;
    private RecyclerView notesRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private PopupNotesViewAdapter popupNotesViewAdapter;
    private Button dismissNotesWindow;
    private ArrayList<Note> notesList;
    private com.example.hazem.facebooklogin.contract.TripDetails.Presenter tripDetailsPresenter;
    private TripDetailsModel tripDetailsModel;
    private Trip myTrip;
    private CancelTripForTripDetails cancelTripForTripDetails;
    public final static String DETAILED_TRIP = "detailed_trip";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trip_details, container, false);

        tripPathImage = (ImageView) v.findViewById(R.id.route_image);
        tripNameTxt = (TextView) v.findViewById(R.id.trip_title);
        tripDateData = (TextView) v.findViewById(R.id.date_text);
        tripTimeData = (TextView) v.findViewById(R.id.time_text);
        startPoint = (TextView) v.findViewById(R.id.from_text);
        endPointTxt = (TextView) v.findViewById(R.id.to_text);
        tripStateData = (TextView) v.findViewById(R.id.state_text);
        start = (Button) v.findViewById(R.id.btn_start);
        cancel = (Button) v.findViewById(R.id.btn_cancel);
        notes = (Button) v.findViewById(R.id.btn_notes);
        edit = (Button) v.findViewById(R.id.btn_edit);
        delete = (Button) v.findViewById(R.id.btn_delete_details);
        dateLayout = (LinearLayout) v.findViewById(R.id.date_layout);
        timeLayout = (LinearLayout) v.findViewById(R.id.time_layout);
        fromLayout = (LinearLayout) v.findViewById(R.id.from_layout);
        toLayout = (LinearLayout) v.findViewById(R.id.to_layout);
        stateLayout = (LinearLayout) v.findViewById(R.id.state_layout);


        tripPathImage.setVisibility(View.GONE);
        tripNameTxt.setVisibility(View.GONE);
        tripDateData.setVisibility(View.GONE);
        tripTimeData.setVisibility(View.GONE);
        startPoint.setVisibility(View.GONE);
        endPointTxt.setVisibility(View.GONE);
        tripStateData.setVisibility(View.GONE);
        start.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        notes.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        dateLayout.setVisibility(View.GONE);
        timeLayout.setVisibility(View.GONE);
        fromLayout.setVisibility(View.GONE);
        toLayout.setVisibility(View.GONE);
        stateLayout.setVisibility(View.GONE);

        tripDetailsModel = new TripDetailsModel(getContext());
        tripDetailsPresenter = new TripDetailsPresenter(tripDetailsModel, this);
        tripDetailsModel.setMyPresenter(tripDetailsPresenter);
        return v;
    }

    @SuppressLint("SetTextI18n")
    public void showTrip(final Trip t, final CancelTripForTripDetails cancelTripForTripDetails) {

        this.cancelTripForTripDetails = cancelTripForTripDetails;
        myTrip = t;

        tripPathImage.setVisibility(View.VISIBLE);
        tripNameTxt.setVisibility(View.VISIBLE);
        tripDateData.setVisibility(View.VISIBLE);
        tripTimeData.setVisibility(View.VISIBLE);
        startPoint.setVisibility(View.VISIBLE);
        if(t.getTripStatus()==0) {
            start.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
        endPointTxt.setVisibility(View.VISIBLE);
        tripStateData.setVisibility(View.VISIBLE);

        notes.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        dateLayout.setVisibility(View.VISIBLE);
        timeLayout.setVisibility(View.VISIBLE);
        fromLayout.setVisibility(View.VISIBLE);
        toLayout.setVisibility(View.VISIBLE);
        stateLayout.setVisibility(View.VISIBLE);

        Glide.
                with(getContext()).
                load(t.getImageURL()).
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.ALL).
                skipMemoryCache(true).
                into(tripPathImage);
        tripNameTxt.setText(t.getName());
        tripDateData.setText(t.getDay() + " / " + t.getMonth() + " / " + t.getYear());
        tripTimeData.setText(t.getHour() + " : " + t.getMinute());
        startPoint.setText(t.getStartLocationName());
        endPointTxt.setText(t.getEndLocationName());

        String upcoming = getContext().getResources().getString(R.string.upcoming);
        String done = getContext().getResources().getString(R.string.done);
        String canceled = getContext().getResources().getString(R.string.canceled);

        if (t.getTripStatus() == 0) {
            tripStateData.setText(upcoming);
        } else if (t.getTripStatus() == 1) {
            tripStateData.setText(done);
        } else if (t.getTripStatus() == 2) {
            tripStateData.setText(canceled);
        }

        //set start button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(t);
            }
        });

        //set cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTripForTripDetails.cancelTrip(t);
            }
        });

        //set notes button
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tID = String.valueOf(t.getYear()) + String.valueOf(t.getMonth()) + String.valueOf(t.getDay()) +
                        String.valueOf(t.getHour()) + String.valueOf(t.getMinute()) ;
                tripDetailsPresenter.fetchNotes(tID);
            }
        });

        //set edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle myBundle = new Bundle();
                myBundle.putSerializable(TripDetails.CLICKED_TRIP, t);
                Intent myIntent = new Intent(getContext(), EditTrip.class);
                myIntent.putExtra(TripDetails.CLICKED_TRIP, myBundle);
                getContext().startActivity(myIntent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripDetailsPresenter.deleteTrip(t);
            }
        });
    }

    private void checkPermission(Trip t) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, 2084);
        } else {
            t.setTripStatus(1);
            tripDetailsPresenter.updateTrip(t);
            Bundle myBundle = new Bundle();
            myBundle.putSerializable(TripDetails.CLICKED_TRIP, t);
            Intent i = new Intent(getApplicationContext(), FloatingService.class);
            i.putExtra(TripDetails.CLICKED_TRIP, myBundle);
            getApplicationContext().startService(i);
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + t.getEndLat() + "," + t.getEndLong());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            getActivity().finish();
        }
    }


    @Override
    public void showNotesPopUp(final ArrayList<Note> notes) {
        notesList = notes;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View notesView = inflater.inflate(R.layout.notes_popup, null);
        ConstraintLayout myConstraintLayout = (ConstraintLayout) notesView.findViewById(R.id.notes_layout);
        mPopupWindow = new PopupWindow(
                notesView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(10.0f);
        }
        notesRecyclerView = (RecyclerView) notesView.findViewById(R.id.notes_popup);
        notesRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setLayoutManager(myLayoutManager);
        popupNotesViewAdapter = new PopupNotesViewAdapter(notes, this);
        notesRecyclerView.setAdapter(popupNotesViewAdapter);
        dismissNotesWindow = (Button) notesView.findViewById(R.id.close_notes);
        dismissNotesWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripDetailsPresenter.updateNotes(notes, myTrip);
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(myConstraintLayout, Gravity.CENTER, 0, 0);
        mPopupWindow.update(0, 0, mPopupWindow.getWidth(), mPopupWindow.getHeight());
    }


    @Override
    public void updateNote(Note note, int i) {
        notesList.remove(i);
        notesList.add(i, note);
        popupNotesViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (myTrip != null) {
                Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                i.putExtra(TripDetailsFragment.DETAILED_TRIP, myTrip.getTid());
                startActivity(i);
            }
        }
    }
}
