package MelsitovSemik.study.laboratory2.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import MelsitovSemik.study.laboratory2.Services.RefreshMeetingsService;

public class RefreshMeetingsBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startForegroundService(new Intent(context, RefreshMeetingsService.class));
        Log.d("==> ", "обновляю");
    }
}
