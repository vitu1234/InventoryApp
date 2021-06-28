package com.example.inventoryapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.adapters.recyclers.ProductListAdapter;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.room_db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class SearchProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    List<Product> productList;
    AppDatabase room_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        room_db = AppDatabase.getDbInstance(this);

        recyclerView = findViewById(R.id.recyclerProducts);
        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getProducts(query);
                searchView.setSubmitButtonEnabled(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getProducts(String query) {
        productList = room_db.productDao().searchByProductName(query);
        Log.e("size", productList.size() + " thats size");
        if (productList.size() > 0) {
            initViews();
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "No products found, try changing search query!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sGoBack(View view) {
        onBackPressed();
    }

    private void initViews() {

//        recyclerView.notifyAll();

        List<IFlexible> myItems = getDatabaseList();
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(myItems);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        recyclerView.setAdapter(adapter);
    }

    public List<IFlexible> getDatabaseList() {
        List<IFlexible> list = new ArrayList<>();

        for (int i = 0; i < productList.size(); i++) {
            String product_name = productList.get(i).getProduct_name();
            int product_id = productList.get(i).getProduct_id();
            int quantity = productList.get(i).getQuantity();
            double product_price = productList.get(i).getPrice();

            list.add(new ProductListAdapter(String.valueOf(i), product_name, product_id, quantity, product_price));

        }
        return list;

    }
}