package com.vince.evalcesi.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vince on 09/11/2016.
 */

public class PreferenceHelper {

    public static final String MY_PREF = "MesPrefs_deTchat";
    public static final String TOKEN_KEY = "MesPrefs_deTchat";

    public static void setToken(final Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.commit();
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, "");
    }
}
