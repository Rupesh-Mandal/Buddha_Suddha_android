package com.softkali.buddhasuddha.dashboard.activity;

import static com.softkali.buddhasuddha.utils.Constant.findOrderByUserId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.adapter.AddressBookAdapter;
import com.softkali.buddhasuddha.dashboard.adapter.OrderAdapter;
import com.softkali.buddhasuddha.dashboard.model.AddressBookModel;
import com.softkali.buddhasuddha.dashboard.model.OrderModel;
import com.softkali.buddhasuddha.utils.AddressBookOnClick;
import com.softkali.buddhasuddha.utils.OrdersOnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {
    private static ProgressDialog progressDialog;

    RecyclerView order_recycler_view;
    SharedPreferences sharedpreferences;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
            userId = jsonObject.getString("userId");
            Log.e("abcd", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        order_recycler_view=findViewById(R.id.order_recycler_view);

        loadOrder();
    }

    private void loadOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url = findOrderByUserId;
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
                        Toast.makeText(OrderListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void setOrder(ArrayList<OrderModel> orderModelList) {
        OrderAdapter orderAdapter=new OrderAdapter(this, orderModelList, new OrdersOnClick() {
            @Override
            public void onClick(OrderModel orderModel) {
                Intent intent=new Intent(OrderListActivity.this,OrdersDetailsActivity.class);
                intent.putExtra("orderModel",new Gson().toJson(orderModel));
                startActivity(intent);
            }

            @Override
            public void onCancel(OrderModel orderModel) {
                cancelOrder(orderModel);
            }
        });
        order_recycler_view.setLayoutManager(new GridLayoutManager(this,1));
        order_recycler_view.setAdapter(orderAdapter);
    }

    private void cancelOrder(OrderModel orderModel) {
    }
}