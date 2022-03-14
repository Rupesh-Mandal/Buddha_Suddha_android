package com.softkali.buddhasuddha.dashboard.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.activity.AddressBookActivity;
import com.softkali.buddhasuddha.dashboard.activity.OrderListActivity;


public class MeFragment extends Fragment {

    LinearLayout orderBtn, addressBookBtn, about_us, contact_us, privacy_policy;
    View view;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        initView();
    }

    private void initView() {
        orderBtn=view.findViewById(R.id.orderBtn);
        addressBookBtn=view.findViewById(R.id.addressBookBtn);
        about_us=view.findViewById(R.id.about_us);
        contact_us=view.findViewById(R.id.contact_us);
        privacy_policy=view.findViewById(R.id.privacy_policy);

        addressBookBtn.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), AddressBookActivity.class));
        });
        orderBtn.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), OrderListActivity.class));
        });
    }
}