package appleoctopus.lastword.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by allenwang on 2017/3/30.
 */

public class Util {
    public static String getPathFromUri(Context context, Uri uri) {


        String[] filePathColumn = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);

        String p = "";
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            p = cursor.getString(columnIndex);
        }

        cursor.close();
        return p;
    }
}
