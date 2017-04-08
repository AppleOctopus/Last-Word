package appleoctopus.lastword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.upload.UploadIntentService;
import appleoctopus.lastword.util.SharePreference;
import appleoctopus.lastword.util.Util;

public class StoryActivity extends AppCompatActivity {

    private ImageView bgImageView;
    private ImageButton imageButton;
    private TextView textView;

    private int catogory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        catogory = getIntent().getIntExtra(CategoryDetailActivity.CATEGORY_KEY, -1);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(takeVideoIntent, catogory);
            }
        });
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

            String videoKey = FirebaseDB.getInstance().saveNewVideo(v, SharePreference.getFirebaseId(this));
            v.setVideoKey(videoKey);

            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, UploadIntentService.class);
            intent.putExtra("url", path);
            intent.putExtra("video", new Gson().toJson(v));
            startService(intent);

            finish();
        }
    }
}
