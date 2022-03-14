package com.softkali.buddhasuddha.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.model.Product;
import com.softkali.buddhasuddha.utils.UserProductOnClick;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder>{

    Context context;
    ArrayList<Product> productArrayList;
    UserProductOnClick userProductOnClick;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, UserProductOnClick userProductOnClick) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.userProductOnClick = userProductOnClick;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_product_item, null, false);
        return new myViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productRate.setText("Rs." + product.getProductRate());
        Glide.with(context).load(product.getProductImageLink()).into(holder.productImageLink);
        holder.itemView.setOnClickListener(v -> {
            userProductOnClick.onclick(product);
        });
        holder.add_to_cart_btn.setOnClickListener(view -> {
            userProductOnClick.addToCartOnclick(product);
        });


        if (position == productArrayList.size() - 1) {
            int paddingDp = 80;
            float density = context.getResources().getDisplayMetrics().density;
            int paddingPixel = (int)(paddingDp * density);
            holder.itemView.setPadding(0, 0, 0, paddingPixel);
        }
    }


    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink;
        TextView productName, productRate;
        Button add_to_cart_btn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink = itemView.findViewById(R.id.productImageLink);
            productName = itemView.findViewById(R.id.productName);
            productRate = itemView.findViewById(R.id.productRate);
            add_to_cart_btn = itemView.findViewById(R.id.add_to_cart_btn);
        }
    }

}
