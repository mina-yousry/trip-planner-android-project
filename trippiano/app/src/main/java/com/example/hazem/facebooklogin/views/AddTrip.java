package com.example.hazem.facebooklogin.views;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hazem.facebooklogin.MyBroadcastReceiver;
import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class AddTrip extends Activity {
    private Trip trip, returnTrip;
    private Note note;
    private Double endLatitude;
    private Double endLongtude;
    private Double startLatitude;
    private Double startLongtude;
    private String thePlaceId;
    private TimePicker timePicker1;
    private ToggleButton toggle;
    private Button datePickerTxt;
    private Button adTtoList, addTrip;
    private Button timePickerTxt;
    private ListView listViewLayout;
    private ArrayList<String> notesList;
    private ArrayList<String> notesList2;
    private ArrayAdapter<String> adapter;
    private String startPlaceName;
    private String endPlaceName;
    private EditText tripName;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private int returnHour;
    private int returnMinute;
    private int returnYear;
    private int returnMonth;
    private int returnDay;
    private Calendar myCalendar;
    private TextView tripNameTxt2, startPointTxt2, textView6, dateTxt2, timeTxt2, addNoteTxt2;
    private Button timePicker2, datePicker2;
    private ListView listViewLayout2;
    private Button adTtoList2;
    private int flag;
    private int chexkFlag;
    private Calendar current;
    private Calendar selected;
    private RadioGroup radioRepeat;
    private RadioButton daily;
    private RadioButton monthly;
    private RadioButton weekly;
    private TextView selectedDate, selectedTime, selectedReturnDate, selectedReturnTime;

    HashMap<String, Object> myMap;

    String tID;


    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase1;
    private DatabaseReference tripRef;
    private DatabaseReference notesRef;
    private DatabaseReference tripAndNotes;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_trip);


        database = AppDatabase.getDatabase(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mFirebaseInstance = FirebaseDatabase.getInstance();

        tripName = findViewById(R.id.tripNameId);
        addTrip = findViewById(R.id.showTrip);
        datePickerTxt = (Button) findViewById(R.id.datePicker);
        timePickerTxt = (Button) findViewById(R.id.timePicker);
        listViewLayout = (ListView) findViewById(R.id.listView);
        selectedDate = (TextView) findViewById(R.id.selected_date);
        selectedTime = (TextView) findViewById(R.id.selected_time);
        notesList = new ArrayList<String>();

//        this code for invisable components

        datePicker2 = (Button) findViewById(R.id.datePicker2);
        timePicker2 = (Button) findViewById(R.id.timePicker2);
        dateTxt2 = (TextView) findViewById(R.id.dateTxt2);
        timeTxt2 = (TextView) findViewById(R.id.timeTxt2);
        selectedReturnDate = (TextView) findViewById(R.id.selected_return_date);
        selectedReturnTime = (TextView) findViewById(R.id.selected_return_time);

        adTtoList = (Button) findViewById(R.id.btnAddToList);


        myCalendar = Calendar.getInstance();

        timePickerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog time = new TimePickerDialog(AddTrip.this, timePickerListener, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true);
                time.show();
            }
        });


        datePickerTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog dp = new DatePickerDialog(AddTrip.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dp.show();

            }
        });

        timePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog time = new TimePickerDialog(AddTrip.this, timePickerListener2, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true);
                time.show();
            }
        });


        datePicker2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog dp = new DatePickerDialog(AddTrip.this, datePickerListener2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dp.show();

            }
        });

        toggle = (ToggleButton) findViewById(R.id.toggBtn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    flag = 1;
                    datePicker2.setVisibility(View.VISIBLE);
                    timePicker2.setVisibility(View.VISIBLE);
                    dateTxt2.setVisibility(View.VISIBLE);
                    timeTxt2.setVisibility(View.VISIBLE);
                    selectedReturnDate.setVisibility(View.VISIBLE);
                    selectedReturnTime.setVisibility(View.VISIBLE);


                } else {
                    flag = 0;
                    datePicker2.setVisibility(View.GONE);
                    timePicker2.setVisibility(View.GONE);
                    dateTxt2.setVisibility(View.GONE);
                    timeTxt2.setVisibility(View.GONE);
                    selectedReturnDate.setVisibility(View.GONE);
                    selectedReturnTime.setVisibility(View.GONE);

                }
            }
        });


        // add listener to make list view expand inside scroll view
        listViewLayout.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        listViewLayout.setAdapter(adapter);


        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((tripName.getText().toString()).matches("") || (selectedDate.getText().toString()).matches("") || (selectedTime.getText().toString()).matches("") || (startPlaceName == null) || (endPlaceName == null)) {
                    Toast.makeText(getApplicationContext(), "Enter Full Trip Data !",
                            Toast.LENGTH_LONG).show();
                } else {

                    int id = database.getTripDao().getMaxId();
                    tID = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) +
                            String.valueOf(hour) + String.valueOf(minute);
                    String tripName1 = tripName.getText().toString();
                    String notes = "";
                    for (int i = 0; i < notesList.size(); i++) {
                        notes += notesList.get(i) + ",,";
                        int nid = database.getNoteDao().getMaxNoteId();
                        note = new Note(nid + 1, notesList.get(i), tID, 0);
                        database.getNoteDao().addNote(note);
                    }
                    trip = new Trip(id + 1, tripName1, startLongtude, startLatitude, endLongtude, endLatitude, notes);

                    trip.setYear(year);
                    trip.setMonth(month);
                    trip.setDay(day);
                    trip.setHour(hour);
                    trip.setMinute(minute);
                    trip.setStartLocationName(startPlaceName);
                    trip.setEndLocationName(endPlaceName);
                    trip.setUserId(user.getUid());
//                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    trip.setStatus(1);
                    mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                    tripAndNotes = mFirebaseDatabase.child(user.getUid()).push();
                    trip.setFirebaseKey(tripAndNotes.getKey());
                    myMap = new HashMap<>();
                    myMap.put("trip", trip);

                    tID = String.valueOf(trip.getYear()) + String.valueOf(trip.getMonth()) + String.valueOf(trip.getDay()) +
                            String.valueOf(trip.getHour()) + String.valueOf(trip.getMinute());
                    myMap.put("notes", database.getNoteDao().getAll(tID));

                    tripAndNotes.updateChildren(myMap);
                    Log.i("firebasekey", tripAndNotes.getKey());


                    database.getTripDao().addTrip(trip);
                    saveAlarm(trip);


                    if (flag == 1) {
                        int id2 = database.getTripDao().getMaxId();


                        returnTrip = new Trip(id2 + 1, tripName1, endLongtude, endLatitude, startLongtude, startLatitude, "");


                        returnTrip.setYear(returnYear);
                        returnTrip.setMonth(returnMonth);
                        returnTrip.setDay(returnDay);
                        returnTrip.setHour(returnHour);
                        returnTrip.setMinute(returnMinute);

                        returnTrip.setStartLocationName(endPlaceName);
                        returnTrip.setEndLocationName(startPlaceName);
                        database.getTripDao().addTrip(returnTrip);
                        returnTrip.setUserId(user.getUid());
//                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                        returnTrip.setStatus(0);
                        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                        tripAndNotes = mFirebaseDatabase.child(user.getUid()).push();
                        returnTrip.setFirebaseKey(tripAndNotes.getKey());
                        HashMap<String, Object> myMap = new HashMap<>();
                        myMap.put("trip", returnTrip);

                        String tID = String.valueOf(returnTrip.getYear()) + String.valueOf(returnTrip.getMonth()) + String.valueOf(returnTrip.getDay()) +
                                String.valueOf(returnTrip.getHour()) + String.valueOf(returnTrip.getMinute());

                        myMap.put("notes", database.getNoteDao().getAll(tID));

                        tripAndNotes.updateChildren(myMap);
                        Log.i("firebasekey", tripAndNotes.getKey());

                        database.getTripDao().addTrip(returnTrip);

                        saveAlarm(returnTrip);


                    }


                    Intent intent = new Intent(AddTrip.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });


        // add listener to (add note) button to add note to list view
        adTtoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText note = (EditText) findViewById(R.id.editNote);


                if ((note.getText().length() > 0) && (!note.getText().equals(" "))) {

                    notesList.add(note.getText().toString());
                    note.setText("");
                    adapter.notifyDataSetChanged();
                }

            }
        });

        // add listener to list view to edit items
        listViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showInputBox(notesList.get(i), i);

            }
        });

        //Auto Complete fragments
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //  Log.i(TAG, "Place: " + place.getName());
                startLatitude = place.getLatLng().latitude;
                startLongtude = place.getLatLng().longitude;
                startPlaceName = place.getName().toString();
//                Toast.makeText(AddTrip.this,""+placeName, Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //  Log.i(TAG, "Place: " + place.getName());
                endLatitude = place.getLatLng().latitude;
                endLongtude = place.getLatLng().longitude;
                endPlaceName = place.getName().toString();
                thePlaceId = place.getId();
//                Toast.makeText(AddTrip.this,""+placeName, Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            selected = Calendar.getInstance();
            current = Calendar.getInstance();

            selected.set(Calendar.HOUR_OF_DAY, i);
            selected.set(Calendar.MINUTE, i1);

            hour = i;
            minute = i1;

            if ((Calendar.getInstance().compareTo(selected) <= 0)) {

//                selectedTime.setText(hour + ":" + minute);
                selectedTime.setText(selected.get(Calendar.HOUR_OF_DAY) + ":" + selected.get(Calendar.MINUTE));
            } else {
                selectedTime.setText("");
            }

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int h, int m) {

            Calendar datetime = Calendar.getInstance();
            Calendar c = Calendar.getInstance();

            datetime.set(Calendar.HOUR_OF_DAY, h);
            datetime.set(Calendar.MINUTE, m);

            returnHour = h;
            returnMinute = m;

            if ((c.compareTo(datetime) <= 0)) {

                selectedReturnTime.setText(datetime.get(Calendar.HOUR_OF_DAY) + ":" + datetime.get(Calendar.MINUTE));

            } else

            {

            }

        }
    };


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int dateYear, int dateMonth, int dateDay) {


            year = dateYear;
            month = dateMonth;
            day = dateDay;


            selectedDate.setText(new StringBuilder().append(day).append(" - ")
                    .append((month + 1)).append(" - ").append(year)
                    .append(" "));


            datePicker.setMinDate(System.currentTimeMillis() - 1000);

        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int dateYear, int dateMonth, int dateDay) {


            returnYear = dateYear;
            returnMonth = dateMonth;
            returnDay = dateDay;


            selectedReturnDate.setText(new StringBuilder().append(returnDay).append(" - ")
                    .append((returnMonth + 1)).append(" - ").append(returnYear)
                    .append(" "));

            datePicker.setMinDate(System.currentTimeMillis() - 1000);

        }
    };


    //show dialog box to edit items inside list view
    public void showInputBox(String oldItem, final int index) {
        final Dialog dialog = new Dialog(AddTrip.this);
        dialog.setTitle("Edit Note");
        dialog.setContentView(R.layout.update_item);
        final EditText editText = (EditText) dialog.findViewById(R.id.txtinput);
        editText.setText(oldItem);
        Button bt = (Button) dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesList.set(index, editText.getText().toString());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void saveAlarm(Trip tr) {
        int _id = tr.getTid();
        Intent intent = new Intent(AddTrip.this, MyBroadcastReceiver.class);
        intent.putExtra("tripid", _id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTrip.this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long currentTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, trip.getYear());
        c.set(Calendar.MONTH, trip.getMonth());
        c.set(Calendar.DAY_OF_MONTH, trip.getDay());
        c.set(Calendar.HOUR_OF_DAY, trip.getHour());
        c.set(Calendar.MINUTE, trip.getMinute());

        long interval = c.getTimeInMillis() - currentTime;

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);

        Toast.makeText(AddTrip.this, "trip saved", Toast.LENGTH_SHORT).show();

        Toast.makeText(AddTrip.this, "ctms: " + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();

        Toast.makeText(AddTrip.this, "interval: " + interval, Toast.LENGTH_SHORT).show();

    }
}
