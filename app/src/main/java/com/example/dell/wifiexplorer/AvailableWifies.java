/*
package com.example.dell.wifiexplorer;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.search.material.library.MaterialSearchView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import helper.Api;
import helper.BusProvider;
import helper.ConnectionHelper;
import io.realm.Realm;
import io.realm.RealmResults;
import model.Feedback;
import model.User;
import model.Wifi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AvailableWifies extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback {



    private GoogleMap mMap;
    Marker myMarker;
    private List<Wifi> wifis;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private MaterialSearchView searchView;
    private OkHttpClient client;
    private ConnectionHelper connectionHelper;
    private SlidingUpPanelLayout mLayout;
    //TextView name;
    Button connected;
   private String key,key2;
    CircularProgressView progressView;
  int count=0,ratingCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       progressView = (CircularProgressView) findViewById(R.id.progress_view);



        connected= (Button) findViewById(R.id.connected);

       // name= (TextView) findViewById(R.id.name);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
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
   */
/*     mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.);
            }
        });*//*

        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            //Toast.makeText(getApplicationContext(),"Hide state",Toast.LENGTH_SHORT).show();
        }
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        SearchAdapter adapter = new SearchAdapter();
        searchView.setAdapter(adapter);

        //code add by azman
        connectionHelper = new ConnectionHelper(getApplicationContext());


        if (connectionHelper.isGPSEnabled()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is off");  // GPS not found
            builder.setMessage("Turn on location services"); // Want to enable?
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }


        //code added by azman


        if (!isMyServiceRunning(WifiService.class))
            startService();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WiFi Explore");
        setSupportActionBar(toolbar);


        connectionHelper = new ConnectionHelper(getApplicationContext());



        client = new OkHttpClient();

      */
/*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddWifiActivity.class);
                startActivity(intent);
            }
        });*//*


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.logout:
                        signOut();
                        return true;
                    case R.id.history:
                       Intent intent= new Intent(getApplicationContext(),HistoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.payments:
                       Intent payment =new Intent(getApplicationContext(),PaymentActivity.class);
                        startActivity(payment);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.add_menu) {

            Intent intent = new Intent(getApplicationContext(), AddWifiActivity.class);
            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Realm realm = Realm.getDefaultInstance();
        User u = null;
        realm.beginTransaction();
        if (realm.where(User.class).count() > 0) {
            u = realm.where(User.class).findFirst();

        }

        client.newCall(Api.userSignOutRequest(
                u.getToken()
        )).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);

                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }

            }
        });
        realm.clear(User.class);
        realm.commitTransaction();
        realm.close();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (connectionHelper.isGPSEnabled()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is off");  // GPS not found
            builder.setMessage("Turn on location services"); // Want to enable?
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }
    }

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
    public void onMessageEvent(Integer event) {


//        if (event == 11) {
//
//            //contected
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.linear, new WifiConnectedFragment())
//                    .addToBackStack(null).commit();
//
//        } else if (event == 10) {
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.linear, new WifiOnFragment())
//                    .addToBackStack(null).commit();
//
//
//
//          */
/*  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//
//            Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.logo);
//            mBuilder.setLargeIcon(icon);
//            mBuilder.setContentTitle("Notification Alert, Click Me!");
//            mBuilder.setContentText("Hi, This is Android Notification Detail!");
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//            mNotificationManager.notify(52658555, mBuilder.build());*//*

//
//
//        } else {
//
//            //disconected
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.linear, new WifiNotConnectedFragment())
//                    .addToBackStack(null).commit();
//
//        }


    }


    private void startService() {
        startService(new Intent(getBaseContext(), WifiService.class));
    }

    // Method to stop the service
    private void stopService() {
        stopService(new Intent(getBaseContext(), WifiService.class));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        GPSTracker gpsTracker = new GPSTracker(this);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());


        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        wifis = realm.where(Wifi.class).findAll();
        realm.commitTransaction();


        for( int i=0; i <wifis.size(); i++ )
        {


            LatLng wifirealm = new LatLng(wifis.get(i).getLatitude(), wifis.get(i).getLongitude());
             Marker marker=mMap.addMarker(new MarkerOptions()
                    .position(wifirealm)
                    .title(wifis.get(i).getName())
                    // .snippet(wifis.get(i).getKey())

                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));


        }
        realm.close();


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


                Realm realm= Realm.getDefaultInstance();
                realm.beginTransaction();
                RealmResults<Wifi> wifi = realm.where(Wifi.class).findAll();
                for(Wifi object:    wifi ){


                    if(object.getLatitude()==marker.getPosition().latitude && object.getLongitude()==marker.getPosition().longitude ){
                        key2=object.getKey();
                        break;
                    }


                }


                RealmResults<Feedback> feedback = realm.where(Feedback.class).findAll();
                for(Feedback object:   feedback ){


                    if(object.getKey().equals(key2)){

                        Log.i("COMMENT",  object.getComments());
                        Log.i("COMMENT",  ""+object.getRating());

                       ratingCount=ratingCount+ object.getRating();
                        count++;
                    }


                }

                Log.i("COMMENT","   "+ratingCount+"     "+count);
               // int avg=ratingCount/count;


                realm.commitTransaction();
                realm.close();











                if (mLayout != null) {

                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.9f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

                    }



                     //   mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            //name.setText(marker.getTitle());

                    connected.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), WifiConnectedActivity.class);
                            intent.putExtra("dataConnected", TrafficStats.getTotalRxBytes());
                            intent.putExtra("Name",marker.getTitle());
                            intent.putExtra("firstTime",System.currentTimeMillis());
                            intent.putExtra("Lati",marker.getPosition().latitude);
                            intent.putExtra("Longi",marker.getPosition().longitude);
                            startActivity(intent);

                        }
                    });


                }
               */
/* final Intent intent = new Intent(MainActivity.this,WifiDetailActivity.class);

                intent.putExtra("Title", marker.getTitle());
                startActivity(intent);
*//*




            }



        });


    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapLoaded() {

    }

*/
/*    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapLoaded() {
        progressView.setVisibility(View.INVISIBLE);
    }*//*


    private class SearchAdapter extends BaseAdapter implements Filterable {

        private ArrayList<String> data;

        private String[] typeAheadData;

        LayoutInflater inflater;

        public SearchAdapter() {
            inflater = LayoutInflater.from(MainActivity.this);
            data = new ArrayList<String>();
            typeAheadData = getResources().getStringArray(R.array.state_array_full);
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (!TextUtils.isEmpty(constraint)) {
                        // Retrieve the autocomplete results.
                        List<String> searchData = new ArrayList<>();

                        for (String str : typeAheadData) {
                            if (str.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                searchData.add(str);
                            }
                        }

                        // Assign the data to the FilterResults
                        filterResults.values = searchData;
                        filterResults.count = searchData.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results.values != null) {
                        data = (ArrayList<String>) results.values;
                        notifyDataSetChanged();
                    }
                }
            };
            return filter;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            String currentListData = (String) getItem(position);

            mViewHolder.textView.setText(currentListData);

            return convertView;
        }


        private class MyViewHolder {
            TextView textView;

            public MyViewHolder(View convertView) {
                textView = (TextView) convertView.findViewById(android.R.id.text1);
            }
        }
    }

}


*/
