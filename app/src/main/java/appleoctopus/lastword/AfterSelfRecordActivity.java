package appleoctopus.lastword;

import android.os.Bundle;

public class AfterSelfRecordActivity extends BaseDynamicViewActivity {
    private static final String TAG = AfterSelfRecordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String t = "每個人生都會有措手不及"
                + System.getProperty ("line.separator")
                + "但那些，讓我們更懂得珍惜";

        setText(t);

        setButtonIntentDestination(new CatogoryActivity(), null);
    }

}
