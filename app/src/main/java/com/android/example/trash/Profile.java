package com.android.example.trash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by NaderNabil on 2/13/2017.
 */

public class Profile extends Fragment {
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    TextView name;
    TextView email;
    TextView phone ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        name = (TextView)view.findViewById(R.id.name_text_view);
        email = (TextView)view.findViewById(R.id.email_text_view);
        phone = (TextView)view.findViewById(R.id.phone_text_view);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        get_user_profile();
    }

    public void get_user_profile(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        final String uid = sharedPreferences.getString("uid","14");
        users.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("nader",dataSnapshot.getKey());
                USER user = dataSnapshot.getValue(USER.class);
                name.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                Log.d("nader",user.name);
                Log.d("nader",user.email);
                Log.d("nader",user.phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
