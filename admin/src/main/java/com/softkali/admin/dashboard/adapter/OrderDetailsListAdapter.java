package com.softkali.admin.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softkali.admin.R;
import com.softkali.admin.dashboard.model.Task;

import java.util.List;


public class OrderDetailsListAdapter extends RecyclerView.Adapter<OrderDetailsListAdapter.myViewHolder> {

    Context context;
    List<Task> taskList;

    public OrderDetailsListAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_details_list_item,null,false);
        return new myViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Task task=taskList.get(position);
        holder.name.setText(task.getProductName());
        holder.quantity.setText(String.valueOf(task.getCount()));
        holder.rate.setText(task.getProductRate());

        double q=task.getCount();
        double r= Double.parseDouble(task.getProductRate());
        double a=q*r;

        holder.amount.setText(String.valueOf(a));

    }

    @Override
    public int getItemCount() {
        return taskList.size();
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
