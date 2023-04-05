package com.myapplication.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "prefname";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static SharedPref singleton;

    @SuppressLint("ApplySharedPref")
    public SharedPref(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.commit();
    }

    private SharedPref() {
        sharedPreferences = App.get().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.commit();
    }

    public static synchronized SharedPref get() {
        if (singleton == null) {
            synchronized (SharedPref.class) {
                if (singleton == null) {
                    singleton = new SharedPref();
                }
            }
        }
        return singleton;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, false);
    }

    public void save(String key, Object value) {
        SharedPreferences.Editor editor = getEditor();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-supported preference");
        }
        editor.commit();
    }

    public String getStringValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public Long getLong(String key) {
        return sharedPreferences.getLong(key, 00);
    }

//    public <T> void setList(String key, List<T> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//
//        save(key, json);
//    }

    public void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }


    public String getIsLogin(String key) {
        return sharedPreferences.getString(key, null);
    }
}