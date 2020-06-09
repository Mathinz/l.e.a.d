package app.dev.lead.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import app.dev.lead.models.pojos.LoginPojo;

public class PreferenceHandler {

    public static final String PREF_NAME = "APPFRAMEWORK_PREFERENCES";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String PREF_KEY_LOGIN = "PREF_KEY_LOGIN";
    public static final String PREF_KEY_USER_DATA = "PREF_KEY_USER_DATA";

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void saveLogin(Context context, Boolean mode) {
        writeBoolean(context, PREF_KEY_LOGIN, mode);
    }

    public static Boolean isLogin (Context context) {
        return  readBoolean(context, PREF_KEY_LOGIN, false);
    }


    public static boolean saveUserData(Context context, LoginPojo userData) {
        try {
            writeString(context, PREF_KEY_USER_DATA, new Gson().toJson(userData));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LoginPojo getUserData(Context context) {
        return new Gson().fromJson(readString(context, PREF_KEY_USER_DATA, null), LoginPojo.class);
    }

}
