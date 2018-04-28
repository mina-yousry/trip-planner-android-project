package com.example.hazem.facebooklogin.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Hazem on 3/4/2018.
 */


@Entity
public class Trip implements Serializable {
    @PrimaryKey
    private int tid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "start_long")
    private Double startLong;

    @ColumnInfo(name = "start_lat")
    private Double startLat;

    @ColumnInfo(name = "end_long")
    private Double endLong;

    @ColumnInfo(name = "end_lat")
    private Double endLat;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "imageURL")
    private String imageURL;

    @ColumnInfo(name = "placeImageURL")
    private String placeImageURL;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "hour")
    private int hour;

    @ColumnInfo(name = "minute")
    private int minute;

    @ColumnInfo(name = "rounded")
    private int rounded;

    @ColumnInfo(name = "startLocationName")
    private String startLocationName;

    @ColumnInfo(name = "endLocationName")
    private String endLocationName;

    //0 for not firebase updated
    //1 for firebase updated
    @ColumnInfo(name = "status")
    private int status;

    //0 for upcomming trip
    //1 for done trip
    //2 for cancelled trip
    @ColumnInfo(name = "tripStatus")
    private int tripStatus;

    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "firebaseKey")
    private String firebaseKey;


    @Ignore
    public Trip() {

    }

    public Trip(int tid, String name, Double startLong, Double startLat, Double endLong, Double endLat, String notes) {
        this.tid = tid;
        this.name = name;
        this.startLong = startLong;
        this.startLat = startLat;
        this.endLong = endLong;
        this.endLat = endLat;
        this.notes = notes;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStartLong() {
        return startLong;
    }

    public void setStartLong(Double startLong) {
        this.startLong = startLong;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getEndLong() {
        return endLong;
    }

    public void setEndLong(Double endLong) {
        this.endLong = endLong;
    }

    public Double getEndLat() {
        return endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getRounded() {
        return rounded;
    }

    public void setRounded(int rounded) {
        this.rounded = rounded;
    }

    public String getStartLocationName() {
        return startLocationName;
    }

    public void setStartLocationName(String startLocationName) {
        this.startLocationName = startLocationName;
    }

    public String getEndLocationName() {
        return endLocationName;
    }

    public void setEndLocationName(String endLocationName) {
        this.endLocationName = endLocationName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getPlaceImageURL() {
        return placeImageURL;
    }

    public void setPlaceImageURL(String placeImageURL) {
        this.placeImageURL = placeImageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tid=" + tid +
                ", name='" + name + '\'' +
                ", startLong=" + startLong +
                ", startLat=" + startLat +
                ", endLong=" + endLong +
                ", endLat=" + endLat +
                ", notes='" + notes + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", placeImageURL='" + placeImageURL + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", rounded=" + rounded +
                ", startLocationName='" + startLocationName + '\'' +
                ", endLocationName='" + endLocationName + '\'' +
                ", status=" + status +
                ", tripStatus=" + tripStatus +
                ", userId='" + userId + '\'' +
                '}';
    }
}
