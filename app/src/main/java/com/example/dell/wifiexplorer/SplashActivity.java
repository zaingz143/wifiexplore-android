package com.example.dell.wifiexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import Model.User;
import io.realm.Realm;

//import io.realm.Realm;
//import model.User;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
       final User user = realm.where(User.class).findFirst();
      realm.commitTransaction();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
              /*
                SplashActivity.this.startActivity(mainIntent);*/

                Intent mainIntent;
               // mainIntent = new Intent(SplashActivity.this, MainActivity.class);
               if (user==null) {

                     mainIntent = new Intent(SplashActivity.this, MainActivity.class);

                }
                else if(!user.isEmailVerified()){
                   mainIntent = new Intent(SplashActivity.this, EmailVerifyActivity.class);

                }
               else if(!user.isMobileVerified()){
                    mainIntent = new Intent(SplashActivity.this, PhoneVerifyActivity.class);

                }
               else{
                   mainIntent = new Intent(SplashActivity.this, Options.class);

              }
               realm.close();
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
