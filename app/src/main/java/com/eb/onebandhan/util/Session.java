package com.eb.onebandhan.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences pref;
    private Context context;
    private String name = "your_database";
    private SharedPreferences.Editor prefsEditor;
    private String ALL_USER_LIST = "ALL_USER_LIST";
    private String USER_PROFILE_DATA = "USER_PROFILE_DATA";

    public Session(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(name, 0); // 0 - for private mode
        prefsEditor = pref.edit();
    }

    public void clearSession() {
        prefsEditor.clear();
        prefsEditor.commit();
    }




    /*________________Set string data into session_____________*/

    public void setString(String name, String string) {

        prefsEditor.putString(name, string);
        prefsEditor.commit();

    }

    public String getString(String name) {

        String json = pref.getString(name, "");
        return json;
    }

}
