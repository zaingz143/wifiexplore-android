package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Helper.Api;
import Helper.ConnectionHelper;
import Helper.Util;
import Model.User;
import Model.Wifi;
import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AddWifi extends AppCompatActivity {

    private EditText lenderName  ;
    private EditText ssid ;
    private EditText wifiPass  ;
    private RelativeLayout  setlocation;
    private RelativeLayout addl;
    private ImageView back;
    Integer indexValue=-3;
    Integer index=-1;
    TextView na;
    String selected_val;

    static int count=0;
    String wifiName,wifiSsid;
    String token;
    Realm realm;
    List<String> security_type = new ArrayList<String>();
    List<String> type = new ArrayList<String>();

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
        setContentView(R.layout.activity_add_wifi);





        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);




        final ConnectionHelper   connectionHelper = new ConnectionHelper(getApplicationContext());
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);


        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        final OkHttpClient client = new OkHttpClient();
        progressView.setVisibility(View.INVISIBLE);
        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });


        security_type.add("Security type");
        security_type.add("WPA");
        security_type.add("WPA2");
        security_type.add("PSK");
        indexValue = spinner.getSelectedItemPosition();


        addl= (RelativeLayout) findViewById(R.id.add);

        lenderName= (EditText) findViewById(R.id.lenderName);
        ssid= (EditText) findViewById(R.id.ssid);
        wifiPass= (EditText) findViewById(R.id.wifipass);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                selected_val=spinner.getSelectedItem().toString();
               // Log.d("SHAN",selected_val+"  "+arg2+"  "+arg3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


            setlocation= (RelativeLayout) findViewById(R.id.setlocation);
            setlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Text = spinner.getSelectedItem().toString();
                //Log.d("SHAN",Text+"   index value"+indexValue);
                count=2;
                Intent intent=new Intent(getApplicationContext(),SetLocation.class);
                intent.putExtra("Lender",lenderName.getText().toString());
                intent.putExtra("Ssid",ssid.getText().toString());
                intent.putExtra("Sec",selected_val);
                intent.putExtra("Type",Text);
                startActivity(intent);
                finish();
            }
        });

        if(count!=0)
        {
            lenderName.setText(getIntent().getStringExtra("Lender"));
            ssid.setText(getIntent().getStringExtra("Ssid"));
            type.add(getIntent().getStringExtra("Type"));
            ArrayAdapter<String> dataAdapter_security = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type);
            dataAdapter_security.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(dataAdapter_security);

            TextView na= (TextView) findViewById(R.id.setl);
            if(!(getIntent().getStringExtra("address") ==null))
            {
                na.setText(getIntent().getStringExtra("address").toString());
            }
            else {
                na  = (TextView) findViewById(R.id.setl);
                na.setText("Set your location");
            }

        }

        if(count==0)
        {
            na= (TextView) findViewById(R.id.setl);
            na.setText("Set your location");
        }
        addl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lenderName.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Lender name can't be empty", Toast.LENGTH_SHORT).show();
                } else if (ssid.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "SSID can't be empty", Toast.LENGTH_SHORT).show();

                } else if (wifiPass.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "WiFi password can't be empty", Toast.LENGTH_SHORT).show();
                } else if (indexValue == -3) {

                    Toast.makeText(getApplicationContext(), "Select security type", Toast.LENGTH_SHORT).show();
                } else if (indexValue == 0 || indexValue == 1 || indexValue == 2) {
                    Toast.makeText(getApplicationContext(), "Select security type" + indexValue, Toast.LENGTH_SHORT).show();
                }
                else if(getIntent().getStringExtra("address") == null){
                    Toast.makeText(getApplicationContext(), "Select your location", Toast.LENGTH_SHORT).show();

                }


                else {

                    if (connectionHelper.isGPSEnabled())
                    {
                        //Log.d("tayyab", "  " + getIntent().getDoubleExtra("Longi", 0) + "   " + getIntent().getDoubleExtra("Lati", 0));
                        realm = Realm.getDefaultInstance();
                        final User user = realm.where(User.class).findFirst();
                        token = user.getToken();
                        realm.close();

                        progressView.setVisibility(View.VISIBLE);
                        progressView.startAnimation();

                        client.newCall(Api.userLocation(
                                lenderName.getText().toString(),
                                ssid.getText().toString(),
                                getIntent().getDoubleExtra("Lati", 0),
                                getIntent().getDoubleExtra("Longi", 0),
                                wifiPass.getText().toString(),
                                token,
                                getIntent().getStringExtra("Sec"),
                                getIntent().getStringExtra("address")

                        )).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressView.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }

                            @Override
                            public void onResponse(Call call, Response response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressView.setVisibility(View.INVISIBLE);
                                    }
                                });
                                if (response.isSuccessful())
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent= new Intent(getApplicationContext(),ConnectToWifi.class);
                                            AddWifi.this.startActivity(intent);
                                            finish();
                                            Toast.makeText(getApplicationContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                } else {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressView.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }


                            }
                        });


                    }//end last else part

                    else {
                        //  Toast.makeText(getApplicationContext(),"GPS location is off",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("GPS is off");  // GPS not found
                        builder.setMessage("Turn on location services"); // Want to enable?
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AddWifi.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });
                        builder.setNegativeButton("No", null);
                        builder.create().show();
                    }



                }//end if



            }

        });


        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
        ArrayAdapter<String> dataAdapter_security = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,security_type);
        dataAdapter_security.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter_security);

    }


}
