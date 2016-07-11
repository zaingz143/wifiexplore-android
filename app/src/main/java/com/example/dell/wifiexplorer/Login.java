package com.example.dell.wifiexplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class Login extends AppCompatActivity {

    private EditText uname;
    private EditText pass;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RelativeLayout login=(RelativeLayout)findViewById(R.id.login);
        uname= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.pass);
        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        final OkHttpClient client = new OkHttpClient();
        progressView.setVisibility(View.INVISIBLE);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uname.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                } else if (pass.getText().toString().length() < 8){
                    Toast.makeText(getApplicationContext(), "Password length is too short", Toast.LENGTH_SHORT).show();
                }
                else if(!(Util.isEmailValid(uname.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Email is not valid",Toast.LENGTH_SHORT).show();

                }
                else
                {
                        Log.d("SHAN", uname.getText().toString());
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();

                    client.newCall(Api.userSignInRequest(
                            uname.getText().toString(),

                            pass.getText().toString()
                    )).enqueue(new Callback() {

                                   @Override
                                   public void onFailure(Call call, IOException e) {
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               progressView.setVisibility(View.INVISIBLE);
                                           }
                                       });


                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_SHORT).show();                                }
                                       });

                                       //Snackbar.make(loginBtn, "Connection error", Snackbar.LENGTH_LONG)
                                           //    .setAction("Action", null).show();
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
                                           //

                                           try {

                                               User user = Util.getGsonObject().fromJson(response.body().string(),
                                                       User.class);
                                               Log.d("SHAN"," email  " +user.isEmailVerified()+"     "+user.isMobileVerified());
                                               Log.d("SHAN"," Ob " +response.toString());


                                               realm = Realm.getDefaultInstance();

                                               realm.beginTransaction();
                                               if (realm.where(User.class).count() > 0) {
                                                   realm.clear(User.class);
                                               }
                                               realm.copyToRealm(user);
                                               realm.commitTransaction();

                                               Intent mainIntent;


                                               if (!user.isEmailVerified())
                                                   mainIntent = new Intent(Login.this, EmailVerifyActivity.class);
                                               else if (!user.isMobileVerified())
                                                   mainIntent = new Intent(Login.this, PhoneVerifyActivity.class);
                                               else
                                                   mainIntent = new Intent(Login.this, Options.class);

                                               Login.this.startActivity(mainIntent);
                                               Login.this.finish();
                                           } catch (Exception e) {
                                               Log.i("zain", "Exception" + e.getMessage());
                                           } finally {
                                               realm.close();
                                           }
                                       } else {

                                           runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(getApplicationContext(),"Invalid Email or Password",Toast.LENGTH_SHORT).show();                                }
                                           });


                                       }
                                   }
                               }

                    );



                }
            }
        });
    }
}
