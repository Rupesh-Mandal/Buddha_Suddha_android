package com.softkali.buddhasuddha.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.model.CategoriesModel;
import com.softkali.buddhasuddha.utils.CategoryOnClick;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.myViewHolder>{

    Context context;
    ArrayList<CategoriesModel> categoriesModelArrayList;
    CategoryOnClick categoryOnClick;

    public CategoryAdapter(Context context, ArrayList<CategoriesModel> categoriesModelArrayList, CategoryOnClick categoryOnClick) {
        this.context = context;
        this.categoriesModelArrayList = categoriesModelArrayList;
        this.categoryOnClick = categoryOnClick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.categories_item,null,false);
        return new myViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CategoriesModel categoriesModel=categoriesModelArrayList.get(position);
        holder.categories_title.setText(categoriesModel.getName());
        holder.itemView.setOnClickListener(view -> {
            categoryOnClick.onClick(categoriesModel);
        });

    }


    @Override
    public int getItemCount() {
        return categoriesModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView categories_title;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            categories_title=itemView.findViewById(R.id.categories_title);
        }
    }
}
