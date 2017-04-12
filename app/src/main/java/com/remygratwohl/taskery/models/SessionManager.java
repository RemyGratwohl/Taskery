package com.remygratwohl.taskery.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Remy on 3/5/2017.
 */

public class SessionManager {

    private SharedPreferences userPref;
    private Context context;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "userPrefs";
    private static final int PREF_MODE = 0;
    private static final String USER_PREF_ID = "userObj";

    public SessionManager(Context context) {
        this.context = context;

        userPref = context.getSharedPreferences(PREF_NAME, PREF_MODE);
        editor = userPref.edit();
        
    }

    // Stores the user object in preferences for use in the app
    public boolean createUserSession(User user){
        editor.putString(USER_PREF_ID, new Gson().toJson(user));
        editor.commit();
        return true;
    }

    // Retrieves the stored user object from prefs
    public User retrieveSessionsUser(){
        String userString = userPref.getString(USER_PREF_ID,null);
        return(new Gson().fromJson(userString,User.class));
    }

    // "Logs out" the current user by deleting the preferences
    public boolean deleteUserSession(){
        editor.clear();
        editor.commit();
        return true;
    }

    public String retrieveUserToken(){
        return "JWT " + retrieveSessionsUser().getJwtToken();
    }

    public boolean doesSessionAlreadyExist(){
        return(userPref.contains(USER_PREF_ID));
    }

}
