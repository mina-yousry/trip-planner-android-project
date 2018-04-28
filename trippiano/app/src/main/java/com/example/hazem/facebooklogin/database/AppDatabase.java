package com.example.hazem.facebooklogin.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.hazem.facebooklogin.dao.NoteDao;
import com.example.hazem.facebooklogin.dao.TripDao;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;


/**
 * Created by Hazem on 3/4/2018.
 */
@Database(entities = {Trip.class,Note.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract TripDao getTripDao();
    public abstract NoteDao getNoteDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "TripApp")
                            //Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // To simplify the exercise, allow queries on the main thread.
                            .allowMainThreadQueries()
                            // recreate the database if necessary
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
