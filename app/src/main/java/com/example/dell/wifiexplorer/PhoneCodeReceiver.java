package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;

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

/**
 * Created by Muhammad Shan on 19/03/2016.
 */
public class PhoneCodeReceiver extends BroadcastReceiver {
    final OkHttpClient client = new OkHttpClient();
    Realm realm;
    User user;
    String token;
    Context con;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.con=context;
        final Bundle bundle = intent.getExtras();
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String message = currentMessage.getDisplayMessageBody();

                    if(message.contains("Your verification code is")) {

                        String code =message.replaceAll("\\D+", "");

                        realm= Realm.getDefaultInstance();
                        realm.beginTransaction();
                        user = realm.where(User.class).findFirst();
                        token= user.getToken();
                        realm.commitTransaction();
                        realm.close();
                        user=null;
                    Log.d("ZAIN",token+ "   "+code);
                        client.newCall(Api.userCodeRequest(
                                token,code
                        )).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                               }


                            @Override
                            public void onResponse(Call call, Response response)  {

                                        if(response.isSuccessful()){
                                            try {
                                                user = Util.getGsonObject().fromJson(response.body().string(), User.class);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            user.setToken(token);
                                            realm = Realm.getDefaultInstance();
                                            realm.beginTransaction();
                                            realm.copyToRealmOrUpdate(user);
                                            realm.commitTransaction();
                                            realm.close();

                                            Intent intent=new Intent(con,Options.class);





                                           final Handler handler = new Handler(Looper.getMainLooper());



                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            BusProvider.getInstance().post("SMS");
                                                        }
                                                    });
















                                           // BusProvider.getInstance().post("SMS");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            con.startActivity(intent);



                                        }
                            }
                        });

                    }

                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }
}
