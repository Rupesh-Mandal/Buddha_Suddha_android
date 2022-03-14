package com.softkali.buddhasuddha.checkout.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.model.AddressBookModel;
import com.softkali.buddhasuddha.database.Task;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    public static double totalAmount=0;
    public static double totalItem=0;
    public static List<Task> taskForOrder;
    public static AddressBookModel address;

    public static int AddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
    }
}