package appleoctopus.lastword;

import android.os.Bundle;

import appleoctopus.lastword.util.SharePreference;

public class AfterSelfRecordActivity extends BaseDynamicViewActivity {
    private static final String TAG = AfterSelfRecordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String t = "";
        if (SharePreference.getIsFirstOpen(this)) {
            t = "嘿！不管剛剛錄的如何"
                    + System.getProperty("line.separator")
                    + "，都是你最可愛的一部份";
            SharePreference.setIsFirstOpen(AfterSelfRecordActivity.this, false);
        } else {
            t = "每個人生都會有措手不及"
                    + System.getProperty("line.separator")
                    + "但那些，讓我們更懂得珍惜";
        }
        setText(t);

        setButtonIntentDestination(new CatogoryActivity(), null);
    }

}
