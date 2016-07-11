package Model;

import io.realm.RealmObject;

/**
 * Created by Muhammad Shan on 22/04/2016.
 */
public class Feedback extends RealmObject {

    private String comments;
    private int rating;
    private String key;

    public Feedback() {
    }

    public Feedback(String comments, int rating, String key) {
        this.comments = comments;
        this.rating = rating;
        this.key = key;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
