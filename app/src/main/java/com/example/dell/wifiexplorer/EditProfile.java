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

import org.json.JSONObject;

import java.io.IOException;

import Helper.Api;
import Helper.Util;
import Model.User;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class EditProfile extends AppCompatActivity {
    private Realm realm;
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText phone;
    private EditText pass;
    private EditText cpass;
    private RelativeLayout update;
    String error;
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
        setContentView(R.layout.activity_edit_profile);

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











        final OkHttpClient client = new OkHttpClient();
        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);

        progressView.setVisibility(View.INVISIBLE);
        firstname= (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);

        pass= (EditText) findViewById(R.id.pass);
        cpass= (EditText) findViewById(R.id.cpass);
        phone= (EditText) findViewById(R.id.phone);
        realm = Realm.getDefaultInstance();

        final User user = realm.where(User.class).findFirst();
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
       phone.setText(user.getMobileNumber());
        update= (RelativeLayout) findViewById(R.id.updatePro);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



        if (firstname.getText().toString().length() < 1) {
            Toast.makeText(getApplicationContext(), "First name can't be empty", Toast.LENGTH_SHORT).show();
        } else if (lastname.getText().toString().length() < 1) {
            Toast.makeText(getApplicationContext(), "Last name can't be empty", Toast.LENGTH_SHORT).show();
        }
        else if (phone.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Phone number  can't be empty", Toast.LENGTH_SHORT).show();
        }
        else if (pass.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Password can't be empty", Toast.LENGTH_SHORT).show();

        } else if (cpass.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Confirm password can't be empty", Toast.LENGTH_SHORT).show();
        }
        else if(!(pass.getText().toString().equals(cpass.getText().toString())))
        {
            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
        }
        else

        {


            client.newCall(Api.userProfileUpdate(
                    firstname.getText().toString(),
                    lastname.getText().toString(),
                    phone.getText().toString(),
                    pass.getText().toString(),
                   user.getToken()

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


                }

                @Override
                public void onResponse(Call call, Response response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressView.setVisibility(View.INVISIBLE);
                        }
                    });
                    if(response.isSuccessful()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Sucessfully Updated",Toast.LENGTH_SHORT).show();
                            }
                        });


                        try{
                            User user = Util.getGsonObject().fromJson(response.body().string(),
                                    User.class);

                            realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            if (realm.where(User.class).count() > 0) {
                                realm.clear(User.class);
                            }
                            realm.copyToRealm(user);
                            realm.commitTransaction();
                            Intent intent=new Intent(getApplicationContext(),EmailVerifyActivity.class);
                            startActivity(intent);
                            finish();


                        }catch (Exception e){
                            Log.i("zaing", "Exception" + e.getMessage());
                        } finally {
                            realm.close();
                        }
                    }

                    else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressView.setVisibility(View.INVISIBLE);
                            }
                        });


                        try {
                            String obj=response.body().string();
                            Log.d("zaing", "  " + obj);
                            JSONObject jo = new JSONObject(obj);

                            error = jo.getString("email");
                            error = error.replaceAll("[^\\w+\\s+\\w+]", "");

                            Log.d("zaing", "Email "+ error);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                                }
                            });



                        }catch (Exception e){
                            Log.d("zaing", "  " + e);
                        }

                    }


                }
            });





        }
            }
        });

    }
}
