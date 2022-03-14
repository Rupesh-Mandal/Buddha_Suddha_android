package com.softkali.admin.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softkali.admin.R;
import com.softkali.admin.dashboard.model.Product;
import com.softkali.admin.util.ProductOnclick;


import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    ProductOnclick productOnclick;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, ProductOnclick productOnclick) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.productOnclick = productOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product product=productArrayList.get(position);
        Glide.with(context).load(product.getProductImageLink()).into(holder.productImageLink);

        holder.productName.setText(product.getProductName());
        holder.productDescription.setText(product.getProductDescription());
        holder.productRate.setText(product.getProductRate());

        holder.deletBtn.setOnClickListener(view -> {
            productOnclick.onDelet(product);
        });
        holder.editBtn.setOnClickListener(view -> {
            productOnclick.onEdit(product);
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink,deletBtn,editBtn;
        TextView productRate,productDescription,productName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink=itemView.findViewById(R.id.productImageLink);
            deletBtn=itemView.findViewById(R.id.deletBtn);
            editBtn=itemView.findViewById(R.id.editBtn);
            productRate=itemView.findViewById(R.id.productRate);
            productDescription=itemView.findViewById(R.id.productDescription);
            productName=itemView.findViewById(R.id.productName);

        }
    }
}
