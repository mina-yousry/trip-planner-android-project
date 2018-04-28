package com.example.hazem.facebooklogin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hazem.facebooklogin.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmActivity extends AppCompatActivity {


    TimePicker timePicker;
    Button alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        timePicker = findViewById(R.id.timePicker);
        alarm = findViewById(R.id.alarm);

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();

                Calendar timeOff1 = Calendar.getInstance();
                timeOff1.set(Calendar.YEAR, 2018);
                timeOff1.set(Calendar.MONTH, 3);
                timeOff1.set(Calendar.DAY_OF_MONTH,6 );
                timeOff1.set(Calendar.HOUR_OF_DAY, hour);
                timeOff1.set(Calendar.MINUTE, min);
                timeOff1.set(Calendar.SECOND, 00);

                final int _id = (int) System.currentTimeMillis();
                Intent intent = new Intent(AlarmActivity.this, MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, _id , intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10000 , pendingIntent);

                Toast.makeText(AlarmActivity.this, "alarm saved" , Toast.LENGTH_SHORT).show();
                //repeating the alarm
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,10000,pendingIntent);

                //canselling allarm
//                Intent intent1 = new Intent(AlarmActivity.this, MyBroadcastReceiver.class);
//                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(AlarmActivity.this, _id , intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
//                alarmManager1.cancel(pendingIntent1);


                }
        });
    }
}
