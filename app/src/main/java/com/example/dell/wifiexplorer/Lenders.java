package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

import Helper.Api;
import Helper.Util;
import Model.User;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Lenders extends AppCompatActivity {
    private TextView name;
    ImageView circle75,editwifi;
    ImageView del,his;
    Context con=this;
    JSONArray a = null;
    JSONObject o = null;
    String id;
    Realm realm;
    private List<JSONObject> wifis=new ArrayList<>();
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
        setContentView(R.layout.activity_lenders);


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
        }, intentFilter);*/














        final OkHttpClient client = new OkHttpClient();

        name= (TextView) findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

editwifi= (ImageView) findViewById(R.id.editwifi);
his= (ImageView) findViewById(R.id.his);

        editwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditWifi.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });


        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Lenders.this,History.class);
                i.putExtra("Name",getIntent().getStringExtra("Name"));
                i.putExtra("id",getIntent().getStringExtra("id"));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        del= (ImageView) findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(con);

                View promptView = layoutInflater.inflate(R.layout.delete , null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);

                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);

                //   final EditText input = (EditText) promptView.findViewById(R.id.userInput);

                // setup a dialog window
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                realm = Realm.getDefaultInstance();




                                final User user = realm.where(User.class).findFirst();




                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(Util.DELETE+"/"+getIntent().getStringExtra("id"))

                                        .addHeader("Authorization", "Token token="+user.getToken())
                                        .delete()
                                        .build();

                                Call call = client.newCall(request);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("SHAN",e+"    exception");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if(response.isSuccessful()){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {



                                                  Intent intent=new Intent(getApplicationContext(),ConnectionInfo.class);
                                                    startActivity(intent);

                                                    finish();

                                                    Intent broadcastIntent = new Intent();
                                                    broadcastIntent.setAction("com.example.dell.wifiexplorer.ACTION_DELETE");
                                                    sendBroadcast(broadcastIntent);
                                                    Toast.makeText(getApplicationContext(),"Wifi has been deleted",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            Log.d("SHAN"  , "Sucess"+response);

                                        }
                                        else {
                                            Log.d("SHAN"," not   exception"+response);

                                        }

                                    }


                                });




                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,    int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();
            }

        });

    }
}
