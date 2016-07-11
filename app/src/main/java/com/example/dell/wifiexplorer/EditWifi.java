package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.IOException;

import Helper.Util;
import Model.User;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditWifi extends AppCompatActivity {

    private EditText lenderName  ;
    private EditText wifiPass  ;
    private RelativeLayout update;
    Realm realm;
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
        setContentView(R.layout.activity_edit_wifi);


        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);
   /*     IntentFilter intentFilter = new IntentFilter();
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







        lenderName= (EditText) findViewById(R.id.lenderName);
        wifiPass= (EditText) findViewById(R.id.wifipass);
        lenderName.setText(getIntent().getStringExtra("name"));

        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        final OkHttpClient client = new OkHttpClient();
        progressView.setVisibility(View.INVISIBLE);

        update= (RelativeLayout) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lenderName.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Lender name can't be empty", Toast.LENGTH_SHORT).show();
                }else if (wifiPass.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "WiFi password can't be empty", Toast.LENGTH_SHORT).show();
                }
                else{


                    realm = Realm.getDefaultInstance();




                    final User user = realm.where(User.class).findFirst();


                    RequestBody body=new FormBody.Builder()
                            .add("wifi[name]",lenderName.getText().toString())

                            .add("wifi[password]", wifiPass.getText().toString())
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Util.DELETE+"/"+getIntent().getStringExtra("id"))

                            .addHeader("Authorization", "Token token="+user.getToken())
                            .put(body)

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
            }
        });
    }
}
