package com.example.dell.wifiexplorer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.WalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentState;

import java.io.IOException;

import Helper.Api;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class Disconnected extends AppCompatActivity {
    private TextView name;
    private TextView endtime,totalUsage;
    // private RatingView mRatingView;
    final Context con=this;


    RatingBar rate;
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
        setContentView(R.layout.activity_disconnected);






        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);
/*
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);*/




        name= (TextView) findViewById(R.id.name);
        totalUsage= (TextView) findViewById(R.id.totalUsage);
        int mobileRx = (int) TrafficStats.getMobileRxBytes();
        int wifiRx = (int) (TrafficStats.getTotalRxBytes() - mobileRx);
        int dis=wifiRx/1048576;
        if(dis>0)
            totalUsage.setText(" "+dis+" Mb");
        else
            totalUsage.setText(" "+dis*-1+" Mb");

        endtime= (TextView) findViewById(R.id.endtime);

        name.setText(getIntent().getStringExtra("Name"));
        endtime.setText(getIntent().getStringExtra("Endtime"));
        RelativeLayout login=(RelativeLayout)findViewById(R.id.rate);










//use ratings within event listner code block

//        float rating = rate.getRating();




 /*       assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
                LayoutInflater layoutInflater = LayoutInflater.from(con);

                View promptView = layoutInflater.inflate(R.layout.prompts, null);
                alertDialogBuilder.setView(promptView);
                final OkHttpClient client = new OkHttpClient();


             //   rate= (RatingBar)promptView.findViewById(R.id.ratingBar);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {





                                ;


                                client.newCall(Api.rating(
                                        getIntent().getStringExtra("id"),
                                        String.valueOf(rate.getRating()),
                                        getIntent().getStringExtra("token")


                                )).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {



                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                //     Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }

                                    @Override
                                    public void onResponse(Call call, final Response response) {
                                        Log.d("Shan","In respnose"+response);

                                        if (response.isSuccessful()) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Toast.makeText(getApplicationContext(), "Sucessfully server resopnse", Toast.LENGTH_SHORT).show();
                                                    Log.d("Shan","In respnose"+response);
                                                }
                                            });


                                        } else {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("Shan","In respnose"+response);
                                                    // Toast.makeText(getApplicationContext(), "Uns_sucessful", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }


                                    }
                                });






                                //  Toast.makeText(getApplicationContext(),"Good Buy",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Disconnected.this,ConnectToWifi.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,    int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();

            }

        });*/

    }

}
