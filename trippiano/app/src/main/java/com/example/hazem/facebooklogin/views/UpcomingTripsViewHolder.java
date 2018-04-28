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
 * Created by Mina on 3/5/2018.
 */

public class UpcomingTripsViewHolder extends RecyclerView.ViewHolder {

    private View tripCardView;
    private TextView tripName;
    private TextView tripDirectionsTextView;
    private TextView tripTime;
    private Button startTrip;
    private Button cancelTrip;
    private Button notes;
    private Button detailsOfTrip;
    private ImageView tripPathView;
    private ImageView upComingTripsMenuImg;
    private CardView upComingTripCard;
    private LinearLayout cardButonsLinearLayout;

    public UpcomingTripsViewHolder(View itemView) {
        super(itemView);
        tripCardView = itemView;
    }

    public View getTripCardView() {
        return tripCardView;
    }

    public TextView getTripName() {
        tripName = (TextView) tripCardView.findViewById(R.id.trip_title);
        return tripName;
    }

    public ImageView getTripPathView() {
        tripPathView = (ImageView) tripCardView.findViewById(R.id.trip_mapView);
        return tripPathView;
    }

    public CardView getUpComingTripCard() {
        upComingTripCard = (CardView) tripCardView.findViewById(R.id.upcoming_trip_card);
        return upComingTripCard;
    }

    public LinearLayout getCardButonsLinearLayout() {
        cardButonsLinearLayout = (LinearLayout) tripCardView.findViewById(R.id.card_buttons_layout);
        return cardButonsLinearLayout;
    }

    public TextView getTripTime() {
        tripTime = (TextView)tripCardView.findViewById(R.id.trip_start_time);
        return tripTime;
    }

    public ImageView getUpComingTripsMenuImg() {
        upComingTripsMenuImg = (ImageView)tripCardView.findViewById(R.id.upcoming_trip_menu);
        return upComingTripsMenuImg;
    }

    public Button getStartTrip() {
        startTrip = (Button)tripCardView.findViewById(R.id.btn_start);
        return startTrip;
    }

    public Button getCancelTrip() {
        cancelTrip = (Button)tripCardView.findViewById(R.id.btn_cancel);
        return cancelTrip;
    }

    public Button getNotes() {
        notes = (Button)tripCardView.findViewById(R.id.btn_notes);
        return notes;
    }

    public Button getDetailsOfTrip() {
        detailsOfTrip = (Button)tripCardView.findViewById(R.id.btn_details);
        return detailsOfTrip;
    }

    public TextView getTripDirectionsTextView() {
        tripDirectionsTextView = (TextView)tripCardView.findViewById(R.id.trip_directions);
        return tripDirectionsTextView;
    }
}
