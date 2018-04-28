package com.example.hazem.facebooklogin.views;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hazem.facebooklogin.AlarmActivity;
import com.example.hazem.facebooklogin.DataBaseActivity;
import com.example.hazem.facebooklogin.MainActivity;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.profileActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    ImageView profilePic;
    TextView profileName;
    TextView profileEmail;
    TextView profilePhone,upCommingTrips,cancelledTrips,doneTrips;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MediaPlayer mp;
    private FirebaseAuth mAuth;
    Button test;
    Button alarm,signOut,login_button,main_Trips;
    private View profileView;
    private AppDatabase database;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileView = inflater.inflate(R.layout.activity_profile, container, false);

        profilePic = profileView.findViewById(R.id.imageView);
        profileName = profileView.findViewById(R.id.name);
        profileEmail = profileView.findViewById(R.id.email);
        profilePhone = profileView.findViewById(R.id.phone);
//        test = profileView.findViewById(R.id.TestDataBase);
//        alarm = profileView.findViewById(R.id.testAlarm);
        signOut = profileView.findViewById(R.id.signout);
        login_button =profileView.findViewById(R.id.login_button);
//        main_Trips = profileView.findViewById(R.id.mainTrips);
        upCommingTrips = profileView.findViewById(R.id.up_comming_trips_data);
        doneTrips = profileView.findViewById(R.id.done_trips_data);
        cancelledTrips = profileView.findViewById(R.id.canceled_trips_data);
        database = AppDatabase.getDatabase(getContext());
//        database.getTripDao().countUpcommingTrips()

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                editor.putBoolean("logged_in",false);
                editor.commit();
                Intent mainactivity = new Intent(getActivity(),MainActivity.class);
                startActivity(mainactivity);
            }
        });
        //hide facebook when signin using email


        /*
        Intent intent = getContext().getIntent();
        if(intent.getBooleanExtra("facebook",false)){
            signOut.setVisibility(View.GONE);
        }else{
            login_button.setVisibility(View.GONE);
        }
*/

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        String uId = pref.getString("user_id","0");
        pref.getString("user_name","user");
        pref.getString("user_email",".com");
        pref.getString("user_photo_uri","");

        profileName.setText(pref.getString("user_name","user"));
        profileEmail.setText(pref.getString("user_email",".com"));
        profilePhone.setText(pref.getString("user_id","0"));



        //count trips


        upCommingTrips.setText(String.valueOf(database.getTripDao().countUpcommingTrips(uId)));
        doneTrips.setText(String.valueOf(database.getTripDao().countDoneTrips(uId)));
        cancelledTrips.setText(String.valueOf(database.getTripDao().countcancelledTrips(uId)));
        //profileName.setText(user.getDisplayName());
        //profileEmail.setText(user.getEmail());
        //profilePhone.setText(user.getUid());
        //Glide.with(profileActivity.this).load(user.getPhotoUrl()).into(profilePic);
        Glide.with(getActivity()).load(pref.getString("user_photo_uri","")).into(profilePic);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent testDB = new Intent(getActivity(),DataBaseActivity.class);
//                startActivity(testDB);
//            }
//        });
//
//        alarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent alarm = new Intent(getActivity(),AlarmActivity.class);
//                startActivity(alarm);
//            }
//        });
//        main_Trips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent main_Trips = new Intent(getActivity(),HomeScreen.class);
//                startActivity(main_Trips);
//            }
//        });



        return profileView;
    }

    public void signOut(View v){
        //FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        editor.putBoolean("logged_in",false);
        editor.commit();
        Intent mainactivity = new Intent(getActivity(),MainActivity.class);
        startActivity(mainactivity);
    }

}
