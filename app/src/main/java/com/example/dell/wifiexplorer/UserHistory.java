package com.example.dell.wifiexplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class UserHistory extends AppCompatActivity {

    private List<UserHistoryContacts> contactsList2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserHistoryAdapter mAdapter;
    TextView username;
    Realm realm;
    CircleImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
        username= (TextView) findViewById(R.id.name);
        realm = Realm.getDefaultInstance();


        final User userr = realm.where(User.class).findFirst();

        username.setText(userr.getFirstName()+" "+userr.getLastName());
        realm.close();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new UserHistoryAdapter(contactsList2, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        profile= (CircleImageView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Profile.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        prepareContactData();

    }
    private void prepareContactData() {
        UserHistoryContacts con = new UserHistoryContacts("Azman", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("ATNT", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("PTCL", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("ZONG", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("Azman", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("ATNT", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("PTCL", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("ZONG", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("Azman", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("ATNT", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("PTCL", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("ZONG", "$ 24");
        contactsList2.add(con);
        con = new UserHistoryContacts("Azman", "$ 24");
        contactsList2.add(con);
    }
}
