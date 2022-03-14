package com.softkali.buddhasuddha.dashboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.model.OrderModel;
import com.softkali.buddhasuddha.database.Task;
import com.softkali.buddhasuddha.utils.OrdersOnClick;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder> {

    Context context;
    ArrayList<OrderModel> orderModelArrayList;
    OrdersOnClick ordersOnClick;

    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, OrdersOnClick ordersOnClick) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.ordersOnClick = ordersOnClick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.oder_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel order=orderModelArrayList.get(position);

        Log.e("abcd", String.valueOf(position));
        if (!order.getStatus().equals("1")){
            holder.cancle_btn.setVisibility(View.GONE);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Task> taskArrayList = gson.fromJson(order.getOrderData(), type);


        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0;i<taskArrayList.size();i++){
            stringBuilder.append(taskArrayList.get(i).getProductName()+"\n");
        }
        holder.items.setText(stringBuilder.toString());

        holder.count.setText(taskArrayList.size()+" Items");
        holder.date.setText(order.getCreatedTime());
        holder.status.setText(order.getStatusMessage());

        holder.cancle_btn.setOnClickListener(v -> {
            ordersOnClick.onCancel(order);
        });
        holder.itemView.setOnClickListener(view -> {
            ordersOnClick.onClick(order);
        });

    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView count,date,items,status,cancle_btn;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            count=itemView.findViewById(R.id.count);
            date=itemView.findViewById(R.id.date);
            items=itemView.findViewById(R.id.items);
            status=itemView.findViewById(R.id.status);
            cancle_btn=itemView.findViewById(R.id.cancle_btn);
        }
    }
}
