package Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Muhammad Shan on 11/04/2016.
 */
public class Connection extends RealmObject {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @PrimaryKey
    private String key;

    private String name;
    private long firstTime;
    private long lastTime ;
    private double dataStarted;
    private double dataEnded;




    private RealmList<Feedback> feedback;

    public Connection() {
    }

    public Connection(String name, long firstTime, long lastTime, double dataStarted, double dataEnded, String key) {
        this.name = name;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.dataStarted = dataStarted;
        this.dataEnded = dataEnded;

        this.key=key;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public double getDataStarted() {
        return dataStarted;
    }

    public void setDataStarted(double dataStarted) {
        this.dataStarted = dataStarted;
    }

    public double getDataEnded() {
        return dataEnded;
    }

    public void setDataEnded(double dataEnded) {
        this.dataEnded = dataEnded;
    }









}
