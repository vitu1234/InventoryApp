package com.example.inventoryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.inventoryapp.R;
import com.example.inventoryapp.common.LoginActivity;
import com.example.inventoryapp.fragments.DashboardFragment;
import com.example.inventoryapp.fragments.ProductsMainFragment;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.storage.SharedPrefManager;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout contentView;
    SharedPrefManager sharedPrefManagera;
    BottomNavigationBar bottomNavigationBar;
    AppDatabase room_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        room_db = AppDatabase.getDbInstance(this);
        sharedPrefManagera = new SharedPrefManager(getApplicationContext());

        contentView = findViewById(R.id.content);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), null).commit();

        bottomNavigationBar
                .setActiveColor(R.color.blue_200)
                .setInActiveColor(R.color.black)
                .setBarBackgroundColor(R.color.white);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_icon, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.folder_icon, "Categories"))
                .addItem(new BottomNavigationItem(R.drawable.products_icon, "Products"))
                .addItem(new BottomNavigationItem(R.drawable.settings_icon, "Settings"))
                .addItem(new BottomNavigationItem(R.drawable.logout_icon, "Logout"))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), null).commit();
                } else if (position == 2) {
                    displayFragment(new ProductsMainFragment());
                } else if (position == 1) {
                    startActivity(new Intent(DashboardActivity.this, ProductCategoryActivity.class));
                } else if (position == 3) {
                    startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
                } else if (position == 4) {
                    logoutUser();
                }


            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });


    }

    private void logoutUser() {
        sharedPrefManagera.logoutUser();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    //hooking fragments
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().remove(new DashboardFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), null).commit();
        bottomNavigationBar.selectTab(0);
    }
}