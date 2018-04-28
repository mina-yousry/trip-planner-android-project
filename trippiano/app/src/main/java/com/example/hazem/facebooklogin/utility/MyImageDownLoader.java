package com.example.hazem.facebooklogin.utility;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hazem.facebooklogin.entity.Trip;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mina on 3/7/2018.
 */

public class MyImageDownLoader {

    private MyVolleyRequester myVolleyRequester;
    private StringRequest myJsonRequest;
    private StringRequest placeSerachStringRequest;
    private String myPoints;
    private RequestQueue myRequestQueue;

    public MyImageDownLoader(MyVolleyRequester requester){
        myVolleyRequester = requester;
    }


    public void downloadImages(final ArrayList<Trip> trips, Context c) {

        Log.i("home_problrm","download image called");
        final Context context = c;
        final int[] counter = {0};

        myRequestQueue = RequestQueueSingletone.newInstance(c);

        for (int i=0;i<trips.size();i++){

            final int temp = i;
            String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + trips.get(i).getStartLat() + ","
                    + trips.get(i).getStartLong() + "&destination="
                    + trips.get(i).getEndLat() + ","
                    + trips.get(i).getEndLong() + "&mode=driving";

             String placeImgRefApi = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+trips.get(i).getEndLat()+","+trips.get(i).getEndLong()+"&radius=500&key=AIzaSyBVpDfwFVEyV1MniiQMqFJgfm0TvhMWUaQ";
//            String placeImgRefApi = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+trips.get(i).getEndLat()+","+trips.get(i).getEndLong()+"&radius=500";


//            placeSerachStringRequest = new StringRequest(Request.Method.POST, placeImgRefApi, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
////                    Log.i("PLACE_RESPONSE",response);
//                    try {
//                        JSONObject jsonObj = new JSONObject(response);
//                        JSONArray jResults = jsonObj.getJSONArray("results");
//                        JSONObject firstResult = jResults.getJSONObject(0);
//                        JSONArray photosArray = firstResult.getJSONArray("photos");
//                        JSONObject photoObject = photosArray.getJSONObject(0);
//                        String photoReference = photoObject.getString("photo_reference");
//                        String placeImageUrl = getPlaceImageUrl(photoReference);
//                        trips.get(temp).setPlaceImageURL(placeImageUrl);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            });


//            myRequestQueue.add(placeSerachStringRequest);

            myJsonRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i("PLACE_RESPONSE",response);
                        JSONObject jsonObj = new JSONObject(response);
                        JSONArray jRoutes = jsonObj.getJSONArray("routes");
                        JSONObject jOverViewPolyLine = jRoutes.getJSONObject(0);
                        JSONObject overview_polyline = jOverViewPolyLine.getJSONObject("overview_polyline");
                        String points = overview_polyline.getString("points");
                        myPoints = points;
                        String imgUrl = getImageUrl(myPoints,trips.get(temp));
                        trips.get(temp).setImageURL(imgUrl);
                        String placeImageUrl = getPlaceImageUrl(myPoints,trips.get(temp));
                        trips.get(temp).setPlaceImageURL(placeImageUrl);
                        Log.i("check_volley",placeImageUrl);
                        if(counter[0]==trips.size()-1){
                            Log.i("check_volley","condition acheived");
                            myVolleyRequester.notifyModel(trips);
                        }
                        counter[0]++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            myRequestQueue.add(myJsonRequest);
        }
    }

    public String getImageUrl(String polyLinePoints,Trip trip){
        String imgUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=11&size=900x400&" +
                "format=JPEG&path=color:0xff0000ff|weight:5|enc:"+
                polyLinePoints+"&markers=size:mid|color:0x082f6d|" +
                ""+trip.getStartLat()+","+trip.getStartLong()+"|"+trip.getEndLat()+","+trip.getEndLong()+"&" +
                "key=AIzaSyBVpDfwFVEyV1MniiQMqFJgfm0TvhMWUaQ";
        return imgUrl;
    }

    public String getPlaceImageUrl(String polyLinePoints,Trip trip){
//        String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&" +
//                "photoreference="+imgRef+"&key=AIzaSyBVpDfwFVEyV1MniiQMqFJgfm0TvhMWUaQ";
        String imgUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=17&size=900x400&" +
                "format=JPEG&path=color:0xff0000ff|weight:5&markers=size:mid|color:0x082f6d|" +
                +trip.getEndLat()+","+trip.getEndLong()+"&" +
                "maptype=satellite&key=AIzaSyBVpDfwFVEyV1MniiQMqFJgfm0TvhMWUaQ";
        return imgUrl;
    }
}
