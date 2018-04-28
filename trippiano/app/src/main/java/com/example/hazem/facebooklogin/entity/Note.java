package com.example.hazem.facebooklogin.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Hazem on 3/11/2018.
 */

@Entity
public class Note {
    @PrimaryKey
    private int nid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "tid")
    private String tid;

    @ColumnInfo(name = "checked")
    private int checked;


    @Ignore
    public Note() {

    }

    public Note(int nid, String name, String tid, int checked) {
        this.nid = nid;
        this.name = name;
        this.tid = tid;
        this.checked = checked;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Note{" +
                "nid=" + nid +
                ", name='" + name + '\'' +
                ", tid=" + tid +
                ", checked=" + checked +
                '}';
    }
}
