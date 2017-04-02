package appleoctopus.lastword.http;

import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

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

    public static Response post(String url, RequestBody body) throws IOException {
         Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = HttpClientProvider.getInstance().newCall(request).execute();
        return response;
    }

    public static Response get(String url, ArrayList<Pair> headers) throws IOException {
        Request.Builder b = new Request.Builder();

        for (int i = 0; i < headers.size(); i++) {
            Pair<String, String> h = headers.get(i);
            b.addHeader(h.first, h.second);
        }

        Request request = b.url(url).build();

        Response response = HttpClientProvider.getInstance().newCall(request).execute();
        return response;
    }
}
