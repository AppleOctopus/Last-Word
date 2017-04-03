package appleoctopus.lastword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.upload.UploadIntentService;
import appleoctopus.lastword.util.SharePreference;
import appleoctopus.lastword.util.Util;

public class CatogoryActivity extends AppCompatActivity {
    public static final String TAG = CatogoryActivity.class.getSimpleName();
    static final int CAMERA_REQUEST = 1;
    static final String CATEGORY = "CATEGORY";

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catogory);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CatogoryRecyclerViewAdapter(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            String path = Util.getPathFromUri(this, Uri.parse(videoUri.toString()));

            Video v = new Video();
            v.setLocalVideoUri(videoUri.toString());
            v.setCategory(requestCode);
            v.setLocalVideoPath(path);

            Log.d(TAG, "Sync Call to firebase");
            //Synchronous Call
            String videoKey = FirebaseDB.getInstance().saveNewVideo(v, SharePreference.getFirebaseId(this));
            Log.d(TAG, "Sync Call to firebase : videoKey="+videoKey);

            //settting video key into video object
            v.setVideoKey(videoKey);

            Log.d(TAG, "ASync Call to UploadIntentService");
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, UploadIntentService.class);
            intent.putExtra("url", path);
            intent.putExtra("video", new Gson().toJson(v));
            startService(intent);
        }
    }
}
