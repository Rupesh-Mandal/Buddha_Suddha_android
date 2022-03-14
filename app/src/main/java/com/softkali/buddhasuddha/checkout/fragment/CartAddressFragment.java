package com.softkali.buddhasuddha.checkout.fragment;


import static com.softkali.buddhasuddha.checkout.activity.CartActivity.address;
import static com.softkali.buddhasuddha.utils.Constant.addAddress;
import static com.softkali.buddhasuddha.utils.Constant.getAddressByUserId;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.adapter.AddressBookAdapter;
import com.softkali.buddhasuddha.dashboard.model.AddressBookModel;
import com.softkali.buddhasuddha.utils.AddressBookOnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartAddressFragment extends Fragment {

    View view;


    Button ad_address_btn;
    private static RecyclerView address_recycler;
    private static NavController navigate;
    private static ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String userId;
    BottomSheetDialog bottomSheetDialog;
    public CartAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
            userId = jsonObject.getString("userId");
            Log.e("abcd", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        address_recycler = view.findViewById(R.id.address_recycler);
        ad_address_btn = view.findViewById(R.id.ad_address_btn);
        navigate = Navigation.findNavController(view);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");
        address_recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        loadAddress();
        ad_address_btn.setOnClickListener(v1 -> {
            showAddAddressDailog();
        });
    }

    public void loadAddress() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        AddressBookAdapter addressBookAdapter = new AddressBookAdapter(getContext(), addressBookModelList, new AddressBookOnClick() {
                            @Override
                            public void onClick(AddressBookModel addressBookModel) {
                                address=addressBookModel;
                                navigate.navigate(R.id.action_cartAddressFragment_to_cartPaymentFragment);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(getContext()).
                inflate(R.layout.add_address_bottomsheet, (ConstraintLayout) view.findViewById(R.id.bottom_sheet_layout));
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = addAddress;
        ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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