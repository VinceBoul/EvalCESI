package com.vince.evalcesi.model;

/**
 * Created by Vince on 11/11/2016.
 */

public class Note {

    String id;
    String username;
    String note;
    Boolean done;
    long date;

    public Note(String id, String username, String note, Boolean done, long date) {
        this.id = id;
        this.username = username;
        this.note = note;
        this.date = date;
        this.done = done;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getUsername() {
        return username;
    }

    public String getNote() {
        return note;
    }

    public long getDate() {
        return date;
    }

}
