package com.softkali.buddhasuddha.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.model.AddressBookModel;
import com.softkali.buddhasuddha.utils.AddressBookOnClick;

import java.util.List;

public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.myViewHolder> {
    Context context;
    List<AddressBookModel> addressArrayList;
    AddressBookOnClick addressBookOnClick;

    public AddressBookAdapter(Context context, List<AddressBookModel> addressArrayList, AddressBookOnClick addressBookOnClick) {
        this.context = context;
        this.addressArrayList = addressArrayList;
        this.addressBookOnClick = addressBookOnClick;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.address_item,null,false);
        return new myViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        AddressBookModel addressBookModel=addressArrayList.get(position);
        holder.productDeliverAddress.setText(addressBookModel.getProductDeliverAddress());
        holder.userPhoneNumber.setText(addressBookModel.getUserPhoneNumber());
        holder.itemView.setOnClickListener(view -> {
            addressBookOnClick.onClick(addressBookModel);
        });
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView productDeliverAddress,userPhoneNumber;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productDeliverAddress=itemView.findViewById(R.id.productDeliverAddress);
            userPhoneNumber=itemView.findViewById(R.id.userPhoneNumber);
        }
    }
}
