package com.example.hazem.facebooklogin.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class EditTrip extends AppCompatActivity implements ViewTripInt {

    private Trip clickedTrip, editedTrip;

    String tID;
    private PlaceAutocompleteFragment place_autocomplete_fragment;
    private PlaceAutocompleteFragment place_autocomplete_fragment2;
    private ToggleButton toggBtn;
    private Button btnAddToList;
    private ListView listView;
    private Button edit_trip;
    ArrayList<String> notesList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private String startPlaceName;
    private String endPlaceName;
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
    private Button datePickerTxt;
    private Button timePickerTxt;
    private Calendar myCalendar;
    private Calendar current;
    private Calendar selected;
    private TextView selectedDate, selectedTime;
    private Button showTrip;
    private Note note;
    private EditText tripNameId;
    private AppDatabase database;
    Double endLatitude;
    Double endLongtude;
    Double startLatitude;
    Double startLongtude;
    String thePlaceId;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase1;
    private DatabaseReference tripRef;
    private DatabaseReference notesRef;
    private DatabaseReference tripAndNotes;
    private int timePickerMinute;
    private int timePickerHour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        Intent i = getIntent();
        Bundle clickedTripBundle = i.getBundleExtra(TripDetails.CLICKED_TRIP);

        database = AppDatabase.getDatabase(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mFirebaseInstance = FirebaseDatabase.getInstance();

        clickedTrip = (Trip) clickedTripBundle.get(TripDetails.CLICKED_TRIP);
//        tripNameId = findViewById(R.id.addTripTxt);
        datePickerTxt = (Button) findViewById(R.id.datePicker);
        timePickerTxt = (Button) findViewById(R.id.timePicker);
        listView = (ListView) findViewById(R.id.listView);
        notesList = new ArrayList<String>();
        btnAddToList = (Button) findViewById(R.id.btnAddToList);
        showTrip = (Button) findViewById(R.id.showTrip);


        selectedDate = (TextView) findViewById(R.id.selected_date);
        selectedTime = (TextView) findViewById(R.id.selected_time);
        tripNameId = (EditText) findViewById(R.id.tripNameId);


        timePickerHour=hour;
        timePickerMinute=minute;

        myCalendar = Calendar.getInstance();

        timePickerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog time = new TimePickerDialog(EditTrip.this, timePickerListener, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true);
                time.show();
            }
        });


        datePickerTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog dp = new DatePickerDialog(EditTrip.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dp.show();

            }
        });

        // add listener to make list view expand inside scroll view
        listView.setOnTouchListener(new ListView.OnTouchListener() {
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
        listView.setAdapter(adapter);


        // add listener to (add note) button to add note to list view
        btnAddToList.setOnClickListener(new View.OnClickListener() {
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showInputBox(notesList.get(i), i);

            }
        });

        //Auto Complete fragments
        place_autocomplete_fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        place_autocomplete_fragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        place_autocomplete_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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

        place_autocomplete_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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


        if (startLongtude == null) {
            startLongtude = clickedTrip.getStartLong();
        }
        if (startLatitude == null) {
            startLatitude = clickedTrip.getStartLat();
        }
        if (endLongtude == null) {
            endLongtude = clickedTrip.getEndLong();
        }
        if (endLatitude == null) {
            endLatitude = clickedTrip.getEndLat();
        }

        if (startPlaceName == null) {
            startPlaceName = clickedTrip.getStartLocationName();
        }
        if (endPlaceName == null) {
            endPlaceName = clickedTrip.getEndLocationName();
        }

    }


    @Override
    public void sendTripToFragment(Trip t) {
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

            if ((current.compareTo(selected) <= 0)) {

//                selectedTime.setText(hour + ":" + minute);
            } else

            {
                selectedTime.setText(current.get(Calendar.HOUR_OF_DAY) + ":" + current.get(Calendar.MINUTE));
            }

        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int dateYear, int dateMonth, int dateDay) {


            year = dateYear;
            month = dateMonth;
            day = dateDay;


            selectedDate.setText(new StringBuilder().append(year).append(" / ")
                    .append((month + 1)).append(" / ").append(day)
                    .append(" "));


            datePicker.setMinDate(System.currentTimeMillis() - 1000);

        }
    };


    public void showInputBox(String oldItem, final int index) {
        final Dialog dialog = new Dialog(EditTrip.this);
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

    @Override
    protected void onStart() {
        super.onStart();
        tripNameId.setText(clickedTrip.getName());
        selectedTime.setText(clickedTrip.getHour() + " : " + clickedTrip.getMinute());
        selectedDate.setText(clickedTrip.getYear() + " / " + clickedTrip.getMonth() + " / " + clickedTrip.getDay());
        place_autocomplete_fragment.setText(clickedTrip.getStartLocationName());
        place_autocomplete_fragment2.setText(clickedTrip.getEndLocationName());
        notesList.add(clickedTrip.getNotes());
        adapter.notifyDataSetChanged();


        showTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if ((tripNameId.getText().toString()).matches("") || (selectedDate.getText().toString()).matches("") || (selectedTime.getText().toString()).matches("") || (startPlaceName == null) || (endPlaceName == null)) {
                    Toast.makeText(getApplicationContext(), "Enter Full Trip Date !",
                            Toast.LENGTH_LONG).show();
                } else {

                    String notes = "";
                    for (int i = 0; i < notesList.size(); i++) {
                        notes += notesList.get(i) + ",,";
                        int nid = database.getNoteDao().getMaxNoteId();
                        note = new Note(nid + 1, notesList.get(i), tID, 0);
                        database.getNoteDao().addNote(note);
                    }

                    String tripName1 = tripNameId.getText().toString();


                    editedTrip = new Trip(clickedTrip.getTid(), tripName1, startLongtude, startLatitude, endLongtude, endLatitude, "");


                    String oldTime = (clickedTrip.getHour() + " : " + clickedTrip.getMinute()).toString();
                    String oldDate = (new StringBuilder().append(clickedTrip.getYear()).append(" / ").append((clickedTrip.getMonth())).append(" / ").append(clickedTrip.getDay()).append(" ")).toString();

                    String timeFromTimePicker = selectedTime.getText().toString();

                    if (oldTime.equals(timeFromTimePicker)) {
                        editedTrip.setHour(clickedTrip.getHour());
                        editedTrip.setMinute(clickedTrip.getMinute());
                    } else {
                        editedTrip.setHour(hour);
                        editedTrip.setMinute(minute);
                    }


                    String dateFromDatePicker = selectedDate.getText().toString();

                    Log.i("datee", "OLD date>> " + oldDate);
                    Log.i("datee", "clicked date>> " + dateFromDatePicker);


                    if (oldDate.equals(dateFromDatePicker)) {
                        editedTrip.setDay(clickedTrip.getDay());
                        Log.i("datee", "clicked dayy>> " + clickedTrip.getDay());
                        editedTrip.setYear(clickedTrip.getYear());
                        Log.i("datee", "clicked year>> " + clickedTrip.getYear());
                        editedTrip.setMonth(clickedTrip.getMonth());
                        Log.i("datee", "clicked month>> " + clickedTrip.getMonth());

                    } else {
                        editedTrip.setDay(day);
                        Log.i("datee", " day>> " + day);

                        editedTrip.setYear(year);
                        Log.i("datee", " year>> " + year);

                        editedTrip.setMonth(month);
                        Log.i("datee", " month>> " + month);

                    }


                    editedTrip.setStartLocationName(startPlaceName);
                    editedTrip.setEndLocationName(endPlaceName);


                    editedTrip.setUserId(user.getUid());
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    editedTrip.setStatus(1);
                    mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                    tripAndNotes = mFirebaseDatabase.child(user.getUid()).push();
                    editedTrip.setFirebaseKey(tripAndNotes.getKey());
                    HashMap<String, Object> myMap = new HashMap<>();
                    myMap.put("trip", editedTrip);

                    String tID = String.valueOf(editedTrip.getYear()) + String.valueOf(editedTrip.getMonth()) + String.valueOf(editedTrip.getDay()) +
                            String.valueOf(editedTrip.getHour()) + String.valueOf(editedTrip.getMinute());

                    myMap.put("notes", database.getNoteDao().getAll(tID));

                    tripAndNotes.updateChildren(myMap);
                    Log.i("firebasekey", tripAndNotes.getKey());

                    database.getTripDao().addTrip(editedTrip);


                    Intent intent = new Intent(EditTrip.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });


    }
}
