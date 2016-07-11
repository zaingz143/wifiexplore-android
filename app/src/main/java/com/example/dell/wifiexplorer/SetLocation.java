package com.example.dell.wifiexplorer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Helper.ConnectionHelper;

public class SetLocation extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView menu,search,addwifi;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private TextView markerText;
    private EditText name, pass, ssid, sn, modelname, security;
    private Button save_btn;
    private LinearLayout markerLayout;
    private LatLng center;
    private List<Address> addresses;
    double x, y;
    String address;
    double stringLatitude;
    double stringLongitude;
    private RelativeLayout ok;

    private static final int REQUEST_CODE_LOCATION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);






     /*   IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);*/





        menu =(ImageView)findViewById(R.id.mymenu);
        search =(ImageView)findViewById(R.id.search);
        addwifi =(ImageView)findViewById(R.id.addwifi);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetLocation.this,Menu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetLocation.this,Search.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        addwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetLocation.this,AddWifi.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        ok= (RelativeLayout) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(),AddWifi.class);
                intent.putExtra("Lati",x);
                intent.putExtra("Longi",y);
                intent.putExtra("address",address);
                intent.putExtra("Lender",getIntent().getStringExtra("Lender"));
                intent.putExtra("Ssid",getIntent().getStringExtra("Ssid"));
                intent.putExtra("Sec",getIntent().getStringExtra("Sec"));
                startActivity(intent);
                finish();
            }
        });
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        final ConnectionHelper connectionHelper = new ConnectionHelper(getApplicationContext());



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






            if (connectionHelper.isGPSEnabled()) {



            WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            if (true){

                if(wifi.isWifiEnabled())
                    Toast.makeText(getApplicationContext(),"Trun on wifi for better experience",Toast.LENGTH_SHORT).show();

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
//wifi is enabled
            }else
            {
                Toast.makeText(getApplicationContext(),"Connect to internet",Toast.LENGTH_SHORT).show();
            }


            /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this)*/;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is off");  // GPS not found
            builder.setMessage("Turn on location services"); // Want to enable?
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {

                    SetLocation.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }


        ;

    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        GPSTracker gpsTracker = new GPSTracker(this);
        stringLatitude = gpsTracker.getLatitude();
        stringLongitude = gpsTracker.getLongitude();
        // Add a marker in Sydney and move the camera
        LatLng currentPosition = new LatLng(stringLatitude, stringLongitude);
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                currentPosition).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);


        mMap.clear();

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub
                center = mMap.getCameraPosition().target;
                markerText = (TextView) findViewById(R.id.locationMarkertext);
                markerText.setText(" Set your Location ");
                mMap.clear();
                markerLayout.setVisibility(View.VISIBLE);

                try {
                   GetLocationAsync task= new GetLocationAsync(center.latitude, center.longitude);
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else

                            task.execute();

                } catch (Exception e) {
                }
            }
        });

        markerLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LatLng latLng1=null;
                try {

                    latLng1 = new LatLng(center.latitude,
                            center.longitude);

                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(latLng1)
                            .title(" Set your Location ")
                            .snippet("")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.marker)));
                    m.setDraggable(true);

                    markerLayout.setVisibility(View.GONE);
                } catch (Exception e) {
                }
            /*    Toast.makeText(getApplicationContext(), "Lat Long is :"+latLng1,
                        Toast.LENGTH_SHORT).show();*/
            }
        });
    }
    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;

        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
            Log.i("Longi","  "+x+"  "+y);
        }

        @Override
        protected void onPreExecute() {
            //Address
            markerText.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(SetLocation.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (Geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");
                    String naam=addresses.get(0).getAddressLine(0)
                            + addresses.get(0).getAddressLine(1) + " ";
                    address=addresses.get(0).getAddressLine(0)
                            + addresses.get(0).getAddressLine(1);
                    Log.d("shan",naam+address);
                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {

                markerText.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");
                address=addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 2:
                finish();
                startActivity(getIntent());


                break;

        }

    }
}
