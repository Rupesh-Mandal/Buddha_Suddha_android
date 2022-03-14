package com.softkali.admin.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softkali.admin.R;
import com.softkali.admin.dashboard.model.OrderModel;
import com.softkali.admin.util.OrderAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder> {
    Context context;
    ArrayList<OrderModel> orderModelArrayList;
    OrderAction orderAction;

    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, OrderAction orderAction) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.orderAction = orderAction;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel orderModel = orderModelArrayList.get(position);


        holder.productDeliverAddress.setText(orderModel.getProductDeliverAddress());
        holder.phoneNumber.setText(orderModel.getPhoneNumber());
        holder.totalRate.setText(orderModel.getTotalRate());
        holder.statusMessage.setText(orderModel.getStatusMessage());

        if (orderModel.isPickupBoyStatus()) {
            holder.actionLayout.setVisibility(View.GONE);
        } else {
            holder.actionLayout.setVisibility(View.VISIBLE);
        }

        holder.cancel.setOnClickListener(view -> {
            orderAction.cancel(orderModel);
        });
        holder.setPickup.setOnClickListener(view -> {
            orderAction.setPickup(orderModel);
        });

        holder.itemView.setOnClickListener(view -> {
            orderAction.onDetail(orderModel);
        });


    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView productDeliverAddress, phoneNumber, itemCount, totalRate, statusMessage;
        Button cancel, setPickup;
        LinearLayout actionLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productDeliverAddress = itemView.findViewById(R.id.productDeliverAddress);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            itemCount = itemView.findViewById(R.id.itemCount);
            totalRate = itemView.findViewById(R.id.totalRate);
            statusMessage = itemView.findViewById(R.id.statusMessage);
            cancel = itemView.findViewById(R.id.cancel);
            setPickup = itemView.findViewById(R.id.setPickup);
            actionLayout = itemView.findViewById(R.id.actionLayout);

        }
    }
}
