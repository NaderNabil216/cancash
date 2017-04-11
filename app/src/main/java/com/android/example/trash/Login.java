package com.android.example.trash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        change_to_login();
    }


    public void change_to_signup() {
        create_user_fragment create_user_fragment = new create_user_fragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, create_user_fragment).commit();

    }

    public void change_to_forget() {
        forget_password_fragment forget_password_fragment = new forget_password_fragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, forget_password_fragment).commit();

    }

    public void change_to_login() {
        main_login_fragment main_login_fragment = new main_login_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, main_login_fragment).commit();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            Log.d("nader", "if");
        } else {
            Log.d("nader", "else");
            finishAffinity();
            System.exit(0);
        }

    }
}
