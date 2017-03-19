package appleoctopus.lastword;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class IntroActivity extends BaseDynamicViewActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String t = "每個人的一生，都是最美的煙火"
                + System.getProperty ("line.separator")
                +"稍縱，即逝";

        setText(t);

        setButtonIntentDestination(new BeforeSelfRecordActivity(), null);
    }
}
