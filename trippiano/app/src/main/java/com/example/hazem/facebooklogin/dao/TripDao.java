package com.example.hazem.facebooklogin.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.hazem.facebooklogin.entity.Trip;

import java.util.List;

/**
 * Created by Hazem on 3/4/2018.
 */

@Dao
public interface TripDao {
    @Query("SELECT * FROM trip WHERE userId = :uid AND tripStatus = 0 ")
    List<Trip> getAll(String uid);

    @Query("SELECT * FROM trip WHERE userId = :uid AND (tripStatus = 1 OR tripStatus = 2) ")
    List<Trip> getAllHistoryTrips(String uid);

    @Query("SELECT * FROM trip WHERE userId = :uid ")
    List<Trip> getAllTrips(String uid);

    @Query("SELECT * FROM trip WHERE tid = :id")
    Trip getTripByID(int id);

    @Query("SELECT * FROM trip WHERE tid IN (:tripIds)")
    List<Trip> loadAllByIds(int[] tripIds);

    @Query("SELECT * FROM trip WHERE name LIKE :first LIMIT 1")
    Trip findByName(String first);

    @Query("SELECT count(tid) FROM trip WHERE userId = :uid AND tripStatus = 0 ")
    int countUpcommingTrips(String uid);

    @Query("SELECT count(tid) FROM trip WHERE userId = :uid AND tripStatus = 1 ")
    int countDoneTrips(String uid);

    @Query("SELECT count(tid) FROM trip WHERE userId = :uid AND tripStatus = 2 ")
    int countcancelledTrips(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTrip(Trip trip);
    //@Insert
    //void insertAll(Trip... trips);

    @Delete
    void delete(Trip trip);

    @Query("delete from trip where tid = :id")
    void removeTripById(int id);

    @Query("delete from trip")
    void removeAllTrips();

    @Query("SELECT max(tid) FROM trip")
    int getMaxId();

}
