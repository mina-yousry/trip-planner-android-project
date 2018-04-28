package com.example.hazem.facebooklogin.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Mina on 3/5/2018.
 */

public class UpComingTripsRecycleViewAdapter extends RecyclerView.Adapter<UpcomingTripsViewHolder> implements NotesPopWindowUpdater {

    private Context myContext;
    private ArrayList<Trip> upcomingTrips;
    private ArrayList<Note> notes;
    private UpComingTripsFragment upComingTripsFragment;
    private View tripCardView;
    private TextView tripTitleTextview;
    private TextView tripTimeTextview;
    private TextView fromToTextView;
    private ImageView cardImageView;
    private ImageView upComingTripsMenuImg;
    private Button tripDetails;
    private Button tripNotes;
    private Button startTrip;
    private Button dismissNotesWindow;
    private Button cancelTrip;
    private UpcomingTripsViewHolder upcomingTripsViewHolder;
    private LinearLayout cardButtonsLinearLayout;
    private PopupWindow mPopupWindow;
    private AppDatabase database;
    private RecyclerView notesRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private PopupNotesViewAdapter popupNotesViewAdapter;
    private String fromString;
    private String toString;
    private int myPosition;


    public UpComingTripsRecycleViewAdapter(ArrayList<Trip> upcomingTrips, UpComingTripsFragment upComingTripsFragment, Context context) {
        this.upcomingTrips = upcomingTrips;
        this.upComingTripsFragment = upComingTripsFragment;
        myContext = context;
    }

    @Override
    public UpcomingTripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        tripCardView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_card_view, parent, false);
        upcomingTripsViewHolder = new UpcomingTripsViewHolder(tripCardView);
        return upcomingTripsViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final UpcomingTripsViewHolder holder, final int position) {

        myPosition = position;

        //get buttons layout
        cardButtonsLinearLayout = holder.getCardButonsLinearLayout();

        //set trip map image
        cardImageView = holder.getTripPathView();
        if (upcomingTrips.get(position).getPlaceImageURL() == null) {
            Glide.
                    with(myContext).
                    load(R.mipmap.default_trip).
                    centerCrop().
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    skipMemoryCache(true).
                    into(cardImageView);
        } else {
            Glide.
                    with(myContext).
                    load(upcomingTrips.get(position).getPlaceImageURL()).
                    centerCrop().
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    skipMemoryCache(true).
                    into(cardImageView);

        }

        //set listener on map image view
        cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!upComingTripsFragment.isLandScape()) {
                    hideShowButtonsLayout(holder, position);
                }else {
                    upComingTripsFragment.showTripDetails(upcomingTrips.get(position));
                }
            }
        });

        //set trip name
        tripTitleTextview = holder.getTripName();
        tripTitleTextview.setText(upcomingTrips.get(position).getName());

        //set trip time
        tripTimeTextview = holder.getTripTime();
        tripTimeTextview.setText(upcomingTrips.get(position).getHour() + ":" + upcomingTrips.get(position).getMinute());

        //set from to text view
        fromString = myContext.getResources().getString(R.string.trip_from);
        toString = myContext.getResources().getString(R.string.trip_to);
        fromToTextView = holder.getTripDirectionsTextView();
        fromToTextView.setText(fromString + " " + upcomingTrips.get(position).getStartLocationName() + toString + " " + upcomingTrips.get(position).getEndLocationName());

        //set edit and delete menu
        upComingTripsMenuImg = holder.getUpComingTripsMenuImg();
        if(!upComingTripsFragment.isLandScape()){
            upComingTripsMenuImg.setVisibility(View.VISIBLE);
        }else {
            upComingTripsMenuImg.setVisibility(View.GONE);
        }
        upComingTripsMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDeleteMenu(v, position);
            }
        });

        //set start trip button
        startTrip = holder.getStartTrip();
        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrip(position);
            }
        });

        //set details button
        tripDetails = holder.getDetailsOfTrip();
        tripDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTripDetails(position);
            }
        });

        //set trip notes button
        tripNotes = (Button) holder.getNotes();
        tripNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotesPopupWindow(position);
            }
        });

        //set trip cancel button
        cancelTrip = (Button)holder.getCancelTrip();
        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTrip(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingTrips.size();
    }

    private void hideShowButtonsLayout(UpcomingTripsViewHolder holder, int pos) {
        if (cardButtonsLinearLayout.getVisibility() == LinearLayout.VISIBLE) {
            cardButtonsLinearLayout = holder.getCardButonsLinearLayout();
            cardButtonsLinearLayout.setVisibility(LinearLayout.GONE);
        } else if (cardButtonsLinearLayout.getVisibility() == LinearLayout.GONE) {
            cardButtonsLinearLayout = holder.getCardButonsLinearLayout();
            cardButtonsLinearLayout.setVisibility(LinearLayout.VISIBLE);
            //upComingTripsFragment.adjustCardVisibility(pos);
        }
    }

    private void showEditDeleteMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(myContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.upcoming_trip_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Edit":
                        Bundle myBundle = new Bundle();
                        myBundle.putSerializable(TripDetails.CLICKED_TRIP, upcomingTrips.get(position));
                        Intent myIntent = new Intent(myContext, EditTrip.class);
                        myIntent.putExtra(TripDetails.CLICKED_TRIP, myBundle);
                        myContext.startActivity(myIntent);
                        break;
                    case "Delete":
                        AlertDialog diaBox = AskOption(position);
                        diaBox.show();
                }
                return false;
            }
        });
        popup.show();
    }

    private void startTrip(int position) {
        upComingTripsFragment.checkPermission(upcomingTrips.get(position));
    }

    private void goToTripDetails(int position) {
        upComingTripsFragment.showTripDetails(upcomingTrips.get(position));
    }

    private void showNotesPopupWindow(int position) {
        Trip trip = upcomingTrips.get(position);
        String tID = String.valueOf(trip.getYear()) + String.valueOf(trip.getMonth()) + String.valueOf(trip.getDay()) +
                String.valueOf(trip.getHour()) + String.valueOf(trip.getMinute()) ;
        upComingTripsFragment.fetchNotes(tID);
    }

    public void setNotesToPopUPMenu(final ArrayList<Note> notes) {
        this.notes = notes;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(LAYOUT_INFLATER_SERVICE);
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
        myLayoutManager = new LinearLayoutManager(myContext);
        notesRecyclerView.setLayoutManager(myLayoutManager);
        popupNotesViewAdapter = new PopupNotesViewAdapter(notes, this);
        notesRecyclerView.setAdapter(popupNotesViewAdapter);
        dismissNotesWindow = (Button) notesView.findViewById(R.id.close_notes);
        dismissNotesWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upComingTripsFragment.updateNotes(notes,upcomingTrips.get(myPosition));
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
        notes.remove(i);
        notes.add(i, note);
        popupNotesViewAdapter.notifyDataSetChanged();
    }

    private void cancelTrip(int position){
        upComingTripsFragment.cancelTrip(upcomingTrips.get(position));
    }

    private AlertDialog AskOption(final int position)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(myContext)
                //set message, title, and icon
                .setTitle("Tripiano")
                .setMessage("Delete trip ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        upComingTripsFragment.detletTrip(upcomingTrips.get(position));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}
