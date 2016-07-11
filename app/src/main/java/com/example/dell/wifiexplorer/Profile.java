package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;


import java.io.IOException;

import Helper.Api;
import Model.User;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Profile extends AppCompatActivity {






    TextView payment, username;
    LinearLayout signout;
    OkHttpClient client;
    LinearLayout his ,editpro;
    CircularProgressView progressView;
    private static final String TAG = "SHAN";

    static final String ITEM_SKU = "android.test.purchased";

Realm realm;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        payment= (TextView) findViewById(R.id.payment);
        signout= (LinearLayout) findViewById(R.id.signout);
        username= (TextView) findViewById(R.id.username);
        realm = Realm.getDefaultInstance();


        final User user = realm.where(User.class).findFirst();

        username.setText(user.getFirstName()+" "+user.getLastName());
        realm.close();
        his= (LinearLayout) findViewById(R.id.his);

        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserHistory.class);
                startActivity(intent);
            }
        });
        editpro= (LinearLayout) findViewById(R.id.editpro);
        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
            }
        });
        progressView= (CircularProgressView) findViewById(R.id.progress_view);

        progressView.setVisibility(View.INVISIBLE);

        client = new OkHttpClient();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setVisibility(View.VISIBLE);
                signOut();
            }
        });


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectItemActivity.class);
                startActivity(intent);

            }
        });

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressView.setVisibility(View.INVISIBLE);
                        }
                    });






                    Intent mainIntent = new Intent(Profile.this, Login.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);


                    Profile.this.startActivity(mainIntent);

                    Profile.this.finish();
                }

            }
        });
        realm.clear(User.class);
        realm.commitTransaction();
        realm.close();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);
    }


}
