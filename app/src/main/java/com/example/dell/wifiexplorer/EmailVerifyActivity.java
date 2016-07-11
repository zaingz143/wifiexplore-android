package com.example.dell.wifiexplorer;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.IOException;

import Helper.Api;
import Helper.Util;
import Model.User;
import io.realm.Realm;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class EmailVerifyActivity extends AppCompatActivity {
    RelativeLayout verify_btn;
    TextView resend;
    Realm realm;
    String token;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);



        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.INVISIBLE);

        final OkHttpClient client = new OkHttpClient();

        verify_btn= (RelativeLayout) findViewById(R.id.confirm);
        resend= (TextView) findViewById(R.id.resend);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressView.setVisibility(View.VISIBLE);
                progressView.startAnimation();

                    realm= Realm.getDefaultInstance();
                    realm.beginTransaction();
                    user = realm.where(User.class).findFirst();
                    token= user.getToken();
                    realm.commitTransaction();
                    realm.close();
                user = null;

                        Log.d("EMAIL", "Chk" + token);

                client.newCall(Api.userEmailRequest(
                        token
                )).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressView.setVisibility(View.INVISIBLE);
                            }
                        });


                        Log.d("EMAIL", "error in server ");
                        //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                      /*  Snackbar.make(verify_btn, "Error", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*/
                    }

                    @Override
                    public void onResponse(Call call, Response response)  {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressView.setVisibility(View.INVISIBLE);
                            }
                        });

                        if(response.isSuccessful()){
                            Log.d("EMAIL", "response is sucess full ");
                            try {
                                user = Util.getGsonObject().fromJson(response.body().string(), User.class);
                                user.setToken(token);
                                Log.d("zain2", user.toString());
                                    if(user.isEmailVerified()){



                                        realm = Realm.getDefaultInstance();
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(user);
                                        realm.commitTransaction();
                                        realm.close();
                                       // Snackbar.make(verify_btn, "Email is verified", Snackbar.LENGTH_LONG)
                                             //   .setAction("Action", null).show();
                                        Intent myIntent = new Intent(getApplicationContext(), PhoneVerifyActivity.class);
                                        startActivity(myIntent);
                                        finish();

                                }
                                else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Email not verified",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                       // Log.d("EMAIL", "email not verified");
                                       // Toast.makeText(getApplicationContext(), "Email is not verified ", Toast.LENGTH_SHORT).show();
                                    }

                            }catch (Exception e){
                                Log.d("zaing", "  " + e);
                            }


                        }
                        else{
                            Log.d("EMAIL", "somthing went ewrmg");
                          //  Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setVisibility(View.VISIBLE);
                realm= Realm.getDefaultInstance();
                realm.beginTransaction();
                user = realm.where(User.class).findFirst();
                token= user.getToken();
                realm.commitTransaction();
                realm.close();
                user = null;
                //TODO: complete it
                progressView.setVisibility(View.INVISIBLE);


            }
        });

    }
}
