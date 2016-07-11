package com.example.dell.wifiexplorer;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zain on 3/14/16.
 */
public class WifiExploreApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("com.holygon.zaingz.alu")
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);



    }

}
