package com.example.hazem.facebooklogin.utility;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mina on 3/7/2018.
 */

public class RequestQueueSingletone{

    private static RequestQueueSingletone requestQueueSingletone;
    private static RequestQueue requestQueue;

    private RequestQueueSingletone(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }
    public static RequestQueue newInstance(Context context){
        if (requestQueue == null){
            requestQueueSingletone = new RequestQueueSingletone(context);
        }
        return requestQueue;
    }

}
