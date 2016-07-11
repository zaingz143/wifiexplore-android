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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import Model.UserWifi;

/**
 * Created by Muhammad Shan on 13/06/2016.
 */
public class UserAdpter extends RecyclerView.Adapter<UserAdpter.MyViewHolder> {
    List<JSONObject> list;
Context con;

    View itemView;

    public UserAdpter(List<JSONObject> horizontalList , Context context) {

        this.list = horizontalList;
        this.con=context;



    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizantol, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        try {
            holder.txtView.setText(list.get(position).getString("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

   holder.view3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                     Intent i= new Intent(con,Lenders.class);
                        i.putExtra("name",list.get(position).getString("name"));
                        i.putExtra("id",list.get(position).getString("id"));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       con. startActivity(i);

                    } catch (JSONException e) {
                        Log.d("SHAN","WRRRRR"+e);
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;
        public ImageView router;
        View view3;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.routername);
            view3=(View) view.findViewById(R.id.view3);

        }
    }
}
