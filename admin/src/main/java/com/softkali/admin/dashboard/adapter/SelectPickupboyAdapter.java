package com.softkali.admin.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softkali.admin.R;
import com.softkali.admin.util.SelectPickupboyOnclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectPickupboyAdapter extends RecyclerView.Adapter<SelectPickupboyAdapter.myViewHolder> {

    Context context;
    JSONArray pickupBoyList;
    SelectPickupboyOnclick selectPickupboyOnclick;

    public SelectPickupboyAdapter(Context context, JSONArray pickupBoyList, SelectPickupboyOnclick selectPickupboyOnclick) {
        this.context = context;
        this.pickupBoyList = pickupBoyList;
        this.selectPickupboyOnclick = selectPickupboyOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.pickupboy_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        try {
            JSONObject pickupboy=pickupBoyList.getJSONObject(position);
            holder.name.setText(pickupboy.getString("name"));
            holder.phoneNumber.setText(pickupboy.getString("phoneNumber"));
            holder.itemView.setOnClickListener(view -> {
                selectPickupboyOnclick.onSelect(pickupboy);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return pickupBoyList.length();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView name,phoneNumber;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
        }
    }
}
