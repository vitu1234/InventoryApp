package com.example.inventoryapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.fragments.AddCategoryBottomSheetFragment;

public class ProductCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
    }

    public void openAddCategory(View view) {
        AddCategoryBottomSheetFragment bottomSheetFragment = new AddCategoryBottomSheetFragment();
        bottomSheetFragment.setAllowEnterTransitionOverlap(true);
        bottomSheetFragment.show(this.getSupportFragmentManager(), bottomSheetFragment.getTag());
    }
}