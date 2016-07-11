package com.example.dell.wifiexplorer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import com.example.dell.wifiexplorer.fragments.DateRangePickerFragment;

public class Payment extends AppCompatActivity implements DateRangePickerFragment.OnDateRangeSelectedListener {
//implements DatePickerDialog.OnDateSetListener

    ImageView calendar;
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
        setContentView(R.layout.activity_payment);

        logoutReceiver = new LogoutReceiver();

        // Register the logout receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.wifiexplorer.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);


        calendar= (ImageView) findViewById(R.id.calendar);



    /*    IntentFilter intentFilter = new IntentFilter();
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


        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Payment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");*/


                DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(Payment.this,false);
                dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");

            }
        });
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {

    }




  /*  @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }*/
}
