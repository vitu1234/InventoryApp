package com.example.inventoryapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.adapters.recyclers.CategoriesAdapter;
import com.example.inventoryapp.fragments.AddCategoryBottomSheetFragment;
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class ProductCategoryActivity extends AppCompatActivity {

    AppDatabase room_db;
    List<Category> categoryList;

    RecyclerView recyclerView;
    MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        recyclerView = findViewById(R.id.recyclerViewCategories);

        room_db = AppDatabase.getDbInstance(this);
        progressDialog = new MyProgressDialog(this);

        if (room_db.categoryDao().countAllCategorys() > 0) {
            categoryList = room_db.categoryDao().getAllCategorys();
            List<IFlexible> myItems = getDatabaseList();
            FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(myItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);

            recyclerView.setAdapter(adapter);
        } else {
            progressDialog.showDangerAlert("No categories found!");
        }


    }

    public void openAddCategory(View view) {
        AddCategoryBottomSheetFragment bottomSheetFragment = new AddCategoryBottomSheetFragment();
        bottomSheetFragment.setAllowEnterTransitionOverlap(true);
        bottomSheetFragment.show(this.getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public List<IFlexible> getDatabaseList() {
        List<IFlexible> list = new ArrayList<>();


        for (int i = 0; i < categoryList.size(); i++) {
            String category_name = categoryList.get(i).getCategory_name();
            int category_id = categoryList.get(i).getCategory_id();

            list.add(new CategoriesAdapter(String.valueOf(i), category_name, category_id));

        }
        return list;

    }

}