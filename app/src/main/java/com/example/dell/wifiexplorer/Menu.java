package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Menu extends AppCompatActivity {
      private ImageView back, profile,mydevices;

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
        setContentView(R.layout.activity_menu);


        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);

    /*    IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);*/
















        back= (ImageView) findViewById(R.id.back);
        profile= (ImageView) findViewById(R.id.profile);
        mydevices= (ImageView) findViewById(R.id.mydevices);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
            }
        });

        mydevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ConnectionInfo.class);
                startActivity(intent);
            }
        });


    }
}
