package com.softkali.admin.dashboard.activity;

import static com.softkali.admin.dashboard.DashboardActivity.location;
import static com.softkali.admin.util.Constant.getAllPickupBoy;
import static com.softkali.admin.util.Constant.setPickup;
import static com.softkali.admin.util.Constant.sign_up_pickup_boy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softkali.admin.R;
import com.softkali.admin.dashboard.adapter.SelectPickupboyAdapter;
import com.softkali.admin.dashboard.model.OrderModel;
import com.softkali.admin.util.SelectPickupboyOnclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PickupBoyListActivity extends AppCompatActivity {
    RecyclerView selectPicupboyRecycler;
    ImageView addPickupBoy;
//    BottomSheetDialog bottomSheetDialog;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_boy_list);
        addPickupBoy =findViewById(R.id.addPickupBoy);
        selectPicupboyRecycler =findViewById(R.id.selectPicupboyRecycler);
        selectPicupboyRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        loadPickupBoy();

        addPickupBoy.setOnClickListener(view -> {
            addPickupBoyDailog();
        });
    }

    private void addPickupBoyDailog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PickupBoyListActivity.this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(PickupBoyListActivity.this).
                inflate(R.layout.add_pickupboy_bottomsheet, null,false);
        bottomSheetDialog.setContentView(v);
        EditText signup_user_name, userPhoneNumber,signup_password;
        Button addPickupboy;

        signup_user_name = v.findViewById(R.id.signup_user_name);
        signup_password = v.findViewById(R.id.signup_password);
        userPhoneNumber = v.findViewById(R.id.userPhoneNumber);
        addPickupboy = v.findViewById(R.id.addPickupboy);

        addPickupboy.setOnClickListener(view1 -> {
            if (signup_user_name.getText().toString().trim().isEmpty()) {
                signup_user_name.setError("Please provide Name");
                signup_user_name.requestFocus();
            } else if (userPhoneNumber.getText().toString().trim().isEmpty()) {
                userPhoneNumber.setError("Please provide valid phone number");
                userPhoneNumber.requestFocus();
            } else if (signup_password.getText().toString().trim().isEmpty()){
                signup_password.setError("Please provide password");
                signup_password.requestFocus();
            }else {
                signUpPickupBoy(signup_user_name.getText().toString().trim(),userPhoneNumber.getText().toString().trim(),signup_password.getText().toString().trim());
            }
        });


        bottomSheetDialog.show();

    }

    private void signUpPickupBoy(String name, String phoneNumber, String password) {
        progressDialog.show();
        try {
            String url=sign_up_pickup_boy;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("phoneNumber", phoneNumber);
            jsonBody.put("password", password);
            jsonBody.put("location", location);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
//                    bottomSheetDialog.dismiss();
                    loadPickupBoy();

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(PickupBoyListActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(PickupBoyListActivity.this, "something went worng error= "+error.toString(), Toast.LENGTH_SHORT).show();
//                    bottomSheetDialog.dismiss();

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
//            bottomSheetDialog.dismiss();

            Toast.makeText(this, "something went worng", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadPickupBoy() {
        RequestQueue queue = Volley.newRequestQueue(PickupBoyListActivity.this);
        String Url = getAllPickupBoy;

        ProgressDialog progressDialog = new ProgressDialog(PickupBoyListActivity.this);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        try {
                            JSONArray pickupBoyList = new JSONArray(response);
                            selectPickupBoyDailog(pickupBoyList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(PickupBoyListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location", location);
                return params;
            }
        };
        queue.add(sr);
    }

    private void selectPickupBoyDailog(JSONArray pickupBoyList) {
        SelectPickupboyAdapter selectPickupboyAdapter = new SelectPickupboyAdapter(PickupBoyListActivity.this, pickupBoyList, new SelectPickupboyOnclick() {
            @Override
            public void onSelect(JSONObject pickupboy) {

            }
        });
        selectPicupboyRecycler.setAdapter(selectPickupboyAdapter);
    }

}