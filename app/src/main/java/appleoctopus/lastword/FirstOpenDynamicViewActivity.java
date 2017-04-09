package appleoctopus.lastword;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.upload.UploadIntentService;
import appleoctopus.lastword.util.SharePreference;
import appleoctopus.lastword.util.Util;

/**
 * Created by allenwang on 2017/3/19.
 */

public class FirstOpenDynamicViewActivity extends AppCompatActivity {
    public static final String TAG ="FirstOpenDynamic";
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int CODE_FOR_WRITE_PERMISSION = 1;

    private ImageView mImageView = null;
    private TextView mTextView1 = null;
    private TextView mTextView2 = null;
    private TextView mTextView3 = null;
    private TextView mTextView4 = null;
    private TextView mTextView5 = null;
    private ArrayList<TextView> mTextViewArray = new ArrayList<>();
    private TextView currentTextView = null;
    private Button mbutton = null;
    private ImageView ivStartSelfie = null;
    private TextView tvStartSelfie = null;

    private int pitchIndex = 0;
    private String[] pitchTextArray = {"慢慢鬆開你的眉頭、\n",
            "細細調整你的呼吸，\n",
            "暫時放空腦袋中複雜的思緒，\n",
            "此刻的你，\n",
            "最想對自己說些什麼呢？"};
    private String callForAction = "此刻的寧靜，只屬於你。";

    private int outDuration = 2000;

    public void setButtonIntentDestination(final Activity activity, final Bundle bundle) {
        View.OnClickListener l = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(FirstOpenDynamicViewActivity.this, activity.getClass());

                if (bundle != null) {
                    i.putExtras(bundle);
                }

                startActivity(i);
                finish();
            }
        };

        mbutton.setOnClickListener(l);
    }

    public void setButtonListener(View.OnClickListener l) {
        mbutton.setOnClickListener(l);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstopen_view);
        mImageView = (ImageView) findViewById(R.id.ivBgImageView);
        mTextView1 = (TextView) findViewById(R.id.textView1);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView3 = (TextView) findViewById(R.id.textView3);
        mTextView4 = (TextView) findViewById(R.id.textView4);
        mTextView5 = (TextView) findViewById(R.id.textView5);
        mbutton = (Button) findViewById(R.id.button);
        ivStartSelfie = (ImageView) findViewById(R.id.iv_start_selfie);
        tvStartSelfie = (TextView) findViewById(R.id.tv_start_selfie);


        //ivStartSelfie
        ivStartSelfie.setImageResource(0);

        mTextViewArray.add(mTextView1);
        mTextViewArray.add(mTextView2);
        mTextViewArray.add(mTextView3);
        mTextViewArray.add(mTextView4);
        mTextViewArray.add(mTextView5);

        //Commit the layout parameters
        mImageView.setImageResource(R.drawable.story0);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        mTextView1.setText(pitchTextArray[pitchIndex++]);


        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(outDuration);

        mbutton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if(pitchIndex<5) {
                    synchronized(FirstOpenDynamicViewActivity.class) {
                        final Animation in = new AlphaAnimation(0.0f, 1.0f);
                        in.setDuration(2000);
                        currentTextView = mTextViewArray.get(pitchIndex);
                        Log.d(TAG, "pitchIndex=" + String.valueOf(pitchIndex));
                        Log.d(TAG, "currentTextView=" + currentTextView);
                        currentTextView.setText(pitchTextArray[pitchIndex++]);
                        currentTextView.startAnimation(in);
                    }
                }else{
                    for( final TextView tv : mTextViewArray){
                        mbutton.setVisibility(View.INVISIBLE);
                        final Animation out = new AlphaAnimation(1.0f, 0.0f);
                        outDuration += 400;
                        out.setDuration(outDuration);
                        Log.d(TAG,"tv.getId()="+tv.getId());
                        if(tv.getId() == R.id.textView5){
                            out.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    tv.setText("");


                                    ivStartSelfie.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            askUserPermission();

                                        }
                                    });

                                    final Animation in = new AlphaAnimation(0.0f, 1.0f);
                                    in.setDuration(2000);

                                    ivStartSelfie.setBackgroundResource(R.drawable.camera01);
                                    tvStartSelfie.setText(callForAction);

                                    ivStartSelfie.startAnimation(in);
                                    tvStartSelfie.startAnimation(in);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                        }else{
                            out.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    tv.setText("");
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }

                        tv.startAnimation(out);
                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void askUserPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {android.Manifest.permission.CAMERA},
                        CODE_FOR_WRITE_PERMISSION);
            }
        } else {
            dispatchTakeVideoIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            if (permissions[0].equals(android.Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakeVideoIntent();
            }else{

            }
        }
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
            finish();

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
