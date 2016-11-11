package com.vince.evalcesi.model;

/**
 * Created by Vince on 11/11/2016.
 */

public class Note {

    public Note(String username, String message, long date) {
        this.username = username;
        this.msg = message;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }

    String username;
    String msg;

    public long getDate() {
        return date;
    }

    long date;

}
