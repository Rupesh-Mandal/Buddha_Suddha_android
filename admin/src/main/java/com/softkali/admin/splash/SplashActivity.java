package com.softkali.admin.splash;

import static com.softkali.admin.util.Constant.AdminSignin;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.admin.R;
import com.softkali.admin.auth.SignInActivity;
import com.softkali.admin.auth.model.AuthUser;
import com.softkali.admin.dashboard.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        object=sharedpreferences.getString("admin","");

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
                    AuthUser authUser=new Gson().fromJson(object, new TypeToken<AuthUser>() {}.getType());
                    checkLogin(authUser.getMobileNumber(),authUser.getPassword());
                }
            }
        }, SPLASH_TIME);
    }


    private void checkLogin(String MobileNumber, String Password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=AdminSignin;

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                JSONObject admin=new JSONObject();
                                admin.put("mobileNumber",MobileNumber);
                                admin.put("password",Password);
                                editor.putString("admin",admin.toString());
                                editor.commit();
                                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                                finish();
                            }else {
                                Intent mySuperIntent = new Intent(SplashActivity.this, SignInActivity.class);
                                startActivity(mySuperIntent);
                                finish();                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("abcd",e.getMessage());
                            Intent mySuperIntent = new Intent(SplashActivity.this, SignInActivity.class);
                            startActivity(mySuperIntent);
                            finish();                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        Intent mySuperIntent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(mySuperIntent);
                        finish();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("mobileNumber",MobileNumber);
                params.put("password",Password);
                return params;
            }
        };
        queue.add(sr);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(3000)
                .start();
    }
}