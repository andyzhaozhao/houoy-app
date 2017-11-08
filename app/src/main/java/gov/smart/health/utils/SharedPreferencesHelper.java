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
}
