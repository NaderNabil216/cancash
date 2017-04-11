package com.android.example.trash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by NaderNabil on 2/13/2017.
 */

public class Schedule extends Fragment {
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    DatabaseReference subs = root.child("subscriptions"); // gadwal el subs

    private RecyclerView recyclerView;
    private recyclerAdapter adapter;
    private ArrayList<subscriptions> subscriptions__List = new ArrayList<>();
    ArrayList<sub_ref> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_schedule);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("nader", "before method on start");

        getdata();
        Log.d("nader", "after method on start");
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getdata() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        final String uid = sharedPreferences.getString("uid", "14");

        users.child(uid).child("subscribtions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("nader", dataSnapshot.getValue() + "");
                Log.d("nader", "out");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    sub_ref sub_ref = new sub_ref();
                    sub_ref.setSub_id(snapshot.getKey());
                    String s = snapshot.getValue(String.class);
                    String[] spliting = s.split(",");
                    sub_ref.setCity(spliting[0]);
                    sub_ref.setDist(spliting[1]);
                    arrayList.add(sub_ref);
                }
                get_subs();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void get_subs() {
        subscriptions__List.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d("nader", "in  " + i);
            Log.d("nader", "array size ba3d el for " + arrayList.size());
            subs.child(arrayList.get(i).getCity())
                    .child(arrayList.get(i).getDist())
                    .child(arrayList.get(i).getSub_id()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subscriptions sub = dataSnapshot.getValue(subscriptions.class);
                    subscriptions__List.add(sub);
                    Log.d("nader", "added to list");
                    adapter = new recyclerAdapter(getActivity(), subscriptions__List);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        Log.d("nader", "sub list abl el adapter barra " + subscriptions__List.size());
    }


}
