package appleoctopus.lastword;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by allenwang on 2017/3/19.
 */

public class BaseDynamicViewActivity extends AppCompatActivity
        implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView = null;
    private TextView mTextView = null;
    private Button mbutton = null;


    private MediaPlayer mp = null;
    private Uri mUri = null;

    public void setBackground(int resId) {
        mUri = Uri.parse("android.resource://" + getPackageName()
                + "/" + resId);
    }

    public void setBackground(Uri uri) {
        this.mUri = uri;
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setButtonIntentDestination(final Activity activity, final Bundle bundle) {
        View.OnClickListener l = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(BaseDynamicViewActivity.this, activity.getClass());

                if (bundle != null) {
                    i.putExtras(bundle);
                }

                startActivity(i);
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
        setContentView(R.layout.activity_dynamic_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceView.getHolder().addCallback(this);
        mTextView = (TextView) findViewById(R.id.textView);
        mbutton = (Button) findViewById(R.id.button);

        setBackground(R.raw.people_walk);

        mp = new MediaPlayer();
        try {
            mp.setDataSource(this, mUri);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        //Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
        lp.width = screenWidth;
        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
        lp.height = screenHeight; //(int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);

        //Commit the layout parameters
        mSurfaceView.setLayoutParams(lp);

        //Start video
        mp.setLooping(true);
        mp.setDisplay(holder);
        mp.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
