package appleoctopus.lastword.Introduction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import appleoctopus.lastword.AfterSelfRecordActivity;
import appleoctopus.lastword.BaseDynamicViewActivity;
import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.upload.UploadIntentService;
import appleoctopus.lastword.util.SharePreference;
import appleoctopus.lastword.util.Util;

public class BeforeSelfRecordActivity extends BaseDynamicViewActivity {
    private static final String TAG = BeforeSelfRecordActivity.class.getSimpleName();
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatchTakeVideoIntent();
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
            String path = Util.getPathFromUri(this, Uri.parse(videoUri.toString()));

            Log.d(TAG, videoUri.toString());
            Log.d(TAG, videoUri.getPath());

            Video v = new Video();
            v.setLocalVideoUri(videoUri.toString());
            v.setLocalVideoPath(path);
            v.setCategory(Video.CATOGORY_LIFE);

            String videoKey = FirebaseDB.getInstance().saveNewVideo(v, SharePreference.getFirebaseId(this));
            Log.d(TAG, "Sync Call to firebase : videoKey="+videoKey);

            //settting video key into video object
            v.setVideoKey(videoKey);

            Intent sync = new Intent(Intent.ACTION_SYNC, null, this, UploadIntentService.class);
            intent.putExtra("url", v.getLocalVideoPath());
            intent.putExtra("video", new Gson().toJson(v));
            startService(sync);

            Intent redirect = new Intent();
            redirect.setClass(this, AfterSelfRecordActivity.class);
            startActivity(redirect);
         }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

}
