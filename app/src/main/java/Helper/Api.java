package Helper;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zain on 3/17/16.
 */
public class Api {


    public static Request userSignInRequest(String email, String password){
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(Util.SIGNIN_URL)
                .post(body)
                .build();

        return request;


    }
    public static Request userProfileUpdate(String firstName, String lastName,String phone,String password, String token){
        FormBody body = new FormBody.Builder()
                .add("user[first_name]",firstName)
                .add("user[last_name]", lastName)
                .add("user[mobile_number]", phone)
                .add("user[password]", password)
                .build();
        Request request = new Request.Builder()
                .url(Util.USER_PROFILE_UPDATE)
                .patch(body)
                .addHeader("Authorization", "Token token="+token)
                .build();

        return request;


    }
    public static Request userSignUpRequest(String firstName, String lastName,String email, String password){
        RequestBody body=new FormBody.Builder()
                .add("user[first_name]",firstName)
                .add("user[last_name]", lastName)
                .add("user[email]", email)
                .add("user[password]", password)
                .build();

        Request request = new Request.Builder()
                .url(Util.SIGNUP_URL)
                .post(body)
                .build();

        return request;
    }
    public static Request userEmailRequest(String token){

        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token=" + token)
                .url(Util.SIGNUP_URL)
                .build();
        return request;
    }
    public  static Request userPhoneVerify(String mobile, String token){
        RequestBody body = new FormBody.Builder()
                .add("mobile_number", mobile)
                .build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token="+token)
                .url(Util.PHONE_URL)
                .patch(body)
                .build();
        return request;
    }
    public static Request userCodeRequest(String token, String code){
        RequestBody formBody = new FormBody.Builder()
                .add("mobile_token", code)
                .build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token="+token)
                .url(Util.PHONE_URL)
                .post(formBody)
                .build();
        return request;
    }
    public static Request userSignOutRequest(String token){
        RequestBody formBody = new FormBody.Builder()
                .add("mobile_token", "1122334455")
                .build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token=" + token)
                .url(Util.SIGNOUT_URL)
                .post(formBody)
                .build();
        return request;
    }
    public static Request userLocation(String name, String ssid, double lati, double longi,String pass,String token, String sec, String address ){
        RequestBody body=new FormBody.Builder()
                .add("wifi[name]",name)
                .add("wifi[ssid]", ssid)
                .add("wifi[security_type]", sec)
                .add("wifi[lat]", String.valueOf(lati))
                .add("wifi[long]", String.valueOf(longi))
                .add("wifi[address]",address)
                .add("wifi[password]", pass)
                .build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token="+token)
                .url(Util.WIFI_ADD_URL)
                .post(body)
                .build();

        return request;
    }






/*
    public static Request getWifiObject(String url){

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();


    }
*/
public static Request deleteWifi(String id,String token ){
    RequestBody body=new FormBody.Builder()
            .add("wifi[id]",id)

            .build();

    Request request = new Request.Builder()
            .addHeader("Authorization", "Token token="+token)
            .url(Util.DELETE)
            .post(body)
            .delete()
            .build();

    return request;
}


    public static Request userConnectionState (String id, String download, String upload, String start, String end, String token ){
        RequestBody body=new FormBody.Builder()
                .add("connection[wifi_id]", id)
                .add("connection[download_data]", download)
                .add("connection[upload_data]", upload)
                .add("connection[connected_at]", start)
                .add("connection[disconnected_at]", end)
                .build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token="+token)
                .url(Util.WIFI_CONNECTION_STATE_URL)
                .post(body)
                .build();

        return request;
    }
    public static Request rating (String id, String rating, String token ){
        RequestBody body=new FormBody.Builder()
                .add("rating[connection_id]", id)
                .add("rating[rate]", rating)
                .build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Token token="+token)
                .url("https://wifi-api.herokuapp.com//wifi/connections/ratings")
                .post(body)
                .build();

        return request;
    }

}
