package com.softkali.buddhasuddha.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.adapter.PaymentListAdapter;
import com.softkali.buddhasuddha.dashboard.model.OrderModel;
import com.softkali.buddhasuddha.database.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrdersDetailsActivity extends AppCompatActivity {
    TextView total;
    RecyclerView payment_recyclerView;
    private static List<Task> taskList = new ArrayList<>();
    private static ProgressDialog progressDialog;
    OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);
        payment_recyclerView=findViewById(R.id.payment_recyclerView);
        total=findViewById(R.id.total);
        payment_recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        String object=getIntent().getStringExtra("orderModel");

        Type type = new TypeToken<OrderModel>(){}.getType();
        orderModel = new Gson().fromJson(object, type);


        Type type2 = new TypeToken<List<Task>>(){}.getType();
        taskList = new Gson().fromJson(orderModel.getOrderData(), type2);


        PaymentListAdapter paymentListAdapter = new PaymentListAdapter(OrdersDetailsActivity.this, taskList);
        payment_recyclerView.setAdapter(paymentListAdapter);
        paymentListAdapter.notifyDataSetChanged();

        double totalAmount=0;
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            double rate = Double.parseDouble(task.getProductRate());
            double quntity = task.getCount();

            double amount = rate * quntity;
            totalAmount = totalAmount + amount;

        }

        total.setText(String.valueOf(totalAmount));

    }
}