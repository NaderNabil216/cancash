package com.android.example.trash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by NaderNabil on 2/28/2017.
 */

public class forget_password_fragment extends Fragment {
    EditText email;
    Button back;
    Button send_email;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forget_password_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        email = (EditText) v.findViewById(R.id.email);
        back = (Button) v.findViewById(R.id.btn_back);
        send_email = (Button) v.findViewById(R.id.btn_reset_password);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login) getActivity()).change_to_login();
            }
        });
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please fill email ", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email.getText().toString().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return v;
    }
}
