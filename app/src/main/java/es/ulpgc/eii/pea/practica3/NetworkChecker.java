package es.ulpgc.eii.pea.practica3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import es.ulpgc.eii.pea.practica3.interfaces.NetworkChecksReceiver;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkChecker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (networkInfo(context) != null) ((NetworkChecksReceiver) context).networkRecovered();

    }

    private NetworkInfo networkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}
