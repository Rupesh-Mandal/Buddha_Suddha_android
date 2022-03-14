package com.softkali.buddhasuddha.dashboard.activity;

import static com.softkali.buddhasuddha.utils.Constant.addAddress;
import static com.softkali.buddhasuddha.utils.Constant.getAddressByUserId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.adapter.AddressBookAdapter;
import com.softkali.buddhasuddha.dashboard.model.AddressBookModel;
import com.softkali.buddhasuddha.utils.AddressBookOnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookActivity extends AppCompatActivity {
    Button ad_address_btn;
    private static RecyclerView address_recycler;
    private static ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String userId;
    BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
            userId = jsonObject.getString("userId");
            Log.e("abcd", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        address_recycler = findViewById(R.id.address_recycler);
        ad_address_btn = findViewById(R.id.ad_address_btn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        address_recycler.setLayoutManager(new GridLayoutManager(this, 1));
        loadAddress();
        ad_address_btn.setOnClickListener(v1 -> {
            showAddAddressDailog();
        });
    }


    public void loadAddress() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url = getAddressByUserId;
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<AddressBookModel>>(){}.getType();
                        List<AddressBookModel> addressBookModelList = gson.fromJson(response, type);
                        AddressBookAdapter addressBookAdapter = new AddressBookAdapter(AddressBookActivity.this, addressBookModelList, new AddressBookOnClick() {
                            @Override
                            public void onClick(AddressBookModel addressBookModel) {
                            }
                        });
                        address_recycler.setAdapter(addressBookAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AddressBookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                return params;
            }
        };
        queue.add(sr);
    }

    private void showAddAddressDailog() {
        bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.add_address_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        EditText productDeliverAddress, userPhoneNumber;
        Button addAddress;

        productDeliverAddress = v.findViewById(R.id.productDeliverAddress);
        userPhoneNumber = v.findViewById(R.id.userPhoneNumber);
        addAddress = v.findViewById(R.id.addAddress);

        addAddress.setOnClickListener(view1 -> {
            if (productDeliverAddress.getText().toString().trim().isEmpty()) {
                productDeliverAddress.setError("Please provide valid address");
                productDeliverAddress.requestFocus();
            } else if (userPhoneNumber.getText().toString().trim().isEmpty()) {
                userPhoneNumber.setError("Please provide valid phone number");
                userPhoneNumber.requestFocus();
            } else {
                uploadAddress(productDeliverAddress.getText().toString().trim(), userPhoneNumber.getText().toString().trim());
            }
        });


        bottomSheetDialog.show();

    }

    private void uploadAddress(String productDeliverAddress, String userPhoneNumber) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url = addAddress;
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        bottomSheetDialog.dismiss();
                        loadAddress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        loadAddress();
                        Toast.makeText(AddressBookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("productDeliverAddress", productDeliverAddress);
                params.put("userId", userId);
                params.put("userPhoneNumber", userPhoneNumber);
                return params;
            }
        };
        queue.add(sr);
    }

}