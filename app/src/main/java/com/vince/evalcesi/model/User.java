package com.vince.evalcesi.model;

import com.vince.evalcesi.helper.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vince on 08/11/2016.
 */

public class User {

    JSONObject myUser;

    public User(String userJSON) {

        myUser = JsonParser.convertJSONToObject(userJSON);
        String username = "";

        username = getUsername();
        String date = getDate();

        System.out.println(date);
        System.out.println(username);
    }

    public String getUsername() {
        try {
            return myUser.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDate() {

        String dateString = "";
        try {
            Long userDate = myUser.getLong("date");

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(userDate);
            dateString = dateFormat.format(date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}