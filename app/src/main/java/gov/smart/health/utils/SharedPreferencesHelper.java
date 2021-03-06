package gov.smart.health.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by laoniu on 11/7/17.
 */
public class SharedPreferencesHelper {

    private static SharedPreferences sharedPref;

    public SharedPreferencesHelper(Context context){
        if(sharedPref == null) {
            sharedPref = context.getSharedPreferences("SHhealth",Context.MODE_PRIVATE);
        }
    }

    public static void settingString(String key ,String value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String gettingString(String key ,String defaultvalue){
       return sharedPref.getString(key,defaultvalue);
    }

    public static void settingBoolean(String key ,Boolean value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean gettingBoolean(String key ,Boolean defaultvalue){
        return sharedPref.getBoolean(key,defaultvalue);
    }

    public static void settingLong(String key ,long value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long gettingLong(String key ,long defaultvalue){
        return sharedPref.getLong(key,defaultvalue);
    }
}
