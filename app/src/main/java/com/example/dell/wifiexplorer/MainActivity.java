package com.example.dell.wifiexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








        LinearLayout login=(LinearLayout)findViewById(R.id.login);

        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Login.class);

                startActivity(i);
                finish();
            }
        });
        LinearLayout btnsignup=(LinearLayout)findViewById(R.id.btnsingup);

        assert btnsignup != null;
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Signup.class);
                startActivity(i);
            }
        });
    }
}
