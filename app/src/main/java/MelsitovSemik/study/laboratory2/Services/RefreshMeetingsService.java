package MelsitovSemik.study.laboratory2.Services;

import android.app.IntentService;
import android.content.Intent;

import MelsitovSemik.study.laboratory2.Global.Constants;

public class RefreshMeetingsService extends IntentService {

    public RefreshMeetingsService() {
        super("Обновление статей");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendBroadcast(new Intent(Constants.REFRESH_MEETINGS_BROADCAST_ACTION));
    }
}
