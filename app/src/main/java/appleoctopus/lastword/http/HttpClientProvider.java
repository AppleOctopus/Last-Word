package appleoctopus.lastword.http;

import okhttp3.OkHttpClient;

/**
 * Created by allenwang on 2017/3/23.
 */

public class HttpClientProvider extends OkHttpClient {

    private static HttpClientProvider instance;

    private HttpClientProvider(){}

    public static HttpClientProvider getInstance() {
        if (instance == null) {
            instance = new HttpClientProvider();
        }
        return instance;
    }
}
