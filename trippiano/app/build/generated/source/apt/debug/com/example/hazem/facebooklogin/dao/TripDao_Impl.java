package com.example.hazem.facebooklogin.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import com.example.hazem.facebooklogin.entity.Trip;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

public class TripDao_Impl implements TripDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTrip;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTrip;

  private final SharedSQLiteStatement __preparedStmtOfRemoveTripById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAllTrips;

  public TripDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrip = new EntityInsertionAdapter<Trip>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Trip`(`tid`,`name`,`start_long`,`start_lat`,`end_long`,`end_lat`,`notes`,`imageURL`,`placeImageURL`,`year`,`month`,`day`,`hour`,`minute`,`rounded`,`startLocationName`,`endLocationName`,`status`,`tripStatus`,`userId`,`firebaseKey`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trip value) {
        stmt.bindLong(1, value.getTid());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getStartLong() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.getStartLong());
        }
        if (value.getStartLat() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindDouble(4, value.getStartLat());
        }
        if (value.getEndLong() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindDouble(5, value.getEndLong());
        }
        if (value.getEndLat() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindDouble(6, value.getEndLat());
        }
        if (value.getNotes() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getNotes());
        }
        if (value.getImageURL() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getImageURL());
        }
        if (value.getPlaceImageURL() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPlaceImageURL());
        }
        stmt.bindLong(10, value.getYear());
        stmt.bindLong(11, value.getMonth());
        stmt.bindLong(12, value.getDay());
        stmt.bindLong(13, value.getHour());
        stmt.bindLong(14, value.getMinute());
        stmt.bindLong(15, value.getRounded());
        if (value.getStartLocationName() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getStartLocationName());
        }
        if (value.getEndLocationName() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getEndLocationName());
        }
        stmt.bindLong(18, value.getStatus());
        stmt.bindLong(19, value.getTripStatus());
        if (value.getUserId() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getUserId());
        }
        if (value.getFirebaseKey() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getFirebaseKey());
        }
      }
    };
    this.__deletionAdapterOfTrip = new EntityDeletionOrUpdateAdapter<Trip>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Trip` WHERE `tid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trip value) {
        stmt.bindLong(1, value.getTid());
      }
    };
    this.__preparedStmtOfRemoveTripById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from trip where tid = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveAllTrips = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from trip";
        return _query;
      }
    };
  }

  @Override
  public void addTrip(Trip trip) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTrip.insert(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Trip trip) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfTrip.handle(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeTripById(int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveTripById.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveTripById.release(_stmt);
    }
  }

  @Override
  public void removeAllTrips() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAllTrips.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAllTrips.release(_stmt);
    }
  }

  @Override
  public List<Trip> getAll(String uid) {
    final String _sql = "SELECT * FROM trip WHERE userId = ? AND tripStatus = 0 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfStartLong = _cursor.getColumnIndexOrThrow("start_long");
      final int _cursorIndexOfStartLat = _cursor.getColumnIndexOrThrow("start_lat");
      final int _cursorIndexOfEndLong = _cursor.getColumnIndexOrThrow("end_long");
      final int _cursorIndexOfEndLat = _cursor.getColumnIndexOrThrow("end_lat");
      final int _cursorIndexOfNotes = _cursor.getColumnIndexOrThrow("notes");
      final int _cursorIndexOfImageURL = _cursor.getColumnIndexOrThrow("imageURL");
      final int _cursorIndexOfPlaceImageURL = _cursor.getColumnIndexOrThrow("placeImageURL");
      final int _cursorIndexOfYear = _cursor.getColumnIndexOrThrow("year");
      final int _cursorIndexOfMonth = _cursor.getColumnIndexOrThrow("month");
      final int _cursorIndexOfDay = _cursor.getColumnIndexOrThrow("day");
      final int _cursorIndexOfHour = _cursor.getColumnIndexOrThrow("hour");
      final int _cursorIndexOfMinute = _cursor.getColumnIndexOrThrow("minute");
      final int _cursorIndexOfRounded = _cursor.getColumnIndexOrThrow("rounded");
      final int _cursorIndexOfStartLocationName = _cursor.getColumnIndexOrThrow("startLocationName");
      final int _cursorIndexOfEndLocationName = _cursor.getColumnIndexOrThrow("endLocationName");
      final int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
      final int _cursorIndexOfTripStatus = _cursor.getColumnIndexOrThrow("tripStatus");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("userId");
      final int _cursorIndexOfFirebaseKey = _cursor.getColumnIndexOrThrow("firebaseKey");
      final List<Trip> _result = new ArrayList<Trip>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trip _item;
        final int _tmpTid;
        _tmpTid = _cursor.getInt(_cursorIndexOfTid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Double _tmpStartLong;
        if (_cursor.isNull(_cursorIndexOfStartLong)) {
          _tmpStartLong = null;
        } else {
          _tmpStartLong = _cursor.getDouble(_cursorIndexOfStartLong);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpEndLong;
        if (_cursor.isNull(_cursorIndexOfEndLong)) {
          _tmpEndLong = null;
        } else {
          _tmpEndLong = _cursor.getDouble(_cursorIndexOfEndLong);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _item = new Trip(_tmpTid,_tmpName,_tmpStartLong,_tmpStartLat,_tmpEndLong,_tmpEndLat,_tmpNotes);
        final String _tmpImageURL;
        _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
        _item.setImageURL(_tmpImageURL);
        final String _tmpPlaceImageURL;
        _tmpPlaceImageURL = _cursor.getString(_cursorIndexOfPlaceImageURL);
        _item.setPlaceImageURL(_tmpPlaceImageURL);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        _item.setYear(_tmpYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        _item.setMonth(_tmpMonth);
        final int _tmpDay;
        _tmpDay = _cursor.getInt(_cursorIndexOfDay);
        _item.setDay(_tmpDay);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        _item.setHour(_tmpHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        _item.setMinute(_tmpMinute);
        final int _tmpRounded;
        _tmpRounded = _cursor.getInt(_cursorIndexOfRounded);
        _item.setRounded(_tmpRounded);
        final String _tmpStartLocationName;
        _tmpStartLocationName = _cursor.getString(_cursorIndexOfStartLocationName);
        _item.setStartLocationName(_tmpStartLocationName);
        final String _tmpEndLocationName;
        _tmpEndLocationName = _cursor.getString(_cursorIndexOfEndLocationName);
        _item.setEndLocationName(_tmpEndLocationName);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        _item.setStatus(_tmpStatus);
        final int _tmpTripStatus;
        _tmpTripStatus = _cursor.getInt(_cursorIndexOfTripStatus);
        _item.setTripStatus(_tmpTripStatus);
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final String _tmpFirebaseKey;
        _tmpFirebaseKey = _cursor.getString(_cursorIndexOfFirebaseKey);
        _item.setFirebaseKey(_tmpFirebaseKey);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trip> getAllHistoryTrips(String uid) {
    final String _sql = "SELECT * FROM trip WHERE userId = ? AND (tripStatus = 1 OR tripStatus = 2) ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfStartLong = _cursor.getColumnIndexOrThrow("start_long");
      final int _cursorIndexOfStartLat = _cursor.getColumnIndexOrThrow("start_lat");
      final int _cursorIndexOfEndLong = _cursor.getColumnIndexOrThrow("end_long");
      final int _cursorIndexOfEndLat = _cursor.getColumnIndexOrThrow("end_lat");
      final int _cursorIndexOfNotes = _cursor.getColumnIndexOrThrow("notes");
      final int _cursorIndexOfImageURL = _cursor.getColumnIndexOrThrow("imageURL");
      final int _cursorIndexOfPlaceImageURL = _cursor.getColumnIndexOrThrow("placeImageURL");
      final int _cursorIndexOfYear = _cursor.getColumnIndexOrThrow("year");
      final int _cursorIndexOfMonth = _cursor.getColumnIndexOrThrow("month");
      final int _cursorIndexOfDay = _cursor.getColumnIndexOrThrow("day");
      final int _cursorIndexOfHour = _cursor.getColumnIndexOrThrow("hour");
      final int _cursorIndexOfMinute = _cursor.getColumnIndexOrThrow("minute");
      final int _cursorIndexOfRounded = _cursor.getColumnIndexOrThrow("rounded");
      final int _cursorIndexOfStartLocationName = _cursor.getColumnIndexOrThrow("startLocationName");
      final int _cursorIndexOfEndLocationName = _cursor.getColumnIndexOrThrow("endLocationName");
      final int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
      final int _cursorIndexOfTripStatus = _cursor.getColumnIndexOrThrow("tripStatus");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("userId");
      final int _cursorIndexOfFirebaseKey = _cursor.getColumnIndexOrThrow("firebaseKey");
      final List<Trip> _result = new ArrayList<Trip>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trip _item;
        final int _tmpTid;
        _tmpTid = _cursor.getInt(_cursorIndexOfTid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Double _tmpStartLong;
        if (_cursor.isNull(_cursorIndexOfStartLong)) {
          _tmpStartLong = null;
        } else {
          _tmpStartLong = _cursor.getDouble(_cursorIndexOfStartLong);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpEndLong;
        if (_cursor.isNull(_cursorIndexOfEndLong)) {
          _tmpEndLong = null;
        } else {
          _tmpEndLong = _cursor.getDouble(_cursorIndexOfEndLong);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _item = new Trip(_tmpTid,_tmpName,_tmpStartLong,_tmpStartLat,_tmpEndLong,_tmpEndLat,_tmpNotes);
        final String _tmpImageURL;
        _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
        _item.setImageURL(_tmpImageURL);
        final String _tmpPlaceImageURL;
        _tmpPlaceImageURL = _cursor.getString(_cursorIndexOfPlaceImageURL);
        _item.setPlaceImageURL(_tmpPlaceImageURL);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        _item.setYear(_tmpYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        _item.setMonth(_tmpMonth);
        final int _tmpDay;
        _tmpDay = _cursor.getInt(_cursorIndexOfDay);
        _item.setDay(_tmpDay);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        _item.setHour(_tmpHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        _item.setMinute(_tmpMinute);
        final int _tmpRounded;
        _tmpRounded = _cursor.getInt(_cursorIndexOfRounded);
        _item.setRounded(_tmpRounded);
        final String _tmpStartLocationName;
        _tmpStartLocationName = _cursor.getString(_cursorIndexOfStartLocationName);
        _item.setStartLocationName(_tmpStartLocationName);
        final String _tmpEndLocationName;
        _tmpEndLocationName = _cursor.getString(_cursorIndexOfEndLocationName);
        _item.setEndLocationName(_tmpEndLocationName);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        _item.setStatus(_tmpStatus);
        final int _tmpTripStatus;
        _tmpTripStatus = _cursor.getInt(_cursorIndexOfTripStatus);
        _item.setTripStatus(_tmpTripStatus);
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final String _tmpFirebaseKey;
        _tmpFirebaseKey = _cursor.getString(_cursorIndexOfFirebaseKey);
        _item.setFirebaseKey(_tmpFirebaseKey);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trip> getAllTrips(String uid) {
    final String _sql = "SELECT * FROM trip WHERE userId = ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfStartLong = _cursor.getColumnIndexOrThrow("start_long");
      final int _cursorIndexOfStartLat = _cursor.getColumnIndexOrThrow("start_lat");
      final int _cursorIndexOfEndLong = _cursor.getColumnIndexOrThrow("end_long");
      final int _cursorIndexOfEndLat = _cursor.getColumnIndexOrThrow("end_lat");
      final int _cursorIndexOfNotes = _cursor.getColumnIndexOrThrow("notes");
      final int _cursorIndexOfImageURL = _cursor.getColumnIndexOrThrow("imageURL");
      final int _cursorIndexOfPlaceImageURL = _cursor.getColumnIndexOrThrow("placeImageURL");
      final int _cursorIndexOfYear = _cursor.getColumnIndexOrThrow("year");
      final int _cursorIndexOfMonth = _cursor.getColumnIndexOrThrow("month");
      final int _cursorIndexOfDay = _cursor.getColumnIndexOrThrow("day");
      final int _cursorIndexOfHour = _cursor.getColumnIndexOrThrow("hour");
      final int _cursorIndexOfMinute = _cursor.getColumnIndexOrThrow("minute");
      final int _cursorIndexOfRounded = _cursor.getColumnIndexOrThrow("rounded");
      final int _cursorIndexOfStartLocationName = _cursor.getColumnIndexOrThrow("startLocationName");
      final int _cursorIndexOfEndLocationName = _cursor.getColumnIndexOrThrow("endLocationName");
      final int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
      final int _cursorIndexOfTripStatus = _cursor.getColumnIndexOrThrow("tripStatus");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("userId");
      final int _cursorIndexOfFirebaseKey = _cursor.getColumnIndexOrThrow("firebaseKey");
      final List<Trip> _result = new ArrayList<Trip>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trip _item;
        final int _tmpTid;
        _tmpTid = _cursor.getInt(_cursorIndexOfTid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Double _tmpStartLong;
        if (_cursor.isNull(_cursorIndexOfStartLong)) {
          _tmpStartLong = null;
        } else {
          _tmpStartLong = _cursor.getDouble(_cursorIndexOfStartLong);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpEndLong;
        if (_cursor.isNull(_cursorIndexOfEndLong)) {
          _tmpEndLong = null;
        } else {
          _tmpEndLong = _cursor.getDouble(_cursorIndexOfEndLong);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _item = new Trip(_tmpTid,_tmpName,_tmpStartLong,_tmpStartLat,_tmpEndLong,_tmpEndLat,_tmpNotes);
        final String _tmpImageURL;
        _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
        _item.setImageURL(_tmpImageURL);
        final String _tmpPlaceImageURL;
        _tmpPlaceImageURL = _cursor.getString(_cursorIndexOfPlaceImageURL);
        _item.setPlaceImageURL(_tmpPlaceImageURL);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        _item.setYear(_tmpYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        _item.setMonth(_tmpMonth);
        final int _tmpDay;
        _tmpDay = _cursor.getInt(_cursorIndexOfDay);
        _item.setDay(_tmpDay);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        _item.setHour(_tmpHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        _item.setMinute(_tmpMinute);
        final int _tmpRounded;
        _tmpRounded = _cursor.getInt(_cursorIndexOfRounded);
        _item.setRounded(_tmpRounded);
        final String _tmpStartLocationName;
        _tmpStartLocationName = _cursor.getString(_cursorIndexOfStartLocationName);
        _item.setStartLocationName(_tmpStartLocationName);
        final String _tmpEndLocationName;
        _tmpEndLocationName = _cursor.getString(_cursorIndexOfEndLocationName);
        _item.setEndLocationName(_tmpEndLocationName);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        _item.setStatus(_tmpStatus);
        final int _tmpTripStatus;
        _tmpTripStatus = _cursor.getInt(_cursorIndexOfTripStatus);
        _item.setTripStatus(_tmpTripStatus);
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final String _tmpFirebaseKey;
        _tmpFirebaseKey = _cursor.getString(_cursorIndexOfFirebaseKey);
        _item.setFirebaseKey(_tmpFirebaseKey);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Trip getTripByID(int id) {
    final String _sql = "SELECT * FROM trip WHERE tid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfStartLong = _cursor.getColumnIndexOrThrow("start_long");
      final int _cursorIndexOfStartLat = _cursor.getColumnIndexOrThrow("start_lat");
      final int _cursorIndexOfEndLong = _cursor.getColumnIndexOrThrow("end_long");
      final int _cursorIndexOfEndLat = _cursor.getColumnIndexOrThrow("end_lat");
      final int _cursorIndexOfNotes = _cursor.getColumnIndexOrThrow("notes");
      final int _cursorIndexOfImageURL = _cursor.getColumnIndexOrThrow("imageURL");
      final int _cursorIndexOfPlaceImageURL = _cursor.getColumnIndexOrThrow("placeImageURL");
      final int _cursorIndexOfYear = _cursor.getColumnIndexOrThrow("year");
      final int _cursorIndexOfMonth = _cursor.getColumnIndexOrThrow("month");
      final int _cursorIndexOfDay = _cursor.getColumnIndexOrThrow("day");
      final int _cursorIndexOfHour = _cursor.getColumnIndexOrThrow("hour");
      final int _cursorIndexOfMinute = _cursor.getColumnIndexOrThrow("minute");
      final int _cursorIndexOfRounded = _cursor.getColumnIndexOrThrow("rounded");
      final int _cursorIndexOfStartLocationName = _cursor.getColumnIndexOrThrow("startLocationName");
      final int _cursorIndexOfEndLocationName = _cursor.getColumnIndexOrThrow("endLocationName");
      final int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
      final int _cursorIndexOfTripStatus = _cursor.getColumnIndexOrThrow("tripStatus");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("userId");
      final int _cursorIndexOfFirebaseKey = _cursor.getColumnIndexOrThrow("firebaseKey");
      final Trip _result;
      if(_cursor.moveToFirst()) {
        final int _tmpTid;
        _tmpTid = _cursor.getInt(_cursorIndexOfTid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Double _tmpStartLong;
        if (_cursor.isNull(_cursorIndexOfStartLong)) {
          _tmpStartLong = null;
        } else {
          _tmpStartLong = _cursor.getDouble(_cursorIndexOfStartLong);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpEndLong;
        if (_cursor.isNull(_cursorIndexOfEndLong)) {
          _tmpEndLong = null;
        } else {
          _tmpEndLong = _cursor.getDouble(_cursorIndexOfEndLong);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _result = new Trip(_tmpTid,_tmpName,_tmpStartLong,_tmpStartLat,_tmpEndLong,_tmpEndLat,_tmpNotes);
        final String _tmpImageURL;
        _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
        _result.setImageURL(_tmpImageURL);
        final String _tmpPlaceImageURL;
        _tmpPlaceImageURL = _cursor.getString(_cursorIndexOfPlaceImageURL);
        _result.setPlaceImageURL(_tmpPlaceImageURL);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        _result.setYear(_tmpYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        _result.setMonth(_tmpMonth);
        final int _tmpDay;
        _tmpDay = _cursor.getInt(_cursorIndexOfDay);
        _result.setDay(_tmpDay);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        _result.setHour(_tmpHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        _result.setMinute(_tmpMinute);
        final int _tmpRounded;
        _tmpRounded = _cursor.getInt(_cursorIndexOfRounded);
        _result.setRounded(_tmpRounded);
        final String _tmpStartLocationName;
        _tmpStartLocationName = _cursor.getString(_cursorIndexOfStartLocationName);
        _result.setStartLocationName(_tmpStartLocationName);
        final String _tmpEndLocationName;
        _tmpEndLocationName = _cursor.getString(_cursorIndexOfEndLocationName);
        _result.setEndLocationName(_tmpEndLocationName);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        _result.setStatus(_tmpStatus);
        final int _tmpTripStatus;
        _tmpTripStatus = _cursor.getInt(_cursorIndexOfTripStatus);
        _result.setTripStatus(_tmpTripStatus);
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final String _tmpFirebaseKey;
        _tmpFirebaseKey = _cursor.getString(_cursorIndexOfFirebaseKey);
        _result.setFirebaseKey(_tmpFirebaseKey);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trip> loadAllByIds(int[] tripIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM trip WHERE tid IN (");
    final int _inputSize = tripIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : tripIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfStartLong = _cursor.getColumnIndexOrThrow("start_long");
      final int _cursorIndexOfStartLat = _cursor.getColumnIndexOrThrow("start_lat");
      final int _cursorIndexOfEndLong = _cursor.getColumnIndexOrThrow("end_long");
      final int _cursorIndexOfEndLat = _cursor.getColumnIndexOrThrow("end_lat");
      final int _cursorIndexOfNotes = _cursor.getColumnIndexOrThrow("notes");
      final int _cursorIndexOfImageURL = _cursor.getColumnIndexOrThrow("imageURL");
      final int _cursorIndexOfPlaceImageURL = _cursor.getColumnIndexOrThrow("placeImageURL");
      final int _cursorIndexOfYear = _cursor.getColumnIndexOrThrow("year");
      final int _cursorIndexOfMonth = _cursor.getColumnIndexOrThrow("month");
      final int _cursorIndexOfDay = _cursor.getColumnIndexOrThrow("day");
      final int _cursorIndexOfHour = _cursor.getColumnIndexOrThrow("hour");
      final int _cursorIndexOfMinute = _cursor.getColumnIndexOrThrow("minute");
      final int _cursorIndexOfRounded = _cursor.getColumnIndexOrThrow("rounded");
      final int _cursorIndexOfStartLocationName = _cursor.getColumnIndexOrThrow("startLocationName");
      final int _cursorIndexOfEndLocationName = _cursor.getColumnIndexOrThrow("endLocationName");
      final int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
      final int _cursorIndexOfTripStatus = _cursor.getColumnIndexOrThrow("tripStatus");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("userId");
      final int _cursorIndexOfFirebaseKey = _cursor.getColumnIndexOrThrow("firebaseKey");
      final List<Trip> _result = new ArrayList<Trip>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trip _item_1;
        final int _tmpTid;
        _tmpTid = _cursor.getInt(_cursorIndexOfTid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Double _tmpStartLong;
        if (_cursor.isNull(_cursorIndexOfStartLong)) {
          _tmpStartLong = null;
        } else {
          _tmpStartLong = _cursor.getDouble(_cursorIndexOfStartLong);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpEndLong;
        if (_cursor.isNull(_cursorIndexOfEndLong)) {
          _tmpEndLong = null;
        } else {
          _tmpEndLong = _cursor.getDouble(_cursorIndexOfEndLong);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _item_1 = new Trip(_tmpTid,_tmpName,_tmpStartLong,_tmpStartLat,_tmpEndLong,_tmpEndLat,_tmpNotes);
        final String _tmpImageURL;
        _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
        _item_1.setImageURL(_tmpImageURL);
        final String _tmpPlaceImageURL;
        _tmpPlaceImageURL = _cursor.getString(_cursorIndexOfPlaceImageURL);
        _item_1.setPlaceImageURL(_tmpPlaceImageURL);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        _item_1.setYear(_tmpYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        _item_1.setMonth(_tmpMonth);
        final int _tmpDay;
        _tmpDay = _cursor.getInt(_cursorIndexOfDay);
        _item_1.setDay(_tmpDay);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        _item_1.setHour(_tmpHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        _item_1.setMinute(_tmpMinute);
        final int _tmpRounded;
        _tmpRounded = _cursor.getInt(_cursorIndexOfRounded);
        _item_1.setRounded(_tmpRounded);
        final String _tmpStartLocationName;
        _tmpStartLocationName = _cursor.getString(_cursorIndexOfStartLocationName);
        _item_1.setStartLocationName(_tmpStartLocationName);
        final String _tmpEndLocationName;
        _tmpEndLocationName = _cursor.getString(_cursorIndexOfEndLocationName);
        _item_1.setEndLocationName(_tmpEndLocationName);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        _item_1.setStatus(_tmpStatus);
        final int _tmpTripStatus;
        _tmpTripStatus = _cursor.getInt(_cursorIndexOfTripStatus);
        _item_1.setTripStatus(_tmpTripStatus);
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        _item_1.setUserId(_tmpUserId);
        final String _tmpFirebaseKey;
        _tmpFirebaseKey = _cursor.getString(_cursorIndexOfFirebaseKey);
        _item_1.setFirebaseKey(_tmpFirebaseKey);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Trip findByName(String first) {
    final String _sql = "SELECT * FROM trip WHERE name LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (first == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, first);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfStartLong = _cursor.getColumnIndexOrThrow("start_long");
      final int _cursorIndexOfStartLat = _cursor.getColumnIndexOrThrow("start_lat");
      final int _cursorIndexOfEndLong = _cursor.getColumnIndexOrThrow("end_long");
      final int _cursorIndexOfEndLat = _cursor.getColumnIndexOrThrow("end_lat");
      final int _cursorIndexOfNotes = _cursor.getColumnIndexOrThrow("notes");
      final int _cursorIndexOfImageURL = _cursor.getColumnIndexOrThrow("imageURL");
      final int _cursorIndexOfPlaceImageURL = _cursor.getColumnIndexOrThrow("placeImageURL");
      final int _cursorIndexOfYear = _cursor.getColumnIndexOrThrow("year");
      final int _cursorIndexOfMonth = _cursor.getColumnIndexOrThrow("month");
      final int _cursorIndexOfDay = _cursor.getColumnIndexOrThrow("day");
      final int _cursorIndexOfHour = _cursor.getColumnIndexOrThrow("hour");
      final int _cursorIndexOfMinute = _cursor.getColumnIndexOrThrow("minute");
      final int _cursorIndexOfRounded = _cursor.getColumnIndexOrThrow("rounded");
      final int _cursorIndexOfStartLocationName = _cursor.getColumnIndexOrThrow("startLocationName");
      final int _cursorIndexOfEndLocationName = _cursor.getColumnIndexOrThrow("endLocationName");
      final int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
      final int _cursorIndexOfTripStatus = _cursor.getColumnIndexOrThrow("tripStatus");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("userId");
      final int _cursorIndexOfFirebaseKey = _cursor.getColumnIndexOrThrow("firebaseKey");
      final Trip _result;
      if(_cursor.moveToFirst()) {
        final int _tmpTid;
        _tmpTid = _cursor.getInt(_cursorIndexOfTid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Double _tmpStartLong;
        if (_cursor.isNull(_cursorIndexOfStartLong)) {
          _tmpStartLong = null;
        } else {
          _tmpStartLong = _cursor.getDouble(_cursorIndexOfStartLong);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpEndLong;
        if (_cursor.isNull(_cursorIndexOfEndLong)) {
          _tmpEndLong = null;
        } else {
          _tmpEndLong = _cursor.getDouble(_cursorIndexOfEndLong);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _result = new Trip(_tmpTid,_tmpName,_tmpStartLong,_tmpStartLat,_tmpEndLong,_tmpEndLat,_tmpNotes);
        final String _tmpImageURL;
        _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
        _result.setImageURL(_tmpImageURL);
        final String _tmpPlaceImageURL;
        _tmpPlaceImageURL = _cursor.getString(_cursorIndexOfPlaceImageURL);
        _result.setPlaceImageURL(_tmpPlaceImageURL);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        _result.setYear(_tmpYear);
        final int _tmpMonth;
        _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
        _result.setMonth(_tmpMonth);
        final int _tmpDay;
        _tmpDay = _cursor.getInt(_cursorIndexOfDay);
        _result.setDay(_tmpDay);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        _result.setHour(_tmpHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        _result.setMinute(_tmpMinute);
        final int _tmpRounded;
        _tmpRounded = _cursor.getInt(_cursorIndexOfRounded);
        _result.setRounded(_tmpRounded);
        final String _tmpStartLocationName;
        _tmpStartLocationName = _cursor.getString(_cursorIndexOfStartLocationName);
        _result.setStartLocationName(_tmpStartLocationName);
        final String _tmpEndLocationName;
        _tmpEndLocationName = _cursor.getString(_cursorIndexOfEndLocationName);
        _result.setEndLocationName(_tmpEndLocationName);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        _result.setStatus(_tmpStatus);
        final int _tmpTripStatus;
        _tmpTripStatus = _cursor.getInt(_cursorIndexOfTripStatus);
        _result.setTripStatus(_tmpTripStatus);
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final String _tmpFirebaseKey;
        _tmpFirebaseKey = _cursor.getString(_cursorIndexOfFirebaseKey);
        _result.setFirebaseKey(_tmpFirebaseKey);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countUpcommingTrips(String uid) {
    final String _sql = "SELECT count(tid) FROM trip WHERE userId = ? AND tripStatus = 0 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countDoneTrips(String uid) {
    final String _sql = "SELECT count(tid) FROM trip WHERE userId = ? AND tripStatus = 1 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countcancelledTrips(String uid) {
    final String _sql = "SELECT count(tid) FROM trip WHERE userId = ? AND tripStatus = 2 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getMaxId() {
    final String _sql = "SELECT max(tid) FROM trip";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
