package com.rba.pushnotification.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ricardo Bravo on 24/08/16.
 */

public class SessionManager {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String KEY_DEVICE_TOKEN = "DEVICE_TOKEN";
    private static final String PREF_NAME = "FIREBASE";

    private static void initSessionManager(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        editor = sharedPreferences.edit();
        editor.apply();
        //editor.apply();
    }

    public static void addDeviceToken(Context context, String token) {
        initSessionManager(context);
        editor.putString(KEY_DEVICE_TOKEN, token);
        editor.commit();
    }

    public static String getDeviceToken(Context context) {
        initSessionManager(context);
        return sharedPreferences.getString(KEY_DEVICE_TOKEN, "");
    }

    public static boolean isToken(Context context) {
        initSessionManager(context);
        Log.i("x- token", ""+sharedPreferences.getString(KEY_DEVICE_TOKEN, "").length());
        return sharedPreferences.getString(KEY_DEVICE_TOKEN, "").length()>0;
    }

}
