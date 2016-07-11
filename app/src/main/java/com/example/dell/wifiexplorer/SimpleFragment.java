package com.example.dell.wifiexplorer;

/**
 * Created by Muhammad Shan on 24/05/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleFragment extends Fragment {

static String name;
    private static final String KEY_MESSAGE = "message";

    public SimpleFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_simple, container, false);


        View btnclick = (View) rootView.findViewById(R.id.view2);
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"working",Toast.LENGTH_SHORT).show();
                Intent i= new Intent(getActivity(),Lenders.class);
                i.putExtra("Name",getArguments().getString(KEY_MESSAGE));
                startActivity(i);
            }
        });
        View btnclick2 = (View) rootView.findViewById(R.id.view3);

      //  String msg = getArguments().getString(KEY_MESSAGE);
       // Log.d("SHAN","Bundle"+msg);
/*
        TextView textView = (TextView) rootView.findViewById(R.id.textview_in_fragment);
        String msg = getArguments().getString(KEY_MESSAGE);
        textView.setText(msg);*/

        return rootView;
    }

    public static Fragment newInstance(String message) {
        name=message;
        Log.d("SHAN","in fragment "+message);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        SimpleFragment fragment = new SimpleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
