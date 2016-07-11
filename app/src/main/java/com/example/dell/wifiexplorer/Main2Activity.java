package com.example.dell.wifiexplorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    List<String> security_type = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Spinner spinner_appointment = (Spinner) findViewById(R.id.spinner);

        security_type.add("Select Type");
        security_type.add("Select Type1");
        security_type.add("Select Type2");


        ArrayAdapter<String> dataAdapter_security = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,security_type);
        dataAdapter_security.setDropDownViewResource(R.layout.spinner_item);
        spinner_appointment.setAdapter(dataAdapter_security);

    }
}
