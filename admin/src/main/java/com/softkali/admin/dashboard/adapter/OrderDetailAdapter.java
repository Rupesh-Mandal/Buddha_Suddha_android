package com.softkali.admin.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softkali.admin.R;
import com.softkali.admin.util.OrderDetailOnclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.myViewHolder> {

    Context context;
    JSONArray orderData;
    OrderDetailOnclick orderDetailOnclick;


    public OrderDetailAdapter(Context context, JSONArray orderData, OrderDetailOnclick orderDetailOnclick) {
        this.context = context;
        this.orderData = orderData;
        this.orderDetailOnclick = orderDetailOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_detail_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        try {
            JSONObject jsonObject=orderData.getJSONObject(position);
            holder.name.setText(jsonObject.getString("name"));
            holder.quantity.setText(jsonObject.getString("quantity"));
            holder.rate.setText(jsonObject.getString("rate"));

            double q= Double.parseDouble(jsonObject.getString("quantity"));
            double r= Double.parseDouble(jsonObject.getString("rate"));
            double a=q*r;

            holder.amount.setText(String.valueOf(a));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderData.length();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        TextView name,quantity,rate,amount;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.quantity);
            rate=itemView.findViewById(R.id.rate);
            amount=itemView.findViewById(R.id.amount);
        }
    }
}
