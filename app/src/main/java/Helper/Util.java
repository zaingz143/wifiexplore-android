package Helper;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;


public class Util {
    public static final String BASE_URL = "http://wifi-api.herokuapp.com";
    public static final String SIGNIN_URL = BASE_URL+"/user/signin";
    public static final String SIGNUP_URL=  BASE_URL+"/users";
    public static final String USER_PROFILE_UPDATE=  BASE_URL+"/user/update";
    public static final String PHONE_URL=BASE_URL+"/user/verify/mobile";
    public static final String SIGNOUT_URL=BASE_URL+"/user/signout";
    public static final String WIFI_ADD_URL=BASE_URL+"/wifis";
    public static final String NEAR_BY_WIFI=BASE_URL+"/wifi/near";

    public static final String DELETE=BASE_URL+"/wifis";


    public static final String WIFI_CONNECTION_STATE_URL=BASE_URL+"/wifi/connections";


    public static final String RATING=BASE_URL+"/wifi/connections/ratings";


//wifi/connections/ratings(post id rate )rating["rate"] rating{connection_id}

    public static Gson getGsonObject(){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
        return gson;
    }

  /*  public static void setError(EditText et, String msg){
        et.setError(msg);
        YoYo.with(Techniques.Shake)
                .duration(700)
                .delay(20)
                .playOn((View) et);

    }*/


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
