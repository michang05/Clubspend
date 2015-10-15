package com.interactiveplus.preferences;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Base preferences Implements the base methods for storing the plugin
 * preferences
 * 
 */
public class BasePreferences {

    // Android Preferences API
    protected static SharedPreferences settings;
    protected static SharedPreferences.Editor editor;
    /**
     * Load the plugin preferences
     */
    public static void load(Activity a) {
        settings = a.getSharedPreferences("clubspend", 0);
        editor = settings.edit();
    }

    /**
     * Store the plugin preferences
     */
    public static boolean save() {
        return editor.commit();
    }
   
}
