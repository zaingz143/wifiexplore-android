
package Helper;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;



public class ConnectionHelper {


    private  Context con;
    private WifiManager mng;

    public ConnectionHelper(Context con) {
        this.con=con;
        mng = (WifiManager)con.getSystemService(Context.WIFI_SERVICE);


    }

    public boolean wifiConnected(){
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.getType() == ConnectivityManager.TYPE_WIFI;

    }
    public boolean isGPSEnabled()
    {
        LocationManager lm = (LocationManager)
                con.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public  boolean wifiOn()
    {
        return mng.isWifiEnabled();
    }


  /*  public void getWifiList(){


       final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    List<ScanResult> mScanResults = mng.getScanResults();
                    BusProvider.getInstance().post(new Apple(mScanResults));
                }
            }
        };




        con.registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mng.startScan();


    }*/



}


