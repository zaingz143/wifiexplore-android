package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private TextView name;
    private List<Contacts> contactsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    ImageView next;

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
        setContentView(R.layout.activity_history);
        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);

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




        name= (TextView) findViewById(R.id.name);

       // name.setText(getIntent().getStringExtra("Name")+"  History");
        next=(ImageView)findViewById(R.id.key75);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ContactAdapter(contactsList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareContactData();
    }
    private void prepareContactData() {
        Contacts con = new Contacts("Azman","$ 24");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 26");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 26");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 26");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 25");
        contactsList.add(con);
        con = new Contacts("Azman","$ 26");
        contactsList.add(con);




        mAdapter.notifyDataSetChanged();
    }
}
