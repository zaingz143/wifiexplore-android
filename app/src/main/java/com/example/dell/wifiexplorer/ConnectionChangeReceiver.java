package com.example.dell.wifiexplorer;




        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.wifi.WifiManager;
        import android.support.v4.app.NotificationCompat;
        import android.support.v4.app.TaskStackBuilder;
        import android.widget.Toast;

        import Helper.BusProvider;
        import Helper.ConnectionHelper;


public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("com.example.Broadcast")){

            Toast.makeText(context, "Wifi connected to "+intent.getStringExtra("name").toString(), Toast.LENGTH_SHORT).show();

        }


            String action  = intent.getAction();
       /* ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();{
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Toast.makeText(context, "Wifi connected", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Wifi not connected", Toast.LENGTH_SHORT).show();
    }
*/


     /*   if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            int networkType = intent.getIntExtra(
                    android.net.ConnectivityManager.EXTRA_NETWORK_TYPE, -1);
            if (ConnectivityManager.TYPE_WIFI == networkType) {
                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.isConnected()) {

                        // TODO: wifi is connected
                        Toast.makeText(context, "Wifi connected", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Wifi not connected", Toast.LENGTH_SHORT).show();

                        // TODO: wifi is not connected
                    }
                }
            }

        }*/

       /* if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){

            WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled()) {
                Toast.makeText(context, "Wifi On", Toast.LENGTH_SHORT).show();
                BusProvider.getInstance().post("ON");
            }
            else {
                Toast.makeText(context, "Wifi of", Toast.LENGTH_SHORT).show();
                BusProvider.getInstance().post("OFF");

            }


        }*/


        ConnectionHelper connectionHelper = new ConnectionHelper(context);

        if(connectionHelper.wifiOn()){
            //  Toast.makeText(context,"WI-FI is on",Toast.LENGTH_SHORT).show();
          /*  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setSmallIcon(R.drawable.logo);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.logo);
            mBuilder.setLargeIcon(icon);
            mBuilder.setContentTitle("Wifi networks are available");
            mBuilder.setContentText("Click to connect to nearby wifi networks");
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


*/



            if (connectionHelper.wifiConnected()) {

                //posting to activity about the connection

                BusProvider.getInstance().post(11);

            }else{


             /*   Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);




                // wifi is on only.....
                mNotificationManager.notify(52658555, mBuilder.build());*/


                //posting to activity for changing the fragment for list
                BusProvider.getInstance().post(10);
                //wifi is only on yet

            }

        }
        else{
            //all dead :-)
            BusProvider.getInstance().post(00);
        }





    /*    ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            EventBus.getDefault().post(11);

        } else {
            EventBus.getDefault().post(00);

        }*/
    }
}
