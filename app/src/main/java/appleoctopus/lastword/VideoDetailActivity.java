package appleoctopus.lastword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.upload.UploadIntentService;

public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static String VIDEO_KEY = "VIDEO_KEY";


    private ImageButton mShareButton;
    private ImageButton mCopyButton;
    private ImageView mImageView;

    private Video mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        mCopyButton = (ImageButton) findViewById(R.id.imageButton_copy);
        mShareButton = (ImageButton) findViewById(R.id.imageButton_share);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mCopyButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        String gson = getIntent().getStringExtra(VIDEO_KEY);
        if (gson == null) { return; }

        mVideo = (new Gson()).fromJson(gson, Video.class);
        Bitmap b = ThumbnailUtils.createVideoThumbnail(
                mVideo.getLocalVideoPath(),
                MediaStore.Video.Thumbnails.MINI_KIND);
        mImageView.setImageBitmap(b);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton_copy:

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", mVideo.getRemoteVideoUri());
                clipboard.setPrimaryClip(clip);

                if (mVideo.getRemoteExist()) {
                    Toast.makeText(this, "已複製連結：" + mVideo.getRemoteVideoUri(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "上傳中，請稍候再試", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, this, UploadIntentService.class);
                    intent.putExtra("url", mVideo.getLocalVideoPath());
                    intent.putExtra("video", new Gson().toJson(mVideo));
                    startService(intent);
                }
                break;

            case R.id.imageButton_share:

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                Uri videoUri = Uri.parse(mVideo.getLocalVideoUri());
                shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                shareIntent.setType("video/*");
                startActivity(Intent.createChooser(shareIntent, "請選擇"));
                break;

            case R.id.imageView:
                Uri uri = Uri.parse(mVideo.getLocalVideoUri());
                Intent play = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(play);
                break;

        }
    }
}
