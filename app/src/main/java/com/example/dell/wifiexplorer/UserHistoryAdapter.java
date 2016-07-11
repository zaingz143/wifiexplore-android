package com.example.dell.wifiexplorer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 3/3/2016.
 */

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.MyViewHolder> {

    private List<UserHistoryContacts> ContactList;
    View itemView;
    Context con;

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


    public UserHistoryAdapter(List<UserHistoryContacts> ContactList, Context con ) {

        this.ContactList =ContactList;
        this.con=con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_history_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final UserHistoryContacts con = ContactList.get(position);
        holder.name.setText(con.getName());

        holder.bill.setText(con.getContactNo());
        // holder.pic.setText(""+con.getPic());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SHAN","Working");         }
        });

    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }
}


