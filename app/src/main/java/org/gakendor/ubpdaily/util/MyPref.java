package org.gakendor.ubpdaily.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.gakendor.ubpdaily.clients.api.model.User;

/**
 * Created by Dizzay on 12/23/2017.
 */

public class MyPref {

    public static void putString(Context context, String key, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value).commit();
    }

    public static void putBoolean(Context context, String key, boolean value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value).commit();
    }

    public static void putInt(Context context, String key, int value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value).commit();
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Static.MyPref, Context.MODE_PRIVATE);
        return preferences.edit();
    }

    public static String getString(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(Static.MyPref, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static boolean getBoolean(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(Static.MyPref, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static int getInt(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(Static.MyPref, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public static void setLoginData(Context context, User data){
        String json = new Gson().toJson(data);
        putString(context, Static.LOGIN_DATA_KEY, json);
    }
//
    public static User getLoginData(Context context){
        try {
            String json = getString(context, Static.LOGIN_DATA_KEY);
            User data = new Gson().fromJson(json, User.class);
            return data;
        }catch (Exception e){
            return null;
        }
    }
//
    public static void logout(Context context){
        putString(context, Static.LOGIN_DATA_KEY, "");
    }

    public static void setPoin(String poin, Context context){
        putString(context, Static.POINT_OUTLET, poin);
    }

    public static String getPoin(Context context){
        return getString(context, Static.POINT_OUTLET);
    }
}
