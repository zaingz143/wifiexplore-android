package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.Marker;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import Helper.BusProvider;
import Helper.ConnectionHelper;
import Helper.Util;
import Model.User;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import java.util.logging.Handler;

public class ConnectToWifi extends FragmentActivity implements OnMapReadyCallback  {


    private GoogleMap mMap;
    ImageView menu, search, addwifi;
    Button btn;
    private List<JSONObject> wifis = new ArrayList<>();
    String state="ok";
    JSONArray a = new JSONArray();
    JSONObject o = null;
    final Context context = this;
    private EditText editTextMainScreen;
    String cellNo;
    double stringLatitude;
    double stringLongitude;
    private String key, key2;
    CircularProgressView progressView;
    int count = 0, ratingCount;
    private TextView name;
    String ssid;
    LocationManager mLocationManager;
    private WifiManager mng;
    String id, pass, actualKey, actualSsid;
    Realm realm;
    private SlidingUpPanelLayout mLayout;
    RelativeLayout connected;
    List<ScanResult> mScanResults;
    private static final int REQUEST_CODE_LOCATION = 2;
BroadcastReceiver broadcastReceiver;

    private ConnectionHelper connectionHelper;
    public class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.dell.wifiexplorer.ACTION_LOGOUT")) {
                finish();
            }
            else if(intent.getAction().equals("com.example.dell.wifiexplorer.ACTION_DISCONNECTED")){
                Toast.makeText(getApplicationContext(), "DISCONNECTED", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private LogoutReceiver logoutReceiver;

    @Override
    protected void onDestroy() {
        // Unregister the logout receiver
        unregisterReceiver(logoutReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_wifi);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request missing location permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);

        } else {
            // Location permission has been granted, continue as usual.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }





        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);


        mng = (WifiManager) getSystemService(Context.WIFI_SERVICE);
   /*     registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();*/


  /*      IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);
*/

        final ConnectionHelper connectionHelper = new ConnectionHelper(getApplicationContext());




        connected= (RelativeLayout) findViewById(R.id.connected);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        name= (TextView) findViewById(R.id.name);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("Sliding", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("Sliding", "onPanelStateChanged " + newState);
            }
        });

        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            //Toast.makeText(getApplicationContext(),"Hide state",Toast.LENGTH_SHORT).show();
        }



        final OkHttpClient client = new OkHttpClient();
        menu =(ImageView)findViewById(R.id.mymenu);
        search =(ImageView)findViewById(R.id.search);
        addwifi =(ImageView)findViewById(R.id.addwifi);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConnectToWifi.this,Menu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConnectToWifi.this,Search.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        addwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConnectToWifi.this,AddWifi.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });



        if (connectionHelper.isGPSEnabled()) {






            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is off");  // GPS not found
            builder.setMessage("Turn on location services"); // Want to enable?
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {

                    ConnectToWifi.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



    }
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;



        GPSTracker gpsTracker = new GPSTracker(this);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double lat = gpsTracker.getLatitude();
        double longi = gpsTracker.getLongitude();
        realm = Realm.getDefaultInstance();

        final User user = realm.where(User.class).findFirst();


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Util.NEAR_BY_WIFI+"/?lat="+lat+"&long="+longi)

                .addHeader("Authorization", "Token token="+user.getToken())
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    String obj=response.body().string();
                    Log.d("SHAN",obj);





                    o = new JSONObject(obj);
                    try {
                        a = o.getJSONArray("wifis");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("ali","Object"+o);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

             /*   WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                if (wifi.isWifiEnabled()) {
                    *//*try {
                        a = o.getJSONArray("wifis");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*//*
                }
                else{
                   // Toast.makeText(getApplicationContext(),"Connect to wifi",Toast.LENGTH_SHORT).show();
                }*/

                final int arrSize = a.length();
                Log.d("ali","size"+arrSize);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < arrSize; ++i) {
                            Log.d("ali","in loop"+i);
                            try {
                                o = a.getJSONObject(i);
                                wifis.add(o);
                                o.get("name");
                                LatLng wifirealm = null;
                                try {
                                    wifirealm = new LatLng(o.getDouble("lat"), o.getDouble("long"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Marker marker=mMap.addMarker(new MarkerOptions()
                                            .position(wifirealm)
                                            .title(o.getString("name"))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }//end loop
                    }
                });
            }


        });




    /*    for( int i=0; i <wifis.size(); i++ )
        {


     *//*       LatLng wifirealm = new LatLng(wifis.get(i).getLatitude(), wifis.get(i).getLongitude());
            Marker marker=mMap.addMarker(new MarkerOptions()
                    .position(wifirealm)
                    .title(wifis.get(i).getLenderName())
                    // .snippet(wifis.get(i).getKey())

                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
*//*

        }
*/


        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {




                name.setText(marker.getTitle());

                if (mLayout != null) {

                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

                    }





                    //connectionHelper=new ConnectionHelper(getApplicationContext());

                    //   mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    //name.setText(marker.getTitle());

                    connected.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            mScanResults = mng.getScanResults();
                            Log.d("SIZE","SHAN"+mScanResults.size());
                            //   BusProvider.getInstance().post(new Apple(mScanResults));



                       /*     for (ScanResult result : mScanResults)



                            {
                                        if (marker.getTitle().equals(result.SSID.toString()))
                                            Log.d("SIZE","match   "+ result.SSID.toString());
                                        else{
                                            Log.d("SIZE"," not match   "+ result.SSID.toString());
                                        }

                            }*/
                          /*  for(int l=0 ; l<mScanResults.size(); l++)
                            {
                                     "+mScanResults.get(l).SSID.toString()

                            }*/



                            for (int i=0; i<wifis.size();i++){

                                String listMarker= String.valueOf(marker.getPosition().latitude);
                                String listLongi=String.valueOf(marker.getPosition().longitude);

                                try {
                                    if((listLongi.equals(wifis.get(i).get("long").toString()))&&listMarker.equals(wifis.get(i).get("lat").toString())){
                                        id=wifis.get(i).get("id").toString();
                                        ssid=wifis.get(i).get("ssid").toString();
                                        pass=wifis.get(i).get("password").toString();
                                        Log.d("SIZE","passs    "+pass);

                                        for (ScanResult result : mScanResults)
                                        {
                                            if (ssid.equals(result.SSID.toString())) {



                                                actualSsid=ssid;
                                                actualKey=pass;
                                                state="fine";
                                                Log.d("SIZE", "match   " + result.SSID.toString());
                                                break;


                                            }
                                            else{
                                                // Toast.makeText(getApplicationContext(),"Can't connect",Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        break;

                                    }
                                    else{
                                        Log.d("SHAN ","NOT"+wifis.get(i).get("long")+wifis.get(i).get("lat")+"  "+marker.getPosition().longitude+"  "+ marker.getPosition().latitude);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }


                            if(state.equals("fine")){
                                Calendar now = Calendar.getInstance();
                                String startTime=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);


                                WifiConfiguration wifiConfig = new WifiConfiguration();
                                wifiConfig.SSID = String.format("\"%s\"", actualSsid);
                                wifiConfig.preSharedKey = String.format("\"%s\"", actualKey);

                                WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);



//remember id

                                int netId = wifiManager.addNetwork(wifiConfig);
                                WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);


                                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);




/*

                                BroadcastReceiver broadcastReceiver = new ConnectionChangeReceiver();
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
                                context.registerReceiver(broadcastReceiver, intentFilter);*/




                                if (wifi.isWifiEnabled()){

                                        wifiManager.enableNetwork(netId, true);


//wifi is enabled
                                }
                                else if(!wifi.isWifiEnabled()) {
                                    wifiManager.setWifiEnabled(true);

                                    wifiManager.enableNetwork(netId, true);
                                }
                                else if(mWifi.isConnected())
                                {
                                    wifiManager.disconnect();
                                    wifiManager.setWifiEnabled(true);
                                    wifiManager.enableNetwork(netId, true);
                                }

                                // wifiManager.reconnect();



                                    Intent intennt = new Intent();
                                    intennt.setAction("com.example.Broadcast");
                                    intennt.putExtra("name", actualSsid);
                                    intennt.putExtra("id", actualKey);
                                    sendBroadcast(intennt);

                                   // BusProvider.getInstance().post("on");


                                int mobileTx = (int) TrafficStats.getMobileTxBytes();
                                int mobileRx = (int) TrafficStats.getMobileRxBytes();
                                int wifiTx = (int) (TrafficStats.getTotalTxBytes() - mobileTx);
                                int wifiRx = (int) (TrafficStats.getTotalRxBytes() - mobileRx);

                                Log.d("DDD"," mobile tx   "+mobileTx);
                                Log.d("DDD"," mobile rx    "+mobileRx);
                                Log.d("DDD"," wifi tx     "+wifiTx);
                                Log.d("DDD"," wifi rx      "+wifiRx);
                                Log.d("DDD"," Over all tx      "+TrafficStats.getTotalTxBytes());
                                Log.d("DDD"," Over all rx      "+TrafficStats.getTotalRxBytes());

                                Log.d("SHAN",startTime);
                                Intent intent = new Intent(getApplicationContext(), Connected.class);
                                intent.putExtra("connected",wifiRx/1048576*-1);
                                intent.putExtra("Name",marker.getTitle());
                                intent.putExtra("startTime",startTime);
                                intent.putExtra("id",id);
                                intent.putExtra("NetworkId",netId);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);}
                            else{
                                Toast.makeText(getApplicationContext(),"Can't connect",Toast.LENGTH_SHORT).show();

                            }





                      /*      Calendar now = Calendar.getInstance();
                            String startTime=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

                            Log.d("SHAN",startTime);
                            Intent intent = new Intent(getApplicationContext(), Connected.class);
                            intent.putExtra("connected",TrafficStats.getTotalRxBytes());
                            intent.putExtra("Name",marker.getTitle());
                            intent.putExtra("startTime",startTime);
                            intent.putExtra("id",id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/


                        }
                    });


                }


            }



        });


    }

/*
    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }
    @Subscribe
    public void onMessageEvent(Apple mScanResults) {


        adapter.swap(mScanResults.list,getApplicationContext());


    }/*
*//*
public void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
}

    @Override
    public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }
    @Subscribe
    public void onMessageEvent(int mScanResults) {





    }
*/

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 2:
                finish();
                startActivity(getIntent());


                break;

        }

    }


}
