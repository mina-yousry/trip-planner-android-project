package com.example.hazem.facebooklogin.utility;

import android.content.Context;


import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.model.HistoryModel;
import com.example.hazem.facebooklogin.model.HomeModel;

import java.util.ArrayList;

/**
 * Created by Mina on 3/7/2018.
 */

public class MyVolleyRequester {

    private MyImageDownLoader myImageDownLoader;
    private HomeModel upComingTripsModel;
    private HistoryModel historyModel;
    private Context myContext;

    public MyVolleyRequester(HomeModel upComingTripsModel, HistoryModel historyModel, Context c) {
        this.upComingTripsModel = upComingTripsModel;
        this.historyModel = historyModel;
        myContext = c;
    }

    public void getTripsImages(ArrayList<Trip> trips) {
        myImageDownLoader = new MyImageDownLoader(this);
        myImageDownLoader.downloadImages(trips, myContext);
    }

    public void notifyModel(ArrayList<Trip> tripsWithImages) {
        if (upComingTripsModel != null) {
            upComingTripsModel.notifyPresenter(tripsWithImages);
        }
        if (historyModel != null) {
            historyModel.notifyPresenter(tripsWithImages);
        }
    }
}
