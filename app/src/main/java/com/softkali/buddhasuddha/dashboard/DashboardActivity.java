package com.softkali.buddhasuddha.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.activity.CartActivity;

public class DashboardActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    boolean isReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        bottomNavigation = findViewById(R.id.meowBottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.me));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        navController.navigate(R.id.home);
                        break;
                    case 2:
                        startActivity(new Intent(DashboardActivity.this, CartActivity.class));
                        break;
                    case 3:
                        navController.navigate(R.id.me);
                        break;
                }
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.e("abcd", String.valueOf(destination.getId()));
                switch (destination.getId()) {
                    case R.id.home:
                        if (isReady){
                            bottomNavigation.show(1, true);
                        }
                        break;
                    case R.id.cart:
                        if (isReady) {
                            bottomNavigation.show(2, true);
                        }
                        break;
                    case R.id.me:
                        if (isReady){
                            bottomNavigation.show(3, true);
                        }
                        break;
                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
                isReady = true;
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });
        bottomNavigation.show(1, true);
    }
}