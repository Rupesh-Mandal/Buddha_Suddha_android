package com.softkali.buddhasuddha.auth;

import static com.softkali.buddhasuddha.utils.Constant.sign_in_user;
import static com.softkali.buddhasuddha.utils.Constant.sign_up_user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignInActivity extends AppCompatActivity {
    AppCompatButton loginBtn;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private  static ProgressDialog progressDialog;

    TextView login_btn_signup;
    EditText login_user_mobile, login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        loginBtn = findViewById(R.id.login_btn_signin);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        login_user_mobile = findViewById(R.id.login_user_mobile);
        login_password = findViewById(R.id.login_password);
        login_btn_signup = findViewById(R.id.login_btn_signup);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        login_btn_signup.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            finish();
        });
        loginBtn.setOnClickListener(view -> {
            if (isValid()) {
                checkLogin(login_user_mobile.getText().toString().trim(), login_password.getText().toString().trim());
            }
        });
    }
    private void checkLogin(String MobileNumber, String Password) {
        progressDialog.show();
        try {
            String url=sign_in_user;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("phoneNumber", MobileNumber);
            jsonBody.put("password", Password);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject=new JSONObject(response);

                        if (jsonObject.getBoolean("status")){
                            Toast.makeText(SignInActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                            JSONObject userObject=jsonObject.getJSONObject("authUser");
                            editor.putString("authUser",userObject.toString());
                            editor.commit();
                            FirebaseMessaging.getInstance().subscribeToTopic(userObject.getString("userId"));
                            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                            finish();
                        }else {
                            Toast.makeText(SignInActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignInActivity.this, "something went worng error= "+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("abcd",e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "something went worng error= "+error.toString(), Toast.LENGTH_SHORT).show();

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
            progressDialog.dismiss();
            Toast.makeText(this, "something went worng", Toast.LENGTH_SHORT).show();
        }
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