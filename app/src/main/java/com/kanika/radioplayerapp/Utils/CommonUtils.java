package com.kanika.radioplayerapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {
    //check internet connectivity and get connectivity status
    public static boolean isInternetConnected(Context mContext) {

        try {
            ConnectivityManager connect = null;
            if (mContext == null)
                return true;
            connect = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connect != null) {
                NetworkInfo resultMobile = connect
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                NetworkInfo resultWifi = connect
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                return (resultMobile != null && resultMobile
                        .isConnectedOrConnecting())
                        || (resultWifi != null && resultWifi
                        .isConnectedOrConnecting());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
