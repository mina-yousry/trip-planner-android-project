package com.example.hazem.facebooklogin.contract;

import com.example.hazem.facebooklogin.entity.Trip;

/**
 * Created by Hazem on 3/9/2018.
 */

public interface AddTripInterface {
    interface View{
        void addTrip(Trip trip);


    }
    interface Model{

        void addTrip(Trip trip);




    }
    interface Presenter{
        void addTrip(Trip trip);

    }
}
