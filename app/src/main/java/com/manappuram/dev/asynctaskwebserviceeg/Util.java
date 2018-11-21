package com.manappuram.dev.asynctaskwebserviceeg;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Util {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final int NOT_LOGGED_IN = -1;
    public static void showText(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void saveString(Context context,String key, String value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void saveInt(Context context,String key, int value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key,defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key,defaultValue);
    }
}
