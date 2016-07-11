package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.RunnableFuture;

import Helper.Api;
import Helper.BusProvider;
import Model.User;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Connected extends AppCompatActivity {
    private  TextView name;
    private  TextView time;
    private  TextView mbs;
    private  TextView connect;
    private  TextView dis;
    private int connected,result;
    private  String token;
    private  String timeend;
    final Context con=this;
    String status="connected";
    int x = 0,d=0;
    int bmb;
    int amb;
    String connection_id;

    RatingBar rate;
    private CountDownTimer timer;
    ImageView strength, full;
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
        setContentView(R.layout.activity_connected);

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










        name= (TextView) findViewById(R.id.name);
        mbs= (TextView) findViewById(R.id.mbs);
        dis= (TextView) findViewById(R.id.dis);
        connect= (TextView) findViewById(R.id.connect);
        connect.setText("Connected");
        dis.setText("Disconnect");
        time= (TextView) findViewById(R.id.time);
        RelativeLayout disconnect=(RelativeLayout)findViewById(R.id.disconnect);

        strength= (ImageView) findViewById(R.id.strength);
        full= (ImageView) findViewById(R.id.full);
        final OkHttpClient client = new OkHttpClient();



        full.setImageResource(R.drawable.status);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final int numberOfLevels = 5;

        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();

        String withoutQuotes_line1 = ssid.replace('"', ' ');
        name.setText(withoutQuotes_line1 );
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);


        if(level==4){
            strength.setImageResource(R.drawable.sfourth);
        }
        else if(level==3){
            strength.setImageResource(R.drawable.sthird);
        }
        else if(level==2){
            strength.setImageResource(R.drawable.sthird);
        }
        else if(level==1){
            strength.setImageResource(R.drawable.sthird);
        }
        else{
            strength.setImageResource(R.drawable.sthird);
        }

        bmb=(int) (TrafficStats.getTotalRxBytes())/1048576;
        assert disconnect != null;
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status.equals("connected")) {

                    timer.onFinish();


                    Log.d("SHAN", "button click");
                    Realm realm = Realm.getDefaultInstance();
                    final User user = realm.where(User.class).findFirst();
                    token = user.getToken();
                    Log.d("SHAN", "token" + "get");
                    realm.close();

                    Calendar now = Calendar.getInstance();
                    String endTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
                    Log.d("SHAN", endTime);
                    String id = getIntent().getStringExtra("id").toString();
                    Log.d("SHAN", "Staerrt" + getIntent().getStringExtra("startTime"));
                    Log.d("SHAN", "token" + token + " id" + id);


                    client.newCall(Api.userConnectionState(
                            id,
                            "44.34",

                            "23.26",
                            getIntent().getStringExtra("startTime"),
                            endTime,
                            token

                    )).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //  Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            //  Log.d("Shan","In respnose"+response);

                            if (response.isSuccessful()) {

                                try {
                                    String obj = response.body().string();
                                    try {
                                        JSONObject Jobject = new JSONObject(obj);
                                        connection_id = Jobject.get("id").toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("Shan", "In respnose dfdsfsdfsdfdsfds" + obj);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //  Toast.makeText(getApplicationContext(), "Sucessfully server resopnse", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }


                        }
                    });


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
                    LayoutInflater layoutInflater = LayoutInflater.from(con);

                    View promptView = layoutInflater.inflate(R.layout.prompts, null);
                    alertDialogBuilder.setView(promptView);
                    final OkHttpClient client = new OkHttpClient();


                    rate = (RatingBar) promptView.findViewById(R.id.ratingBar);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    ;


                                    client.newCall(Api.rating(
                                            connection_id,
                                            String.valueOf(rate.getRating()),
                                            token


                                    )).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }

                                        @Override
                                        public void onResponse(Call call, final Response response) {
                                            Log.d("Shan", "In respnose" + response);

                                            if (response.isSuccessful()) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                                                        Log.d("SHAN",""+getIntent().getIntExtra("NetworkId", 0));
                                                        wifi.removeNetwork(getIntent().getIntExtra("NetworkId", 0));
                                                        wifi.saveConfiguration();


                                                        status="disconnected";
                                                        full.setImageResource(R.drawable.status2);
                                                        strength.setImageResource(R.drawable.sfisrt);
                                                        String tin = timeend;
                                                        connect.setText("Disconnected");
                                                        time.setText(tin);
                                                        dis.setText("Go back");
                                                        //int mobileRx = (int) TrafficStats.getMobileRxBytes();
                                                      /*  int wifiRx = (int) (TrafficStats.getTotalRxBytes() - mobileRx);
                                                        int dis = wifiRx / 1048576;*/
                                                        int y=x;
                                                        if (y > 0)
                                                            mbs.setText(" " + y + " Mb");
                                                        else
                                                            mbs.setText(" " + y * -1 + " Mb");


                                                        // Intent i = new Intent(Connected.this,Disconnected.class);



                                                 /*   i.putExtra("Name",getIntent().getStringExtra("Name"));
                                                    i.putExtra("Endtime",timeend);
                                                    // i.putExtra("connected",getIntent().getIntExtra("connected",0));
                                                    i.putExtra("id",getIntent().getStringExtra("id"));
                                                    i.putExtra("token",token);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);*/
                                                        Log.d("Shan", "In respnose" + response);
                                                    }
                                                });


                                            } else {

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.d("Shan", "In respnose" + response);
                                                        Toast.makeText(getApplicationContext(), "Uns_sucessful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }


                                        }
                                    });


                                    //  Toast.makeText(getApplicationContext(),"Good Buy",Toast.LENGTH_SHORT).show();
                             /*   Intent i = new Intent(Connected.this,Disconnected.class);
                                startActivity(i);*/

                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertD = alertDialogBuilder.create();

                    alertD.show();


/*
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifi.removeNetwork(getIntent().getIntExtra("NetworkId",0));
                wifi.saveConfiguration();

                Intent i = new Intent(Connected.this,Disconnected.class);



                i.putExtra("Name",getIntent().getStringExtra("Name"));
                i.putExtra("Endtime",timeend);
                // i.putExtra("connected",getIntent().getIntExtra("connected",0));
                i.putExtra("id",getIntent().getStringExtra("id"));
                i.putExtra("token",token);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);*/
                }
                if(status.equals("disconnected")){
                    Intent i = new Intent(Connected.this,ConnectToWifi.class);


                    startActivity(i);
                    finish();
                }
            }
        });




        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);



                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();



        name.setText(getIntent().getStringExtra("Name"));
        connected=getIntent().getIntExtra("connected",0);
    /*    int wifiRx = (int) (TrafficStats.getTotalRxBytes())/1048576;
        if(wifiRx>0)
            mbs.setText(""+wifiRx+" Mb");
        else
            mbs.setText(""+wifiRx*-1+" Mb");
*/

        //result=connected/1048576;




        timer= new CountDownTimer(9*10000*10000,1000){
            int i=0;
            int mins=0;
            int secs=0;
            @Override
            public void onFinish() {
                //Do your task.......

                timeend=i+":"+mins+":"+secs;
                Log.d("SHAN","TIME "+i+" "+mins+" "+secs);

            }


            @Override
            public void onTick(long millisUntilFinished) {
                //update your timer value here and show it......
                if(status.equals("connected")) {
                    if (secs < 10) {
                        time.setText("0" + (mins) + ":0" + (secs++));
                        Log.d("SHAN", "TIMEE " + i + " " + mins + " " + secs);
                    } else {
                        time.setText("0" + (mins) + ":" + (secs++));
                        Log.d("SHAN", "TIMEEE " + i + " " + mins + " " + secs);
                    }


                    if (secs / 60 == 1) {
                        mins++;
                        secs = 0;
                    }
                }
            }



        }.start();


    }

    private void updateTextView() {

if(status.equals("connected")) {

    if (x == 0)
        mbs.setText("" + x + " Mb");

    amb = (int) (TrafficStats.getTotalRxBytes()) / 1048576;


    d = amb - bmb;
    x = x + d;
    Log.d("SHAN", x + "   X");
    bmb = amb;
    Log.d("SHAN", bmb + "     after");
    mbs.setText("" + x + " Mb");


}


    }


    @Override
    public void onBackPressed() {

// dont call **super**, if u want disable back button in current screen.
    }

}
