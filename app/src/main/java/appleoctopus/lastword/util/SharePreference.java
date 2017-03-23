package appleoctopus.lastword.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by allenwang on 2017/3/23.
 */

public class SharePreference {
    private static final String DATA = "DATA";
    private static final String URI = "URI";



    public static String readUri(Context context){
        SharedPreferences settings = context.getSharedPreferences(DATA,0);
        return settings.getString(URI, "");
    }
    public static void saveUri(Context context, String path){
        SharedPreferences settings = context.getSharedPreferences(DATA,0);
        settings.edit()
                .putString(URI, path)
                .apply();
    }
}
