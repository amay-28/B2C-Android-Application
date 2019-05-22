package com.eb.onebandhan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.bankDetail.activity.model.MBankDetail;
import com.google.gson.Gson;

public class Session {

    private SharedPreferences pref;
    private Context context;
    private String name = "your_database";
    private SharedPreferences.Editor prefsEditor;
    private String ALL_USER_LIST = "ALL_USER_LIST";
    private String USER_PROFILE_DATA = "USER_PROFILE_DATA";
    private String USER_BANK_DETAIL = "USER_BANK_DETAIL";

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

    public MUser getUserProfile() {

        Gson gson = new Gson();
        String json = pref.getString(USER_PROFILE_DATA, "");

        MUser userProfile;

        if (json.isEmpty()) {
            userProfile = new MUser();
        } else {
            userProfile = gson.fromJson(json, MUser.class);
        }
        return userProfile;
    }

    public void setUserProfile(MUser UserProfile) {

        Gson gson = new Gson();
        String json = gson.toJson(UserProfile);
        prefsEditor.putString(USER_PROFILE_DATA, json);
        prefsEditor.commit();

    }

}
