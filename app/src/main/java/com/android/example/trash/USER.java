package com.android.example.trash;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by NaderNabil on 2/25/2017.
 */

public class USER {
    String email ;
    String name ;
    String phone ;
    ArrayList<String> subscriptions ;

    public USER() {
        Log.d("nader","constructor called");
    }
//    public USER(String email, String name, String phone , HashMap<String, String> subscriptions) {
//        this.email = email;
//        this.name = name;
//        this.phone = phone;
//        this.subscriptions =subscriptions ;
//    }

    public USER(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
