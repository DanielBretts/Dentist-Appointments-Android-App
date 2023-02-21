package com.example.patientqueuemanagement.Utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class SharedPreferences {
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String YEAR_OF_BIRTH = "YEAR_OF_BIRTH";
    public static final String PHONE = "PHONE";
    public static final String ADDRESS = "ADDRESS";
    public static final String NOTES = "NOTES";
    public static final String ANOTHER_DOCTOR = "ANOTHER_DOCTOR";
    public static final String DAY = "DAY";
    public static final String DAY_NAME = "DAY_NAME";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
    public static final String HOUR = "HOUR";
    public static final String DB_FILE = "DB_FILE";
    public static final String[] KEYS = {FIRST_NAME,LAST_NAME, YEAR_OF_BIRTH,PHONE,ANOTHER_DOCTOR,ADDRESS,NOTES};
    private static SharedPreferences instance = null;
    private static android.content.SharedPreferences preferences;
    private static android.content.SharedPreferences.Editor editor;

    private SharedPreferences(Context context) {
        preferences = context.getSharedPreferences(DB_FILE,context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void init(Context context){
        if(instance == null)
            instance = new SharedPreferences(context);
    }

    public static SharedPreferences getInstance(){
        return instance;
    }


    public void setString(String key,String value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public static android.content.SharedPreferences.Editor getEditor() {
        return editor;
    }

    public String getString(String key){
        return preferences.getString(key,"");
    }

    public void clearInputsText() {
        for (String key:SharedPreferences.KEYS){
            editor.remove(key).apply();
        }

    }

    public void setInt(String key, int value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public int getInt(String key){
        return preferences.getInt(key,0);
    }

}
