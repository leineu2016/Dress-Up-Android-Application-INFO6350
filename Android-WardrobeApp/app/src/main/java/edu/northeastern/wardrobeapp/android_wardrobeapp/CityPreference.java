package edu.northeastern.wardrobeapp.android_wardrobeapp;

/**
 * Created by ruotianwang on 4/17/17.
 */

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Seattle as the default city
    String getCity(){
        return prefs.getString("city", "Seattle, US");
    }

    void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }

}
