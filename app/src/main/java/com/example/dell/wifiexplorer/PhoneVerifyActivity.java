package com.example.dell.wifiexplorer;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.otto.Subscribe;


import java.io.IOException;


import Helper.Api;
import Helper.BusProvider;
import Helper.Util;
import Model.User;
import io.realm.Realm;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class PhoneVerifyActivity extends AppCompatActivity {

    EditText mob;
    RelativeLayout verify_btn;
    Realm realm;
    String token;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);



        final OkHttpClient client = new OkHttpClient();

        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.INVISIBLE);

        mob= (EditText) findViewById(R.id.mob);
        verify_btn= (RelativeLayout) findViewById(R.id.confirm);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Number can't be empty", Toast.LENGTH_SHORT).show();

                } else {
                    progressView.setVisibility(View.VISIBLE);

                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    user = realm.where(User.class).findFirst();
                    token = user.getToken();
                    realm.commitTransaction();
                    realm.close();
                    user = null;


                    client.newCall(Api.userPhoneVerify(
                            mob.getText().toString(), token
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
                            if (response.isSuccessful()) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Confirmation code is sent",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Toast.makeText(getApplicationContext(), "Confirmation code is sent", Toast.LENGTH_SHORT).show();
                            } else {



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Oops..! Something went wrong",Toast.LENGTH_SHORT).show();
                                    }
                                });

                               // Toast.makeText(getApplicationContext(), "Oops..! Something went wrong", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });
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
    public void onMessageEvent(String event){

        finish();
    }

}
