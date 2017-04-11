package com.android.example.trash;

/**
 * Created by NaderNabil on 2/25/2017.
 */

public class subscriptions {
    private String bundle;
    private String location;
    private String request;
    private String shift;
    private String starting_date;
    private String title;
    private String user_id;

    public subscriptions() {
    }

    public subscriptions(String user_id, String title, String starting_date, String shift, String request, String location, String bundle) {
        this.user_id = user_id;
        this.title = title;
        this.starting_date = starting_date;
        this.shift = shift;
        this.request = request;
        this.location = location;
        this.bundle = bundle;
    }

    public String getBundle() {
        return bundle;
    }

    public String getLocation() {
        return location;
    }

    public String getRequest() {
        return request;
    }

    public String getShift() {
        return shift;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_id() {
        return user_id;
    }
}
