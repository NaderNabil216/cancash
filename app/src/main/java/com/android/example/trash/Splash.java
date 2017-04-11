package com.android.example.trash;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.model.ConfigSplash;


public class Splash extends AwesomeSplash {

    //by nader 12/11/2016

    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setLogoSplash(R.drawable.logomin); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInUp); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        configSplash.setBackgroundColor(R.color.colorPrimaryDark);

        configSplash.setTitleSplash("CanCash");
        configSplash.setTitleTextColor(android.R.color.white);
        configSplash.setTitleTextSize(35f); //float value
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInDown);
    }

    @Override
    public void animationsFinished() {
        SharedPreferences sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE);
        boolean state = sharedPreferences.getBoolean("is_logged", false);
        if (state) {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}