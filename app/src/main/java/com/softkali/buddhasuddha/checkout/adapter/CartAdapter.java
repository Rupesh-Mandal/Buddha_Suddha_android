package com.softkali.buddhasuddha.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.database.Task;
import com.softkali.buddhasuddha.database.TaskOpration;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myViewHolder> {
    Context context;
    List<Task> taskList;
    TaskOpration taskOpration;


    public CartAdapter(Context context, List<Task> taskList, TaskOpration taskOpration) {
        this.context = context;
        this.taskList = taskList;
        this.taskOpration = taskOpration;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item,null,false);
        return new myViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Task task=taskList.get(position);
        double r1= Double.parseDouble(task.getProductRate());
        double c1 = task.getCount();

        double rate=r1*c1;
        Glide.with(context).load(task.getProductImageLink()).into(holder.item_img);

        holder.item_title.setText(task.getProductName());
        holder.item_rate.setText(String.valueOf(rate));
        holder.count.setText(String.valueOf(task.getCount()));
        holder.productDescription.setText(task.getProductDescription());

        holder.count_negative.setOnClickListener(view -> {
            double r= Double.parseDouble(task.getProductRate());
            double c = task.getCount();

            if (c>1){
                int i= (int) (c-1);

                holder.count.setText(String.valueOf(i));
                holder.item_rate.setText(String.valueOf((c-1)*r));

                task.setCount((int) (c-1));
                taskOpration.upDate(task);
            }
        });

        holder.count_positive.setOnClickListener(view1 -> {
            double r= Double.parseDouble(task.getProductRate());
            double c = task.getCount();

            int i= (int) (c+1);
            holder.count.setText(String.valueOf(i));
            holder.item_rate.setText(String.valueOf((c+1)*r));

            task.setCount((int) (c+1));
            taskOpration.upDate(task);
        });
        holder.cart_delet.setOnClickListener(view -> {
            taskOpration.Delet(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img,cart_delet;
        ImageView count_negative,count_positive;
        TextView item_title,item_rate,count,productDescription;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img=itemView.findViewById(R.id.item_img);
            cart_delet=itemView.findViewById(R.id.cart_delet);
            item_title=itemView.findViewById(R.id.item_title);
            item_rate=itemView.findViewById(R.id.item_rate);
            count_negative=itemView.findViewById(R.id.count_negative);
            count=itemView.findViewById(R.id.count);
            count_positive=itemView.findViewById(R.id.count_positive);
            productDescription=itemView.findViewById(R.id.productDescription);
        }
    }
}
