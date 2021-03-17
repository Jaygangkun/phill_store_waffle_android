package com.edelivery.store.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.edelivery.store.BaseActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkHelper {

    private static NetworkHelper networkHelper = new NetworkHelper();
    private NetworkRequest networkRequest;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;

    private BaseActivity.NetworkListener networkAvailableListener;

    private NetworkHelper() {
        networkRequest = new NetworkRequest
                .Builder().build();
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                if (networkAvailableListener != null) {
                    networkAvailableListener.onNetworkChange(true);
                }

            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
               /* if (networkAvailableListener != null) {
                    networkAvailableListener.onNetworkChange(false);
                }*/

            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                if (networkAvailableListener != null) {
                    networkAvailableListener.onNetworkChange(false);
                }
            }
        };
    }

    public static NetworkHelper getInstance() {
        return networkHelper;
    }

    public void initConnectivityManager(Context context) {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager)
                    context.getSystemService
                            (Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
            }
        }

    }

    public void setNetworkAvailableListener(BaseActivity.NetworkListener
                                                    networkAvailableListener) {
        this.networkAvailableListener = networkAvailableListener;
    }


}
