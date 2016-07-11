package com.example.dell.wifiexplorer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

/**
 * Created by dell on 3/3/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<Contacts> ContactList;
    View itemView;
    Context conn;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, bill, pic;
        public ImageView profile_image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            bill = (TextView) view.findViewById(R.id.bill);
            //pic = (TextView) view.findViewById(R.id.pic);
            //profile_image = (ImageView) view.findViewById(R.id.profile_image);
        }
    }


    public ContactAdapter(List<Contacts> ContactList, Context con ) {

        this.ContactList = ContactList;
        this.conn=con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Contacts con = ContactList.get(position);
        holder.name.setText(con.getName());

        holder.bill.setText(con.getContactNo());
       // holder.pic.setText(""+con.getPic());

      /*  itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(conn,UserHistory.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                conn.startActivity(i);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }
}