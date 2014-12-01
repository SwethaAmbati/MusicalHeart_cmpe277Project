package edu.project.cmpe277.musicalheart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by SwethaAmbati on 11/30/14.
 */
public class AlarmService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String val = intent.getStringExtra("message");
        Intent service1 = new Intent(context, NotificationService.class);
        service1.putExtra("message", val);
        context.startService(service1);
    }
}
