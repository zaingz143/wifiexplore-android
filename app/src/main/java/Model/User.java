package Model;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zain on 3/14/16.
 */
public class User extends RealmObject {
    @PrimaryKey
    private String token;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    @SerializedName("mobile_number")
    private String mobileNumber;
    @SerializedName("email_verified")
    private boolean isEmailVerified;
    @SerializedName("number_verified")
    private boolean isMobileVerified;

    public User(){}

    public User(String token, String firstName, String lastName, String email, String mobileNumber, boolean isEmailVerified, boolean isMobileVerified) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.isEmailVerified = isEmailVerified;
        this.isMobileVerified = isMobileVerified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setIsMobileVerified(boolean isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", isMobileVerified=" + isMobileVerified +
                '}';
    }
}
