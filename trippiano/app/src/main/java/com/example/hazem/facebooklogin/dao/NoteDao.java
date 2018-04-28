package com.example.hazem.facebooklogin.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.hazem.facebooklogin.entity.Note;

import java.util.List;

/**
 * Created by Hazem on 3/11/2018.
 */

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE tid = :tripid")
    List<Note> getAll(String tripid);

    @Query("SELECT * FROM note WHERE tid IN (:tripIds)")
    List<Note> loadAllByIds(int[] tripIds);

    @Query("SELECT * FROM note WHERE name LIKE :first LIMIT 1")
    Note findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNote(Note note);
    //@Insert
    //void insertAll(Trip... trips);

    @Delete
    void delete(Note note);

    @Query("delete from note")
    void removeAllTrips();

    @Query("SELECT max(nid) FROM Note")
    int getMaxNoteId();
    @Query("SELECT max(tid) FROM Note")
    int getMaxId();

    @Query("delete from Note where tid = :tripid")
    void removeNoteById(int tripid);
}
