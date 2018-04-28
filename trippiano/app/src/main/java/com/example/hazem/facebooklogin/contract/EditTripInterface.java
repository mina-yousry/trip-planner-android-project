package com.example.hazem.facebooklogin.contract;

import com.example.hazem.facebooklogin.entity.Trip;

/**
 * Created by Hazem on 3/9/2018.
 */

public interface EditTripInterface {
    interface View{
        void updateTrip(Trip trip);

    }
    interface Model{

        void updateTrip(Trip trip);

    }
    interface Presenter{
        void updateTrip(Trip trip);

    }
}
