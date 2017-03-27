package appleoctopus.lastword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.util.SharePreference;

public class CatogoryActivity extends AppCompatActivity {
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
            Video v = new Video();
            v.setLocalVideoUri(videoUri.toString());
            v.setCategory(requestCode);
            FirebaseDB.getInstance().saveNewVideo(v, SharePreference.getFirebaseId(this));
        }
    }
}
