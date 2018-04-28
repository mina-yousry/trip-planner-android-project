package com.example.hazem.facebooklogin.views;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hazem.facebooklogin.R;

/**
 * Created by Mina Yousry on 3/14/2018.
 */

public class HistoryTripsViewHolder extends RecyclerView.ViewHolder {

    private View myView;
    private TextView tripName;
    private TextView tripDirectionsTextView;
    private TextView tripTime;
    private Button notes;
    private Button detailsOfTrip;
    private Button deleteTrip;
    private ImageView tripPathView;
    private LinearLayout cardButonsLinearLayout;

    public HistoryTripsViewHolder(View itemView) {
        super(itemView);
        myView = itemView;
    }

    public View getMyView() {
        return myView;
    }

    public TextView getTripName() {
        tripName = (TextView)myView.findViewById(R.id.trip_title);
        return tripName;
    }

    public TextView getTripDirectionsTextView() {
        tripDirectionsTextView = (TextView)myView.findViewById(R.id.trip_directions);
        return tripDirectionsTextView;
    }

    public TextView getTripTime() {
        tripTime = (TextView)myView.findViewById(R.id.trip_start_time);
        return tripTime;
    }

    public Button getNotes() {
        notes = (Button)myView.findViewById(R.id.btn_notes);
        return notes;
    }

    public Button getDetailsOfTrip() {
        detailsOfTrip = (Button)myView.findViewById(R.id.btn_details);
        return detailsOfTrip;
    }

    public Button getDeleteTrip() {
        deleteTrip = (Button)myView.findViewById(R.id.btn_delete);
        return deleteTrip;
    }

    public ImageView getTripPathView() {
        tripPathView = (ImageView)myView.findViewById(R.id.history_trip_mapView);
        return tripPathView;
    }

    public LinearLayout getCardButonsLinearLayout() {
        cardButonsLinearLayout = (LinearLayout)myView.findViewById(R.id.card_buttons_layout);
        return cardButonsLinearLayout;
    }
}
