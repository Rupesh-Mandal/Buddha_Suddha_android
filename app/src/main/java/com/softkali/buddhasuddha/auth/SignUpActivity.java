package com.softkali.buddhasuddha.auth;

import static com.softkali.buddhasuddha.utils.Constant.getAllLocation;
import static com.softkali.buddhasuddha.utils.Constant.sign_up_user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.DashboardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    Spinner user_location;
    EditText signup_user_name, signup_user_mobile, signup_password;
    Button btn_signup;
    TextView login_btn;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setContentView(R.layout.activity_sign_up);
        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        initView();

    }

    private void initView() {
        user_location = (Spinner) findViewById(R.id.user_location);
        signup_user_name = findViewById(R.id.signup_user_name);
        signup_user_mobile = findViewById(R.id.signup_user_mobile);
        signup_password = findViewById(R.id.signup_password);
        btn_signup = findViewById(R.id.btn_signup);
        login_btn = findViewById(R.id.login_btn);

        loadLocation();

        login_btn.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });

        btn_signup.setOnClickListener(v -> {
            if (isValid()){
                signUp();
            }
        });


    }

    private void loadLocation() {
        progressDialog.show();
        String URL = getAllLocation;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd",response);

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    ArrayList<String> locationArrayList=new ArrayList<>();

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject locationObject=jsonArray.getJSONObject(i);
                        locationArrayList.add(locationObject.getString("name"));
                    }
                    user_location.setAdapter(new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, locationArrayList));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this,"someting went wrong ", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(SignUpActivity.this,"someting went wrong "+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void signUp() {
        progressDialog.show();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = sign_up_user;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", signup_user_name.getText().toString().trim());
            jsonBody.put("phoneNumber", signup_user_mobile.getText().toString().trim());
            jsonBody.put("password", signup_password.getText().toString().trim());
            jsonBody.put("location", user_location.getSelectedItem().toString());
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("abcd",response);
                    progressDialog.dismiss();
                    JSONObject jsonObject;
                    try {
                        jsonObject=new JSONObject(response);
                        if (jsonObject.getBoolean("status")){
                            Toast.makeText(SignUpActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                            JSONObject userObject=jsonObject.getJSONObject("authUser");
                            editor.putString("authUser",userObject.toString());
                            editor.commit();
                            FirebaseMessaging.getInstance().subscribeToTopic(userObject.getString("userId"));
                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                            finish();
                        }else {
                            Toast.makeText(SignUpActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("abcd",error.toString());
                    Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    boolean isValid() {
        if (signup_user_name.getText().toString().trim().isEmpty()) {
            signup_user_name.setError("Please enter full name");
            signup_user_name.requestFocus();
            return false;

        } else {
            if (signup_user_mobile.getText().toString().trim().isEmpty()) {
                signup_user_mobile.setError("Please enter your mobile number");
                signup_user_mobile.requestFocus();
                return false;

            } else {
                if (signup_password.getText().toString().trim().isEmpty()) {
                    signup_password.setError("Please enter password");
                    signup_password.requestFocus();
                    return false;

                } else {
                    return true;
                }
            }

        }

    }
}