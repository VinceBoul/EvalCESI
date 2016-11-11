package com.vince.evalcesi.helper;

/**
 * Created by Vince on 07/11/2016.
 */
import com.vince.evalcesi.model.Message;
import com.vince.evalcesi.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by sca on 03/06/15.
 */
public class JsonParser {

    /**
     * Retourne une liste d'objets Message
     * @param json
     * @return List messages
     * @throws JSONException
     */
    public static List<Message> getMessages(String json) throws JSONException {
        List<Message> messages = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        Message msg;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            if (obj.optString("message").length() > 0){
                msg = new Message(obj.optString("username"), obj.optString("message"), obj.optLong("date"));
                messages.add(msg);
            }
        }

        return messages;
    }

    public static String getToken(String response) throws JSONException {
        return new JSONObject(response).optString("token");
    }

    public static List<String> getUsers(String response) throws JSONException {
        JSONArray array = new JSONArray(response);
        List<String> users = new LinkedList<String>();
        for(int i=0; i<array.length(); i++){
            System.out.println(array.getString(i));
            users.add(array.getString(i));
        }
        return users;
    }

    public static List<Note> getNotes(String response) throws JSONException {
        List<Note> notes = new LinkedList<>();
        JSONArray array = new JSONArray(response);
        JSONObject obj;
        Note note;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            if (obj.optString("message").length() > 0){
                note = new Note(obj.optString("username"), obj.optString("message"), obj.optLong("date"));
                notes.add(note);
            }
        }

        return notes;
    }

    public static JSONObject convertJSONToObject(String userJSON){
        JSONObject jObject = new JSONObject();
        try {
            jObject = new JSONObject(userJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }
}