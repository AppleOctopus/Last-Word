package appleoctopus.lastword.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by allenwang on 2017/3/23.
 */

public class SharePreference {
    private static final String DATA = "DATA";
    private static final String URI = "URI";
    private static final String FIREBASE_ID = "FIRE_BASE_ID";
    private static final String IS_FIRST_OPEN = "IS_FIRSE_OPEN";

    public static boolean getIsFirstOpen(Context context){
        SharedPreferences settings = context.getSharedPreferences(DATA, MODE_PRIVATE);
        return settings.getBoolean(IS_FIRST_OPEN, true);
    }
    public static void setIsFirstOpen(Context context, boolean isFirstOpen){
        SharedPreferences settings = context.getSharedPreferences(DATA, MODE_PRIVATE);
        settings.edit().putBoolean(IS_FIRST_OPEN, isFirstOpen).apply();
    }

    public static String getFirebaseId(Context context){
        SharedPreferences settings = context.getSharedPreferences(DATA, MODE_PRIVATE);
        return settings.getString(FIREBASE_ID, "");
    }
    public static void setFirebaseId(Context context, String id){
        SharedPreferences settings = context.getSharedPreferences(DATA, MODE_PRIVATE);
        settings.edit()
                .putString(FIREBASE_ID, id)
                .apply();
    }

    public static String getUri(Context context){
        SharedPreferences settings = context.getSharedPreferences(DATA, MODE_PRIVATE);
        return settings.getString(URI, "");
    }
    public static void setUri(Context context, String path){
        SharedPreferences settings = context.getSharedPreferences(DATA, MODE_PRIVATE);
        settings.edit()
                .putString(URI, path)
                .apply();
    }
}
