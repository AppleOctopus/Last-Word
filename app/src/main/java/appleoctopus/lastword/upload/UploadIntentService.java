package appleoctopus.lastword.upload;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import appleoctopus.lastword.R;
import appleoctopus.lastword.http.API;
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
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                String results = uploadFile(url);

                /* Sending result back to activity */
                if (null != results && !results.isEmpty()) {
                    bundle.putString("result", results);
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

        String token = "";
        try {
            Response res = API.post("https://api.dailymotion.com/oauth/token", formBody);
            String jsonData = res.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            token = jsonObject.getString("access_token");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return token;
    }

    private String getUploadUrl(){
        String token = getToken();

        ArrayList<Pair> headers = new ArrayList<>();
        Pair header = new Pair("Authorization", "Bearer " + token);
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

    private String uploadFile(String filePath) {
        String url = getUploadUrl();

        RequestBody video = RequestBody.create(MediaType.parse("video/*"), new File(filePath));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filePath, video)
                .build();

        String jsonData = "";
        try {
            Response res = API.post(url, requestBody);
            jsonData = res.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;

    }
}
