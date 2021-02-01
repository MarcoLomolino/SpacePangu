package com.example.drawabletest.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import static android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED;

public class InternetConnection implements LifecycleObserver {

    private ConnectivityManager mConnectivityMgr;
    private Context mContext;
    private boolean mIsConnected = false;

    public InternetConnection(Context context) {
        mContext = context;
        mConnectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ((AppCompatActivity) mContext).getLifecycle().addObserver(this);
    }

    public boolean isOnline() {
        mIsConnected = false;
        Network[] allNetworks = mConnectivityMgr.getAllNetworks();
        for (Network network : allNetworks) {
            NetworkCapabilities networkCapabilities = mConnectivityMgr.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED))
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                        mIsConnected = true;
            }
        }
        return mIsConnected;
    }
}
