package com.example.inventoryapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.inventoryapp.R;
import com.example.inventoryapp.fragments.DashboardFragment;
import com.example.inventoryapp.fragments.ProductsMainFragment;
import com.example.inventoryapp.models.User;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.storage.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    //    ?DRAWER MENU
    static final float END_SCALE = 0.7f;
    NavigationView navigationView;
    ImageView menu_icon;
    LinearLayout contentView;
    TextView textVietitle;
    SharedPrefManager sharedPrefManagera;
    BottomNavigationBar bottomNavigationBar;

    List<User> userList;

    AppDatabase room_db;


    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        room_db = AppDatabase.getDbInstance(this);
        sharedPrefManagera = new SharedPrefManager(getApplicationContext());

        swipeRefreshLayout = findViewById(R.id.swipeHome);
        swipeRefreshLayout.setSoundEffectsEnabled(true);
        contentView = findViewById(R.id.content);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), null).commit();

        bottomNavigationBar
                .setActiveColor(R.color.blue_200)
                .setInActiveColor(R.color.black)
                .setBarBackgroundColor(R.color.white);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_icon, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.products_icon, "Products"))
                .addItem(new BottomNavigationItem(R.drawable.folder_icon, "Categories"))
                .addItem(new BottomNavigationItem(R.drawable.settings_icon, "Settings"))
                .addItem(new BottomNavigationItem(R.drawable.logout_icon, "Logout"))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), null).commit();
                } else if (position == 1) {
                    displayFragment(new ProductsMainFragment());
                } else if (position == 2) {
                    Toast.makeText(DashboardActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                } else if (position == 3) {
                    Toast.makeText(DashboardActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                }else if (position == 4) {
                    Toast.makeText(DashboardActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    //hooking fragments
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit();
    }
}