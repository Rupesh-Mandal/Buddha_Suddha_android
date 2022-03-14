package com.softkali.admin.auth;

import static com.softkali.admin.util.Constant.AdminSignin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import com.softkali.admin.dashboard.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    EditText login_user_mobile, login_password;

    AppCompatButton loginBtn;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private  static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loginBtn = findViewById(R.id.login_btn_signin);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        login_user_mobile = findViewById(R.id.login_user_mobile);
        login_password = findViewById(R.id.login_password);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        loginBtn.setOnClickListener(view -> {
            if (isValid()) {
                checkLogin(login_user_mobile.getText().toString().trim(), login_password.getText().toString().trim());
            }
        });

    }
    private void checkLogin(String MobileNumber, String Password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=AdminSignin;

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                JSONObject admin=new JSONObject();
                                admin.put("mobileNumber",MobileNumber);
                                admin.put("password",Password);
                                editor.putString("admin",admin.toString());
                                editor.commit();
                                startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                                finish();
                            }else {
                                Toast.makeText(SignInActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("abcd",e.getMessage());
                            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

    boolean isValid() {
        if (login_user_mobile.getText().toString().trim().isEmpty()) {
            login_user_mobile.setError("Please enter mobile number");
            login_user_mobile.requestFocus();
            return false;

        } else {
            if (login_password.getText().toString().trim().isEmpty()) {
                login_password.setError("Please enter Password");
                login_password.requestFocus();
                return false;

            } else {
                return true;
            }
        }
    }

}