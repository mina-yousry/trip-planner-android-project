package com.example.hazem.facebooklogin.presenters;

import com.example.hazem.facebooklogin.contract.FloatingServiceInterface;
import com.example.hazem.facebooklogin.entity.Note;
import com.example.hazem.facebooklogin.entity.Trip;
import com.example.hazem.facebooklogin.views.FloatingService;

import java.util.ArrayList;

/**
 * Created by Mina Yousry on 3/13/2018.
 */

public class FloatingServicePresenter implements FloatingServiceInterface.Presenter {

    private FloatingServiceInterface.Viewer floatingServiceViewer;
    private FloatingServiceInterface.Model floatingServiceModel;

    public FloatingServicePresenter(FloatingServiceInterface.Viewer floatingServiceViewer, FloatingServiceInterface.Model floatingServiceModel) {
        this.floatingServiceViewer = floatingServiceViewer;
        this.floatingServiceModel = floatingServiceModel;
    }

    @Override
    public void fetchNotes(String id) {
        floatingServiceModel.setPresenter(this);
        floatingServiceModel.fetchNotes(id);
    }

    @Override
    public void notifyFloatingServiceNotesFitched(ArrayList<Note> notes) {
        floatingServiceViewer.setNotes(notes);
    }

    @Override
    public void updateNotes(ArrayList<Note> notes,Trip t) {
        floatingServiceModel.updateNotes(notes,t);
    }
}
