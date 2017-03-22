package appleoctopus.lastword;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BeforeSelfRecordActivity extends BaseDynamicViewActivity {
    private static final String TAG = BeforeSelfRecordActivity.class.getSimpleName();
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            Log.d(TAG, videoUri.toString());

            saveTheVideo(intent);

            File videofiles = new File(videoUri.toString());
            boolean b = videofiles.delete();

            Intent i = new Intent();
            i.setClass(this, AfterSelfRecordActivity.class);
            startActivity(i);
         }
    }

    private void saveTheVideo(Intent data) {
        try {

            // 1. rename doesn't work!
            //File from = new File(data.getDataString());
            //File to = new File(getFilesDir(), "LastWords");
            //boolean b = from.renameTo(file);

            //outputStream.write(string.getBytes());
            //outputStream.close();

            AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
            FileInputStream fis = videoAsset.createInputStream();
            FileOutputStream fos = new FileOutputStream(getExternalStorageFile());

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(getInternalStorageFile());
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private File getInternalStorageFile() {
        return new File(getFilesDir(), "last.mp4");
    }

    private FileOutputStream getInternalStorageStream() {
        String filename = "myfile";
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    private File getExternalStorageFile() {
        File root = new File(Environment.getExternalStorageDirectory(), "last");
        if (!root.exists()) {
            root.mkdirs();
        }
       return new File(root, "last_"+System.currentTimeMillis()+".mp4" );
    }

}
