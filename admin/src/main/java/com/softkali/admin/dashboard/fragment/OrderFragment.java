package com.softkali.admin.dashboard.fragment;

import static com.softkali.admin.dashboard.DashboardActivity.location;
import static com.softkali.admin.util.Constant.AdminSignin;
import static com.softkali.admin.util.Constant.cancelOrderBySeller;
import static com.softkali.admin.util.Constant.findByLocation;
import static com.softkali.admin.util.Constant.getAllPickupBoy;
import static com.softkali.admin.util.Constant.setPickup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.admin.R;
import com.softkali.admin.auth.SignInActivity;
import com.softkali.admin.dashboard.DashboardActivity;
import com.softkali.admin.dashboard.activity.OrderDetailActivity;
import com.softkali.admin.dashboard.activity.OrdersDetailsActivity;
import com.softkali.admin.dashboard.adapter.OrderAdapter;
import com.softkali.admin.dashboard.adapter.SelectPickupboyAdapter;
import com.softkali.admin.dashboard.model.OrderModel;
import com.softkali.admin.util.OrderAction;
import com.softkali.admin.util.SelectPickupboyOnclick;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderFragment extends Fragment {
    View view;
    RecyclerView orderRecycler;


    private static ProgressDialog progressDialog;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        initView();
    }

    private void initView() {
        orderRecycler = view.findViewById(R.id.orderRecycler);
        orderRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        loadOrder();
    }

    private void loadOrder() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = findByLocation;

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<OrderModel>>(){}.getType();
                        ArrayList<OrderModel> orderModelList = gson.fromJson(response, type);
                        setOrder(orderModelList);

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
                params.put("location", location);
                return params;
            }
        };
        queue.add(sr);
    }

    private void setOrder(ArrayList<OrderModel> orderModelList) {

        OrderAdapter orderAdapter = new OrderAdapter(getContext(), orderModelList, new OrderAction() {
            @Override
            public void cancel(OrderModel orderModel) {
                orderAction(orderModel, cancelOrderBySeller);
            }

            @Override
            public void setPickup(OrderModel orderModel) {
                loadPickupBoy(orderModel);

            }

            @Override
            public void onDetail(OrderModel orderModel) {
                Intent intent=new Intent(getContext(), OrdersDetailsActivity.class);
                intent.putExtra("orderModel",new Gson().toJson(orderModel));
                startActivity(intent);
            }


        });
        orderRecycler.setAdapter(orderAdapter);
    }

    private void loadPickupBoy(OrderModel orderModel) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getAllPickupBoy;

        ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                            selectPickupBoyDailog(orderModel, pickupBoyList);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void selectPickupBoyDailog(OrderModel orderModel, JSONArray pickupBoyList) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(getContext()).
                inflate(R.layout.select_picupboy_dailog, (ConstraintLayout) view.findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        RecyclerView selectPicupboyRecycler = v.findViewById(R.id.selectPicupboyRecycler);
        selectPicupboyRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        SelectPickupboyAdapter selectPickupboyAdapter = new SelectPickupboyAdapter(getContext(), pickupBoyList, new SelectPickupboyOnclick() {
            @Override
            public void onSelect(JSONObject pickupboy) {
                try {
                    orderModel.setPickupBoyId(pickupboy.getString("pickupBoyId"));
                    orderAction(orderModel, setPickup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        selectPicupboyRecycler.setAdapter(selectPickupboyAdapter);
        bottomSheetDialog.show();
    }

    private void orderAction(OrderModel orderModel, String url) {
        progressDialog.show();
        String URL = url;

        final String mRequestBody = new Gson().toJson(orderModel);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(getContext(), jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        loadOrder();
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong " + e.toString(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
    }
}