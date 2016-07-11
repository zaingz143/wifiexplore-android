package com.example.dell.wifiexplorer;

import android.support.v7.widget.RecyclerView;

/**
 * Created by dell on 3/3/2016.
 */
public class UserHistoryContacts {
    private String name, contactNo;
    private int pic;

    public UserHistoryContacts() {
    }

    public UserHistoryContacts(String name, String contactNo) {
        this.name = name;
        this.contactNo = contactNo;
        this.pic = pic;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}