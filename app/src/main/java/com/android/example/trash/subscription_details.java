package com.android.example.trash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class subscription_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String location = intent.getStringExtra("location");
        String bundle = intent.getStringExtra("bundle");
        String request = intent.getStringExtra("request");
        String date = intent.getStringExtra("date");
        String shift = intent.getStringExtra("shift");
        long l = Long.valueOf(date);
        String new_date = getDate(l);
        TextView title_tv = (TextView)findViewById(R.id.sub_det_title);
        TextView location_tv = (TextView)findViewById(R.id.sub_det_location);
        TextView bundle_tv = (TextView)findViewById(R.id.sub_det_bundle);
        TextView request_tv = (TextView)findViewById(R.id.sub_det_request);
        TextView date_tv = (TextView)findViewById(R.id.sub_det_date);
        TextView shift_tv = (TextView)findViewById(R.id.sub_det_shift);

        title_tv.setText(title);
        location_tv.setText("Location :"+location);
        bundle_tv.setText("Bundle :"+bundle);
        date_tv.setText("Date :"+new_date);
        request_tv.setText("Request :"+request);
        shift_tv.setText("Shift :"+shift);

    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
