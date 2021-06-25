package com.example.inventoryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.barisatalay.filterdialog.FilterDialog;
import com.barisatalay.filterdialog.model.DialogListener;
import com.barisatalay.filterdialog.model.FilterItem;
import com.example.inventoryapp.R;
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.room_db.AppDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    AppDatabase room_db;
    List<Category> categoryList;
    String category_name;
    int category_id;

    TextInputEditText textInputEditTextCatName, textInputEditTextProdName,
            textInputEditTextProdQuantity, textInputEditTextProdPrice, textInputEditTextProdDesc, textInputEditTextBrandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        room_db = AppDatabase.getDbInstance(this);
        textInputEditTextCatName = findViewById(R.id.productCatN);
        textInputEditTextProdName = findViewById(R.id.prodName);
        textInputEditTextProdQuantity = findViewById(R.id.prodQty);
        textInputEditTextProdPrice = findViewById(R.id.prodPrice);
        textInputEditTextProdDesc = findViewById(R.id.prodDesc);
        textInputEditTextBrandName = findViewById(R.id.proBrandName);

    }

    public void openScanActivity(View view) {
        String category_name = textInputEditTextCatName.getText().toString();
        String product_name = textInputEditTextProdName.getText().toString();
        String brand_name = textInputEditTextBrandName.getText().toString();
        String description = textInputEditTextProdDesc.getText().toString();

        if (category_name.isEmpty()) {
            textInputEditTextCatName.setError("Required field");
            return;
        }
        if (product_name.isEmpty()) {
            textInputEditTextProdName.setError("Required field");
            return;
        }
        if (textInputEditTextProdQuantity.getText().toString().isEmpty()) {
            textInputEditTextProdQuantity.setError("Required field");
            return;
        }
        if (textInputEditTextProdPrice.getText().toString().isEmpty()) {
            textInputEditTextProdPrice.setError("Required field");
            return;
        }
        if (textInputEditTextProdDesc.getText().toString().isEmpty()) {
            textInputEditTextProdDesc.setError("Required field");
            return;
        }

        if (textInputEditTextBrandName.getText().toString().isEmpty()) {
            textInputEditTextBrandName.setError("Required field");
            return;
        }

        int quantity = Integer.parseInt(textInputEditTextProdQuantity.getText().toString());
        double price = Double.parseDouble(textInputEditTextProdPrice.getText().toString());

        Intent intent = new Intent(this, ScanProductActivity.class);
        intent.putExtra("category_name", category_name);
        intent.putExtra("category_id", category_id);
        intent.putExtra("product_name", product_name);
        intent.putExtra("product_quantity", quantity);
        intent.putExtra("product_price", price);
        intent.putExtra("product_description", description);
        intent.putExtra("brand_name", description);
        startActivity(intent);
        finish();
    }

    private void showCategories() {
        List<String> stringList = new ArrayList<>();
        categoryList = room_db.categoryDao().getAllCategorys();
        for (int i = 0; i < room_db.categoryDao().countAllCategorys(); i++) {
            stringList.add("#" + categoryList.get(i).getCategory_id() + " " + categoryList.get(i).getCategory_name());
        }


        final FilterDialog filterDialog = new FilterDialog(this);
        filterDialog.setToolbarTitle("Categories");
        filterDialog.setSearchBoxHint("You can search here...");
        filterDialog.setList(stringList);

        filterDialog.backPressedEnabled(false);
        filterDialog.setOnCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dispose();
            }
        });

        /*
         * When you have List<String,Integer,Boolean,Double,Float> should be use this method
         */
        filterDialog.show(new DialogListener.Single() {
            @Override
            public void onResult(FilterItem selectedItem) {
                category_name = "";
                String[] category_name_id = selectedItem.getName().split(" ");
                for (int i = 0; i < category_name_id.length; i++) {
                    if (i != 0) {
                        category_name += category_name_id[i] + " ";
                    }

                }

                String[] id = category_name_id[0].split("#");
                category_id = Integer.parseInt(id[1]);

                textInputEditTextCatName.setText(category_name);

                filterDialog.dispose();
            }
        });
    }

    public void showList(View view) {
        showCategories();
    }
}