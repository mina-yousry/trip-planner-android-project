package com.example.hazem.facebooklogin.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hazem.facebooklogin.R;
import com.example.hazem.facebooklogin.database.AppDatabase;
import com.example.hazem.facebooklogin.entity.Note;

import java.util.ArrayList;

/**
 * Created by Mina Yousry on 3/14/2018.
 */

public class PopupNotesViewAdapter extends RecyclerView.Adapter<FloatingNotesViewHolder>{

    private ArrayList<Note> tripNotes;
    private View rowView;
    private TextView countryNameTextView;
    private ImageView countryFlafImageView;
    private FloatingNotesViewHolder floatingNotesViewHolder;
    private CheckBox noteCheckBox;
    private AppDatabase database;
    private NotesPopWindowUpdater notesPopWindowUpdater;

    public PopupNotesViewAdapter(ArrayList<Note> countries ,NotesPopWindowUpdater notesPopWindowUpdater) {
        tripNotes = countries;
        this.notesPopWindowUpdater = notesPopWindowUpdater;
    }



    @Override
    public FloatingNotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        rowView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.note_single_row, parent, false);
        floatingNotesViewHolder = new FloatingNotesViewHolder(rowView);
        floatingNotesViewHolder.setIsRecyclable(false);
        return floatingNotesViewHolder;
    }

    @Override
    public void onBindViewHolder(final FloatingNotesViewHolder holder, final int position) {

        floatingNotesViewHolder =holder;
        countryNameTextView = holder.getCountryNameTextView();
        countryNameTextView.setText(tripNotes.get(position).getName());
        noteCheckBox = holder.getMyCheckBox();
        final Note note = tripNotes.get(position);
        if (note.getChecked()==0){
            noteCheckBox.setChecked(false);
        }else {
            noteCheckBox.setChecked(true);
        }
        noteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    note.setChecked(1);
                    notesPopWindowUpdater.updateNote(note,position);
                }else {
                    note.setChecked(0);
                    notesPopWindowUpdater.updateNote(note,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripNotes.size();
    }
}
