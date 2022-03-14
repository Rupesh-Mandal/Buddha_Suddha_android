package com.softkali.buddhasuddha.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.auth.SignInActivity;
import com.softkali.buddhasuddha.dashboard.DashboardActivity;

public class SplashActivity extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 1500; //This is 3 seconds
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    String object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        object=sharedpreferences.getString("authUser","");

        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        //Code to start timer and take action after the timer ends
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (object.trim().isEmpty()){
                    Intent mySuperIntent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();

                }
            }
        }, SPLASH_TIME);
    }
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(3000)
                .start();
    }
}