package com.example.inventoryapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.activities.AddProductActivity;
import com.example.inventoryapp.adapters.recyclers.CategoriesProductsListAdapter;
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.room_db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


public class ProductsMainFragment extends Fragment {
    ImageView imageViewScan;
    RecyclerView recyclerView;
    AppDatabase room_db;
    List<Category> categoryList;

    TextView textViewproductsCount;

    public ProductsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_main, container, false);
        imageViewScan = view.findViewById(R.id.scanIcon);
        recyclerView = view.findViewById(R.id.productsMainRecycler);
        room_db = AppDatabase.getDbInstance(this.getContext());
        textViewproductsCount = view.findViewById(R.id.productsCount);

        innitViews();

        recyclerData();
        imageViewScan.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddProductActivity.class)));
        return view;
    }

    private void innitViews() {
        int countProducts = room_db.productDao().countAllProducts();
        textViewproductsCount.setText(countProducts + "");
    }

    public List<IFlexible> getDatabaseList() {
        List<IFlexible> list = new ArrayList<>();

        int countAVailable = 0;
        for (int i = 0; i < categoryList.size(); i++) {
            String category_name = categoryList.get(i).getCategory_name();
            int category_id = categoryList.get(i).getCategory_id();
            countAVailable = room_db.productDao().getSingleProductCountByCategory(category_id);
            list.add(new CategoriesProductsListAdapter(String.valueOf(i), category_name, category_id, countAVailable, 0));

        }
        return list;

    }

    public void recyclerData() {
        if (room_db.categoryDao().countAllCategorys() > 0) {
            categoryList = room_db.categoryDao().getAllCategorys();
            List<IFlexible> myItems = getDatabaseList();
            FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(myItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);

            recyclerView.setAdapter(adapter);
        }
    }
}