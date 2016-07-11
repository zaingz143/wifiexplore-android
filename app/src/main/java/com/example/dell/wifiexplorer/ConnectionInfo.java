package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Helper.Util;
import Model.User;
import Model.UserWifi;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectionInfo extends AppCompatActivity {


    ImageView menu,search,addwifi;
    Bundle bundle;
    UserAdpter horizontalAdapter;
    JSONArray a = null;
    JSONObject o = null;
    Realm realm;
    List<JSONObject> horizontalList;
    private RecyclerView horizontal_recycler_view;
    private TextView name;

    public class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.dell.wifiexplorer.ACTION_LOGOUT")) {
                finish();
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
        setContentView(R.layout.activity_connection_info);

        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);









        name= (TextView) findViewById(R.id.name);
        //  name.setText("WELCOME  "+getIntent().getStringExtra("Name"));



        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        horizontalList=new ArrayList<>();


        realm = Realm.getDefaultInstance();
        final User user = realm.where(User.class).findFirst();


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Util.WIFI_ADD_URL)

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
                    o = new JSONObject(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    a = o.getJSONArray("wifis");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final int arrSize = a.length();

                for (int i = 0; i < arrSize; ++i) {
                    try {
                        o = a.getJSONObject(i);
                        horizontalList.add(o);
                    } catch (JSONException e) {

                    }

                }//end loop*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        horizontalAdapter = new UserAdpter(horizontalList,getApplicationContext());
                        horizontal_recycler_view.setAdapter(horizontalAdapter);
                    }
                });

            }


        });


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(ConnectionInfo.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);



        menu =(ImageView)findViewById(R.id.mymenu);
        search =(ImageView)findViewById(R.id.search);
        addwifi =(ImageView)findViewById(R.id.addwifi);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConnectionInfo.this,Menu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConnectionInfo.this,Search.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        addwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConnectionInfo.this,AddWifi.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        Log.d("SHAN","In inofo"+ getIntent().getStringExtra("Name"));


    }



}
