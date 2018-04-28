package com.example.hazem.facebooklogin.contract;

import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.presenters.FloatingServicePresenter;

import java.util.ArrayList;

/**
 * Created by Mina Yousry on 3/13/2018.
 */

public interface FloatingServiceInterface {

    interface Model{
        void fetchNotes(String id);
        void updateNotes(ArrayList<Note> notes,Trip t);

        void setPresenter(FloatingServicePresenter floatingServicePresenter);
    }

    interface Viewer{
        void setNotes(ArrayList<Note> notes);
    }

    interface Presenter{
        public void fetchNotes(String id);
        void notifyFloatingServiceNotesFitched(ArrayList<Note> notes);
        void updateNotes(ArrayList<Note> notes,Trip t);
    }
}
