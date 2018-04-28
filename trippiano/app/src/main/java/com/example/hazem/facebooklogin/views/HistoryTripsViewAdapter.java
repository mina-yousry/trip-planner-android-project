package com.example.hazem.facebooklogin.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
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

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Mina Yousry on 3/14/2018.
 */

public class HistoryTripsViewAdapter extends RecyclerView.Adapter<HistoryTripsViewHolder> implements NotesPopWindowUpdater {

    private HistoryTripsFragment historyTripsFragment;
    private ArrayList<Trip> historyTrips;
    private ArrayList<Note> notes;
    private Context myContext;
    private View historyCardView;
    private HistoryTripsViewHolder historyTripsViewHolder;
    private LinearLayout cardButtonsLinearLayout;
    private ImageView cardImageView;
    private TextView tripTitleTextview;
    private TextView tripTimeTextview;
    private TextView fromToTextView;
    private String fromString;
    private String toString;
    private Button tripDetails;
    private Button tripNotes;
    private Button deleteTrip;
    private PopupWindow mPopupWindow;
    private RecyclerView notesRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private PopupNotesViewAdapter popupNotesViewAdapter;
    private Button dismissNotesWindow;
    private int myPosition;


    public HistoryTripsViewAdapter(HistoryTripsFragment historyTripsFragment, ArrayList<Trip> historyTrips, Context myContext) {
        this.historyTripsFragment = historyTripsFragment;
        this.historyTrips = historyTrips;
        this.myContext = myContext;
    }

    @Override
    public HistoryTripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        historyCardView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.history_trip_card_view, parent, false);
        historyTripsViewHolder = new HistoryTripsViewHolder(historyCardView);
        return historyTripsViewHolder;
    }

    @Override
    public void onBindViewHolder(final HistoryTripsViewHolder holder, final int position) {

        myPosition = position;
        //get buttons layout
        cardButtonsLinearLayout = holder.getCardButonsLinearLayout();

        //set trip map image
        cardImageView = holder.getTripPathView();
        Glide.
                with(myContext).
                load(historyTrips.get(position).getImageURL()).
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.ALL).
                skipMemoryCache(true).
                into(cardImageView);
        //set listener on map image view
        cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!historyTripsFragment.isLandScape()) {
                    hideShowButtonsLayout(holder, position);
                } else {
                    historyTripsFragment.showTripDetails(historyTrips.get(position));
                }

            }
        });

        //set trip name
        tripTitleTextview = holder.getTripName();
        tripTitleTextview.setText(historyTrips.get(position).getName());

        //set trip time
        tripTimeTextview = holder.getTripTime();
        tripTimeTextview.setText(historyTrips.get(position).getHour() + ":" + historyTrips.get(position).getMinute());

        //set from to text view
        fromString = myContext.getResources().getString(R.string.trip_from);
        toString = myContext.getResources().getString(R.string.trip_to);
        fromToTextView = holder.getTripDirectionsTextView();
        fromToTextView.setText(fromString + " " + historyTrips.get(position).getStartLocationName() + toString + " " + historyTrips.get(position).getEndLocationName());

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

        //set trip Delete button
        deleteTrip = (Button) holder.getDeleteTrip();
        deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption(position);
                diaBox.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyTrips.size();
    }

    private void hideShowButtonsLayout(HistoryTripsViewHolder holder, int pos) {
        if (cardButtonsLinearLayout.getVisibility() == LinearLayout.VISIBLE) {
            cardButtonsLinearLayout = holder.getCardButonsLinearLayout();
            cardButtonsLinearLayout.setVisibility(LinearLayout.GONE);
        } else if (cardButtonsLinearLayout.getVisibility() == LinearLayout.GONE) {
            cardButtonsLinearLayout = holder.getCardButonsLinearLayout();
            cardButtonsLinearLayout.setVisibility(LinearLayout.VISIBLE);
            //historyTripsFragment.adjustCardVisibility(pos);
        }
    }

    private void goToTripDetails(int position) {
        Bundle myBundle = new Bundle();
        myBundle.putSerializable(TripDetails.CLICKED_TRIP, historyTrips.get(position));
        Intent myIntent = new Intent(myContext, TripDetails.class);
        myIntent.putExtra(TripDetails.CLICKED_TRIP, myBundle);
        myContext.startActivity(myIntent);
    }

    private void showNotesPopupWindow(int position) {

        String tID = String.valueOf(historyTrips.get(position).getYear()) + String.valueOf(historyTrips.get(position).getMonth()) + String.valueOf(historyTrips.get(position).getDay()) +
                String.valueOf(historyTrips.get(position).getHour()) + String.valueOf(historyTrips.get(position).getMinute());
        historyTripsFragment.fetchNotes(tID);
    }

    public void setNotesToPopUPMenu(final ArrayList<Note> notes) {
        this.notes = notes;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View notesView = inflater.inflate(R.layout.notes_popup, null);
        final ConstraintLayout myConstraintLayout = (ConstraintLayout) notesView.findViewById(R.id.notes_layout);
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
                historyTripsFragment.updateNotes(notes, historyTrips.get(myPosition));
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

    private AlertDialog AskOption(final int position)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(myContext)
                //set message, title, and icon
                .setTitle("Tripiano")
                .setMessage("Delete trip ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        historyTripsFragment.detletTrip(historyTrips.get(position));
                        historyTrips.remove(position);
                        notifyDataSetChanged();
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
