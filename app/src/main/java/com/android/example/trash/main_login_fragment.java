package com.android.example.trash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by NaderNabil on 2/28/2017.
 */

public class main_login_fragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "Main Login Fragment";
    EditText emailET;
    EditText passET;
    Button btn_login;
    ProgressDialog dialog;
    TextView create_user;
    TextView link_forget_password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_login_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        emailET = (EditText) view.findViewById(R.id.input_email);
        passET = (EditText) view.findViewById(R.id.input_password);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        create_user = (TextView) view.findViewById(R.id.link_signup);
        create_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login)getActivity()).change_to_signup();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailS = emailET.getText().toString().trim();
                String passS = passET.getText().toString().trim();
                dialog = new ProgressDialog(getContext());
                dialog.setMessage("Loging in");
                dialog.show();
                sign_in(emailS, passS);
            }
        });
        link_forget_password = (TextView)view.findViewById(R.id.link_forget_password);
        link_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login)getActivity()).change_to_forget();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void sign_in(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                           save_to_pref(mAuth.getCurrentUser().getUid());
                            Toast.makeText(getActivity(), "User signed",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });
    }
    public void save_to_pref(String uid){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged",true);
        editor.putString("uid",uid);
        editor.apply();

    }


}
