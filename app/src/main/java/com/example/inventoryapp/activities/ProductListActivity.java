package com.example.inventoryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

public class ProductListActivity extends AppCompatActivity {

    AppDatabase room_db;
    RecyclerView recyclerView;
    int category_id = 0;
    TextView textViewTitle;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recyclerView = findViewById(R.id.recyclerViewProductList);
        textViewTitle = findViewById(R.id.pgTitle);
        room_db = AppDatabase.getDbInstance(this);
        Intent intent = getIntent();
        category_id = intent.getIntExtra("category_id", -1);

        setData();
        initViews();
    }

    private void setData() {
        productList = room_db.productDao().findByProductWithCatId(category_id);

        String category_name = room_db.categoryDao().findByCategoryId(category_id).getCategory_name();
        textViewTitle.setText(category_name + " Products");
//        Log.e("e", productList.size() + " size there!");
    }

    private void initViews() {


        List<IFlexible> myItems = getDatabaseList();
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(myItems);
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

    public void goBakc(View view) {
        onBackPressed();
    }

    public void openAddProduct(View view) {
        Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
        startActivity(intent);
    }
}