package com.softkali.admin.dashboard.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.softkali.admin.R;
import com.softkali.admin.dashboard.activity.PickupBoyListActivity;

public class ProfileFragment extends Fragment {

    LinearLayout pickupBoyList;
    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view=v;
        initView();
    }

    private void initView() {
        pickupBoyList=view.findViewById(R.id.pickupBoyList);
        pickupBoyList.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), PickupBoyListActivity.class));
        });
    }
}