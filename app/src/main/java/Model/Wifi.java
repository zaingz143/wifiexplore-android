package Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Muhammad Shan on 24/03/2016.
 */
public class Wifi extends RealmObject {

    private String key;



    private String lenderName;
    private String ssid;
    private String security;

    private double latitude;
    private double longitude;

    public Wifi() {
    }

    public Wifi(String key, String lenderName, String ssid, String security, double latitude, double longitude, String password) {
        this.key = key;
        this.lenderName = lenderName;
        this.ssid = ssid;
        this.security = security;

        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
    }

    private String password;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
