package com.retailer.oneops.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.retailer.oneops.auth.model.MUser;
import com.google.gson.Gson;

public class Session {

    private SharedPreferences pref;
    private Context context;
    private String name = "your_database";
    private SharedPreferences.Editor prefsEditor;
    private String ALL_USER_LIST = "ALL_USER_LIST";
    private String USER_PROFILE_DATA = "USER_PROFILE_DATA";
    private String INVENTORY_TYPE = "INVENTORY_TYPE";
    private String USER_BANK_DETAIL = "USER_BANK_DETAIL";
    private static final String PREFERENCE = "ONE_OPS_RETAILER";

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

    public void setInventoryType(String inventoryType) {
        prefsEditor.putString(INVENTORY_TYPE, inventoryType);
        prefsEditor.commit();

    }

    public String getInventoryType() {
        String inventoryType = pref.getString(INVENTORY_TYPE, null);
        return inventoryType;
    }

    //save Integer preferences
    public void setIntegerSharedPreference(Context context, String name, int value) {
        this.context = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    //Save Integer
    public  int getIngerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getInt(name, 0);
    }

    //Save boolean
    public  void setSharedPreferenceBoolean(Context context, String name, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    //Get boolean
    public static boolean getSharedPreferencesBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getBoolean(name, false);
    }

    public void setSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.apply();
    }

    //Get String
    public String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        if (name == null) {
            name = "";
        }
        return settings.getString(name, "");
    }
}
