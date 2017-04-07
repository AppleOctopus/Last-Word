package appleoctopus.lastword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.upload.UploadIntentService;
import appleoctopus.lastword.util.SharePreference;
import appleoctopus.lastword.util.Util;

import static appleoctopus.lastword.firebase.FirebaseDB.VIDEO;

public class CatogoryActivity extends AppCompatActivity {
    public static final String TAG = CatogoryActivity.class.getSimpleName();
    static final int CAMERA_REQUEST = 1;
    static final String CATEGORY = "CATEGORY";

    RecyclerView recyclerView;
    CatogoryRecyclerViewAdapter adapter;
    Set<Integer> intSet = new HashSet<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catogory);
        adapter = new CatogoryRecyclerViewAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        final ArrayList arrayList = new ArrayList();
        intSet = new HashSet<Integer>();

        FirebaseDB.getInstance()
                .getReference(VIDEO)
                .child(SharePreference.getFirebaseId(this))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot video : dataSnapshot.getChildren()) {
                        Video v = video.getValue(Video.class);
                        if (v != null) {
//                            arrayList.add(v);
                            int i = v.getCategory();
                            intSet.add(i);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter.updateData(intSet);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.updateData(intSet);
        adapter.notifyDataSetChanged();
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
