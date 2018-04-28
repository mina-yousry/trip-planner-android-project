package com.example.hazem.facebooklogin.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hazem.facebooklogin.R;

/**
 * Created by Mina on 2/27/2018.
 */

public class FloatingNotesViewHolder extends RecyclerView.ViewHolder {

    private View myView;
    private TextView countryNameTextView;
    private CheckBox myCheckBox;
    private boolean checkBoxChecked;

    public FloatingNotesViewHolder(View v) {
        super(v);
        myView = v;
    }

    public TextView getCountryNameTextView() {
        countryNameTextView = (TextView) myView.findViewById(R.id.county_list_name);
        return countryNameTextView;
    }

    public CheckBox getMyCheckBox() {
        myCheckBox = (CheckBox) myView.findViewById(R.id.row_checkbox);
        return myCheckBox;
    }

    public View getMyView() {
        return myView;
    }

    public boolean isCheckBoxChecked() {
        return checkBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        this.checkBoxChecked = checkBoxChecked;
    }
}
