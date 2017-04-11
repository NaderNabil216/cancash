package com.android.example.trash;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by NaderNabil on 2/28/2017.
 */

public class create_user_fragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    Button back;
    private String TAG = "Main Login Fragment";
    EditText email;
    EditText password;
    EditText name;
    EditText phone;
    Button register;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_user_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        back = (Button) v.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login) getActivity()).change_to_login();
            }
        });
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//            }
//        };
        email = (EditText) v.findViewById(R.id.email_input);
        password = (EditText) v.findViewById(R.id.password_input);
        name = (EditText) v.findViewById(R.id.name_input);
        phone = (EditText) v.findViewById(R.id.phone_input);
        register = (Button) v.findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || password.getText().toString().equals("") || name.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Creating user");
                    dialog.show();
                    create_user(email.getText().toString().trim(), password.getText().toString().trim(), name.getText().toString().trim(), phone.getText().toString().trim());

                }
            }
        });
        return v;
    }

    public void create_user(final String email, String password, final String name, final String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String s = mAuth.getCurrentUser().getUid();
                            create_user_in_db(email, name, phone, s);

                        }
                    }
                });
    }

    public void create_user_in_db(String email, String name, String phone, String user_id) {
//        String unique_id = UUID.randomUUID().toString();
        USER user = new USER(email, name, phone);
        users.child(user_id).setValue(user);
        Toast.makeText(getActivity(), "user created", Toast.LENGTH_SHORT).show();
        ((Login) getActivity()).change_to_login();

    }

}
