package com.example.dell.wifiexplorer;

import android.content.Intent;
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

public class Signup extends AppCompatActivity {
    String error;
    private Realm realm;
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText phone;
    private EditText pass;
    private EditText cpass;
    private RelativeLayout register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname= (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);
        email= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.pass);
        cpass= (EditText) findViewById(R.id.cpass);
        phone= (EditText) findViewById(R.id.phone);
        register= (RelativeLayout) findViewById(R.id.register);

        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        final OkHttpClient client = new OkHttpClient();
        progressView.setVisibility(View.INVISIBLE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstname.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "First name can't be empty", Toast.LENGTH_SHORT).show();
                } else if (lastname.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), "Last name can't be empty", Toast.LENGTH_SHORT).show();

                } else if (email.getText().toString().length() < 1){
                     Toast.makeText(getApplicationContext(), "Email can't be empty", Toast.LENGTH_SHORT).show();
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
                else if(!(Util.isEmailValid(email.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Email is not valid",Toast.LENGTH_SHORT).show();

                }

                else{
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();




                    client.newCall(Api.userSignUpRequest(
                            firstname.getText().toString(),
                            lastname.getText().toString(),
                            email.getText().toString(),
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
                                        Toast.makeText(getApplicationContext(),"Sucessfully server resopnse",Toast.LENGTH_SHORT).show();
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
                                    Log.i("zain", "Exception" + e.getMessage());
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
                                    Log.d("Object", "  " + obj);
                                    JSONObject jo = new JSONObject(obj);

                                  error = jo.getString("email");
                                    error = error.replaceAll("[^\\w+\\s+\\w+]", "");

                                    Log.d("Object", "Email "+ error);
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





                }//end last else part



            }
        });












    }
}
