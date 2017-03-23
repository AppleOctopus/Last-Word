package appleoctopus.lastword;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by lin1000 on 2017/3/19.
 */

public class CustomApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Firebase.setAndroidContext(this);
        Fabric.with(this, new Crashlytics());
    }
}
