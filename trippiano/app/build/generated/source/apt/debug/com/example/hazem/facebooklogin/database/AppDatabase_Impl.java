package com.example.hazem.facebooklogin.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import com.example.hazem.facebooklogin.dao.NoteDao;
import com.example.hazem.facebooklogin.dao.NoteDao_Impl;
import com.example.hazem.facebooklogin.dao.TripDao;
import com.example.hazem.facebooklogin.dao.TripDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class AppDatabase_Impl extends AppDatabase {
  private volatile TripDao _tripDao;

  private volatile NoteDao _noteDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Trip` (`tid` INTEGER NOT NULL, `name` TEXT, `start_long` REAL, `start_lat` REAL, `end_long` REAL, `end_lat` REAL, `notes` TEXT, `imageURL` TEXT, `placeImageURL` TEXT, `year` INTEGER NOT NULL, `month` INTEGER NOT NULL, `day` INTEGER NOT NULL, `hour` INTEGER NOT NULL, `minute` INTEGER NOT NULL, `rounded` INTEGER NOT NULL, `startLocationName` TEXT, `endLocationName` TEXT, `status` INTEGER NOT NULL, `tripStatus` INTEGER NOT NULL, `userId` TEXT, `firebaseKey` TEXT, PRIMARY KEY(`tid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Note` (`nid` INTEGER NOT NULL, `name` TEXT, `tid` TEXT, `checked` INTEGER NOT NULL, PRIMARY KEY(`nid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"8a0890b3d7b4d0d92ab897c570917673\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Trip`");
        _db.execSQL("DROP TABLE IF EXISTS `Note`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTrip = new HashMap<String, TableInfo.Column>(21);
        _columnsTrip.put("tid", new TableInfo.Column("tid", "INTEGER", true, 1));
        _columnsTrip.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsTrip.put("start_long", new TableInfo.Column("start_long", "REAL", false, 0));
        _columnsTrip.put("start_lat", new TableInfo.Column("start_lat", "REAL", false, 0));
        _columnsTrip.put("end_long", new TableInfo.Column("end_long", "REAL", false, 0));
        _columnsTrip.put("end_lat", new TableInfo.Column("end_lat", "REAL", false, 0));
        _columnsTrip.put("notes", new TableInfo.Column("notes", "TEXT", false, 0));
        _columnsTrip.put("imageURL", new TableInfo.Column("imageURL", "TEXT", false, 0));
        _columnsTrip.put("placeImageURL", new TableInfo.Column("placeImageURL", "TEXT", false, 0));
        _columnsTrip.put("year", new TableInfo.Column("year", "INTEGER", true, 0));
        _columnsTrip.put("month", new TableInfo.Column("month", "INTEGER", true, 0));
        _columnsTrip.put("day", new TableInfo.Column("day", "INTEGER", true, 0));
        _columnsTrip.put("hour", new TableInfo.Column("hour", "INTEGER", true, 0));
        _columnsTrip.put("minute", new TableInfo.Column("minute", "INTEGER", true, 0));
        _columnsTrip.put("rounded", new TableInfo.Column("rounded", "INTEGER", true, 0));
        _columnsTrip.put("startLocationName", new TableInfo.Column("startLocationName", "TEXT", false, 0));
        _columnsTrip.put("endLocationName", new TableInfo.Column("endLocationName", "TEXT", false, 0));
        _columnsTrip.put("status", new TableInfo.Column("status", "INTEGER", true, 0));
        _columnsTrip.put("tripStatus", new TableInfo.Column("tripStatus", "INTEGER", true, 0));
        _columnsTrip.put("userId", new TableInfo.Column("userId", "TEXT", false, 0));
        _columnsTrip.put("firebaseKey", new TableInfo.Column("firebaseKey", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrip = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrip = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrip = new TableInfo("Trip", _columnsTrip, _foreignKeysTrip, _indicesTrip);
        final TableInfo _existingTrip = TableInfo.read(_db, "Trip");
        if (! _infoTrip.equals(_existingTrip)) {
          throw new IllegalStateException("Migration didn't properly handle Trip(com.example.hazem.facebooklogin.entity.Trip).\n"
                  + " Expected:\n" + _infoTrip + "\n"
                  + " Found:\n" + _existingTrip);
        }
        final HashMap<String, TableInfo.Column> _columnsNote = new HashMap<String, TableInfo.Column>(4);
        _columnsNote.put("nid", new TableInfo.Column("nid", "INTEGER", true, 1));
        _columnsNote.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsNote.put("tid", new TableInfo.Column("tid", "TEXT", false, 0));
        _columnsNote.put("checked", new TableInfo.Column("checked", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNote = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNote = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNote = new TableInfo("Note", _columnsNote, _foreignKeysNote, _indicesNote);
        final TableInfo _existingNote = TableInfo.read(_db, "Note");
        if (! _infoNote.equals(_existingNote)) {
          throw new IllegalStateException("Migration didn't properly handle Note(com.example.hazem.facebooklogin.entity.Note).\n"
                  + " Expected:\n" + _infoNote + "\n"
                  + " Found:\n" + _existingNote);
        }
      }
    }, "8a0890b3d7b4d0d92ab897c570917673");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Trip","Note");
  }

  @Override
  public TripDao getTripDao() {
    if (_tripDao != null) {
      return _tripDao;
    } else {
      synchronized(this) {
        if(_tripDao == null) {
          _tripDao = new TripDao_Impl(this);
        }
        return _tripDao;
      }
    }
  }

  @Override
  public NoteDao getNoteDao() {
    if (_noteDao != null) {
      return _noteDao;
    } else {
      synchronized(this) {
        if(_noteDao == null) {
          _noteDao = new NoteDao_Impl(this);
        }
        return _noteDao;
      }
    }
  }
}
