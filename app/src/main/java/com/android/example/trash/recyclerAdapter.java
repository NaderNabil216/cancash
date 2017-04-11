package com.android.example.trash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NaderNabil on 2/13/2017.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<subscriptions> subscriptions_list;

    public recyclerAdapter(Context context, ArrayList<subscriptions> subscriptions_list) {
        this.context = context;
        this.subscriptions_list = subscriptions_list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,location;
        public RelativeLayout relativeLayout ;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_title);
            location = (TextView) view.findViewById(R.id.textView_map_location);
            relativeLayout=(RelativeLayout)view.findViewById(R.id.relative_layout);
        }
    }

    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_schedule_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(recyclerAdapter.MyViewHolder holder, int position) {
        Log.d("nader","abl el set text");
        final subscriptions subscription = subscriptions_list.get(position);
        holder.title.setText(subscription.getTitle());
        holder.location.setText(subscription.getLocation());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,subscription_details.class);
                intent.putExtra("title",subscription.getTitle());
                intent.putExtra("location",subscription.getLocation());
                intent.putExtra("bundle",subscription.getBundle());
                intent.putExtra("shift",subscription.getShift());
                intent.putExtra("request",subscription.getRequest());
                intent.putExtra("date",subscription.getStarting_date());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("nader","size of array gowa el adapter "+ subscriptions_list.size());
        return subscriptions_list.size();
    }



}
