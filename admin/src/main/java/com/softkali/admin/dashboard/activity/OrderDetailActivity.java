package com.softkali.admin.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.admin.R;
import com.softkali.admin.dashboard.adapter.OrderAdapter;
import com.softkali.admin.dashboard.adapter.OrderDetailAdapter;
import com.softkali.admin.util.OrderAction;
import com.softkali.admin.util.OrderDetailOnclick;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity {

    JSONObject orderData;

    RecyclerView orderDataRecycler;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        try {
            orderData=new JSONObject(getIntent().getStringExtra("orderData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws JSONException {
        orderDataRecycler=findViewById(R.id.orderDataRecycler);
        total=findViewById(R.id.total);

        orderDataRecycler.setLayoutManager(new GridLayoutManager(this,1));

        OrderDetailAdapter orderDetailAdapter=new OrderDetailAdapter(this, orderData.getJSONArray("orderData"), new OrderDetailOnclick() {
            @Override
            public void onClick(JSONObject orderModel) {

            }
        });
        orderDataRecycler.setAdapter(orderDetailAdapter);
        total.setText(orderData.getString("totalRate"));
    }
}