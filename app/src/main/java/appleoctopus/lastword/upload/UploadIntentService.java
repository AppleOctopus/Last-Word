package appleoctopus.lastword.upload;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import appleoctopus.lastword.R;
import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.http.API;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.util.SharePreference;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadIntentService extends IntentService {
    private static final String TAG = "uploadService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private Video mVideo;
    private String mToken = "";
    private String mVideoLocalPath = "";
    private String mRemoteVideoId = "";

    public UploadIntentService() {
        super("uploadService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        if (receiver == null) {
            receiver = new ResultReceiver(new Handler());
        }

        String gson = intent.getStringExtra("video");
        mVideo = (new Gson()).fromJson(gson, Video.class);
        mVideoLocalPath = intent.getStringExtra("url");

        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(mVideoLocalPath)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                boolean isSuccess = createVideo();

                /* Sending result back to activity */
                if (isSuccess) {
                    mVideo.setRemoteExist(true);
                    mVideo.setRemoteVideoUri("http://www.dailymotion.com/video/" + mRemoteVideoId);

                    FirebaseDB.getInstance().saveNewVideo(mVideo, SharePreference.getFirebaseId(this));

                    bundle.putBoolean("result", isSuccess);
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String getToken() {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", getString(R.string.dailymotion_account))
                .add("password", getString(R.string.dailymotion_pwd))
                .add("client_id", getString(R.string.dailymotion_api_id))
                .add("client_secret", getString(R.string.dailymotion_api_secret))
                .add("scope", "manage_videos")
                .build();
         try {
            Response res = API.post("https://api.dailymotion.com/oauth/token", formBody);
            String jsonData = res.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            mToken = jsonObject.getString("access_token");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return mToken;
    }

    private String getUploadUrl(){

        ArrayList<Pair> headers = new ArrayList<>();
        Pair header = new Pair("Authorization", "Bearer " + getToken());
        headers.add(header);

        String url = "";
        try {
            Response res = API.get("https://api.dailymotion.com/file/upload", headers);
            String jsonData = res.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            url = jsonObject.getString("upload_url");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String getSecondUrl(String filePath) {
        String firstUrl = getUploadUrl();

        RequestBody video = RequestBody.create(MediaType.parse("video/*"), new File(filePath));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filePath, video)
                .build();

        String secondUrl = "";
        try {
            Response res = API.post(firstUrl, requestBody);
            String jsonData = res.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            secondUrl = jsonObject.getString("url");
        } catch (IOException | JSONException e) {
          e.printStackTrace();
        }

        return secondUrl;
    }

    private boolean createVideo() {
        String url = getSecondUrl(mVideoLocalPath);

        ArrayList<Pair> headers = new ArrayList<>();
        Pair header = new Pair("Authorization", "Bearer " + mToken);
        headers.add(header);

        RequestBody formBody = new FormBody.Builder()
                .add("url", url)
                .build();
        boolean isSuccess = false;
         try {
             Response res = API.post("https://api.dailymotion.com/me/videos", headers, formBody);
             isSuccess = res.code() == 200;
             String jsonData = res.body().string();
             JSONObject jsonObject = new JSONObject(jsonData);
             mRemoteVideoId = jsonObject.getString("id");
         } catch (IOException | JSONException e) {
             e.printStackTrace();
         }
        return isSuccess;
    }
}
