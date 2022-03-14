package com.softkali.admin.dashboard;

import static com.softkali.admin.dashboard.fragment.AllProductFragment.loadProduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.softkali.admin.R;

import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    boolean isReady = false;

    public static final String location="Lahan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        bottomNavigation = findViewById(R.id.meowBottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_format_list_bulleted_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_sensor_window_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_storefront_24));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        navController.navigate(R.id.allProductFragment);
                        break;
                    case 2:
                        navController.navigate(R.id.orderFragment);
                        break;
                    case 3:
                        navController.navigate(R.id.profileFragment);
                        break;
                }
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.e("abcd", String.valueOf(destination.getId()));
                switch (destination.getId()) {
                    case R.id.allProductFragment:
                        if (isReady){
                            bottomNavigation.show(1, true);
                        }
                        break;
                    case R.id.orderFragment:
                        if (isReady) {
                            bottomNavigation.show(2, true);
                        }
                        break;
                    case R.id.profileFragment:
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
        bottomNavigation.show(2, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            loadProduct(this);
        }
    }

}