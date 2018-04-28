package com.example.hazem.facebooklogin.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import com.example.hazem.facebooklogin.entity.Note;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

public class NoteDao_Impl implements NoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfNote;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAllTrips;

  private final SharedSQLiteStatement __preparedStmtOfRemoveNoteById;

  public NoteDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNote = new EntityInsertionAdapter<Note>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Note`(`nid`,`name`,`tid`,`checked`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Note value) {
        stmt.bindLong(1, value.getNid());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getTid() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTid());
        }
        stmt.bindLong(4, value.getChecked());
      }
    };
    this.__deletionAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Note` WHERE `nid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Note value) {
        stmt.bindLong(1, value.getNid());
      }
    };
    this.__preparedStmtOfRemoveAllTrips = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from note";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveNoteById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from Note where tid = ?";
        return _query;
      }
    };
  }

  @Override
  public void addNote(Note note) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfNote.insert(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Note note) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfNote.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
  public void removeNoteById(int tripid) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveNoteById.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, tripid);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveNoteById.release(_stmt);
    }
  }

  @Override
  public List<Note> getAllNotes() {
    final String _sql = "SELECT * FROM note";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfNid = _cursor.getColumnIndexOrThrow("nid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfChecked = _cursor.getColumnIndexOrThrow("checked");
      final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Note _item;
        final int _tmpNid;
        _tmpNid = _cursor.getInt(_cursorIndexOfNid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpTid;
        _tmpTid = _cursor.getString(_cursorIndexOfTid);
        final int _tmpChecked;
        _tmpChecked = _cursor.getInt(_cursorIndexOfChecked);
        _item = new Note(_tmpNid,_tmpName,_tmpTid,_tmpChecked);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Note> getAll(String tripid) {
    final String _sql = "SELECT * FROM note WHERE tid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tripid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tripid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfNid = _cursor.getColumnIndexOrThrow("nid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfChecked = _cursor.getColumnIndexOrThrow("checked");
      final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Note _item;
        final int _tmpNid;
        _tmpNid = _cursor.getInt(_cursorIndexOfNid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpTid;
        _tmpTid = _cursor.getString(_cursorIndexOfTid);
        final int _tmpChecked;
        _tmpChecked = _cursor.getInt(_cursorIndexOfChecked);
        _item = new Note(_tmpNid,_tmpName,_tmpTid,_tmpChecked);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Note> loadAllByIds(int[] tripIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM note WHERE tid IN (");
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
      final int _cursorIndexOfNid = _cursor.getColumnIndexOrThrow("nid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfChecked = _cursor.getColumnIndexOrThrow("checked");
      final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Note _item_1;
        final int _tmpNid;
        _tmpNid = _cursor.getInt(_cursorIndexOfNid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpTid;
        _tmpTid = _cursor.getString(_cursorIndexOfTid);
        final int _tmpChecked;
        _tmpChecked = _cursor.getInt(_cursorIndexOfChecked);
        _item_1 = new Note(_tmpNid,_tmpName,_tmpTid,_tmpChecked);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Note findByName(String first) {
    final String _sql = "SELECT * FROM note WHERE name LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (first == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, first);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfNid = _cursor.getColumnIndexOrThrow("nid");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfTid = _cursor.getColumnIndexOrThrow("tid");
      final int _cursorIndexOfChecked = _cursor.getColumnIndexOrThrow("checked");
      final Note _result;
      if(_cursor.moveToFirst()) {
        final int _tmpNid;
        _tmpNid = _cursor.getInt(_cursorIndexOfNid);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpTid;
        _tmpTid = _cursor.getString(_cursorIndexOfTid);
        final int _tmpChecked;
        _tmpChecked = _cursor.getInt(_cursorIndexOfChecked);
        _result = new Note(_tmpNid,_tmpName,_tmpTid,_tmpChecked);
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
  public int getMaxNoteId() {
    final String _sql = "SELECT max(nid) FROM Note";
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

  @Override
  public int getMaxId() {
    final String _sql = "SELECT max(tid) FROM Note";
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
