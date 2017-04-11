package com.android.example.trash;

/**
 * Created by NaderNabil on 2/13/2017.
 */

public class schedual_obj {
    String Location;
    String Time_interval;
    String title ;

    public String getLocation() {
        return Location;
    }

    public String getTitle() {
        return title;
    }


    public String getTime_interval() {
        return Time_interval;
    }

    public schedual_obj(String title, String time_interval,String location) {

        this.title = title;
        Time_interval = time_interval;
        Location = location;
    }
}
