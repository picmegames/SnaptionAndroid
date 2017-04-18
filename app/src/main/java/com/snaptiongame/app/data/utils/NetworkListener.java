package com.snaptiongame.app.data.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.provider.Settings;

/**
 * Created by nickromero on 4/18/17.
 */

public class NetworkListener extends BroadcastReceiver {
    private ConnectivityManager connectivityManager;
    private Context mContext;

    public NetworkListener() {};

    public NetworkListener(Context context) {
        mContext = context;
        connectivityManager = (ConnectivityManager) mContext.getSystemService( Context.CONNECTIVITY_SERVICE );
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public boolean isConnectedToWifi() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public boolean isConnectedToMobile() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean isAirplaneModeOn() {
        return Settings.System.getInt(mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    public boolean isConnectedToInternet() {
        if (isAirplaneModeOn())
            return isConnectedToWifi();
        else
            return isConnectedToMobile() || isConnectedToWifi();
    }
}
