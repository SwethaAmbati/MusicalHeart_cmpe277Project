package edu.project.cmpe277.musicalheart;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class WifiReceiver extends BroadcastReceiver {

    private String wifiName = "";
    private final String TAG = "WifiReceiver";


        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();
             String intentString="" ;//= intent.getStringExtra("wifi");


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            intentString = preferences.getString("wifi", "");
            Log.v(TAG, "action: " + action + " String: " +intentString );

            if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION) ||
                    action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION ))
            {
                if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)||
                        intent.getBooleanExtra(WifiManager.WIFI_STATE_CHANGED_ACTION, true))
                {
                    // wifi is enabled
                    Log.v(TAG, " wifi enabled / disabled" );

                    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected()) {

                        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                        if (manager.isWifiEnabled()) {
                            // get the wifi information
                            WifiInfo wifiInfo = manager.getConnectionInfo();
                            Log.v(TAG, "wifi enabled... ");

                            if (wifiInfo != null) {
                                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                                    /// get the name of the wifi
                                    wifiName =  wifiInfo.getSSID();

                                    Log.v(TAG, "Device is connected to wifi: "+wifiName );

                                    //Check if home network is same as wifi being connected
                                    if (wifiName.contentEquals("\"" + intentString + "\"" ))
                                    {
                                        /******Bring the Activity to foreground if it is in background*****/
                                        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                                                .getSystemService(Activity.ACTIVITY_SERVICE);
                                        String className = activityManager.getRunningTasks(1).get(0).topActivity
                                                .getClassName();

                                        if (!(className.equals("edu.project.cmpe277.musicalheart.DeviceScanActivity"))) {
                                            Log.v(TAG, "App is in backgound ");

                                            Intent i = new Intent(context, DeviceScanActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.getApplicationContext().startActivity(i);
                                        } else {
                                            Log.v(TAG, "App is in foreground ");
                                        }
                                    }

                                }
                            }
                        }
                        Toast.makeText(context, R.string.wifitoast_message + wifiName, Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    // wifi is disabled
                    Log.v(TAG, "Wifi disabled ");
                }
            }
        }
    }



