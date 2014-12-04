package edu.project.cmpe277.musicalheart;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by SwethaAmbati on 11/30/14.
 */
public class NotificationService extends Service{


    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings({ "static-access"})
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        String val = "Start Playing";
        NotificationManager notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this.getApplicationContext(),DeviceScanActivity.class);
        notificationIntent.putExtra("message", val);
        notificationIntent.putExtra("reminder", true);

        Notification notification = new Notification(R.drawable.ic_launcher,"Reminder", System.currentTimeMillis());
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "Reminder",val, pendingNotificationIntent);

        notificationManager.notify(0, notification);
    }



    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
