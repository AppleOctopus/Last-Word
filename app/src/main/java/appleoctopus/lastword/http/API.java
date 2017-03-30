package appleoctopus.lastword.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by allenwang on 2017/3/27.
 */

public class API {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType X_WWW_FORM
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = HttpClientProvider.getInstance().newCall(request).execute();
        return response.body().string();
    }

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = HttpClientProvider.getInstance().newCall(request).execute();
        return response.body().string();
    }
}
