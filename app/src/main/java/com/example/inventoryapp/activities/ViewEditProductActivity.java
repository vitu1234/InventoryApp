package com.example.inventoryapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.barisatalay.filterdialog.FilterDialog;
import com.barisatalay.filterdialog.model.DialogListener;
import com.example.inventoryapp.R;
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ViewEditProductActivity extends AppCompatActivity {
    AppDatabase room_db;
    List<Category> categoryList;
    String category_name;
    int category_id = -1;
    int product_id = -1;
    MyProgressDialog progressDialog;
    TextInputEditText textInputEditTextCatName, textInputEditTextProdName,
            textInputEditTextProdQuantity, textInputEditTextProdPrice, textInputEditTextProdDesc, textInputEditTextBrandName, textInputEditTextCode;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_product);

        Intent intent = this.getIntent();
        product_id = intent.getIntExtra("product_id", -1);

        room_db = AppDatabase.getDbInstance(this);
        progressDialog = new MyProgressDialog(this);

        textInputEditTextCode = findViewById(R.id.eproCode);
        textInputEditTextCatName = findViewById(R.id.eproductCatN);
        textInputEditTextProdName = findViewById(R.id.eprodName);
        textInputEditTextProdQuantity = findViewById(R.id.eprodQty);
        textInputEditTextProdPrice = findViewById(R.id.eprodPrice);
        textInputEditTextProdDesc = findViewById(R.id.eprodDesc);
        textInputEditTextBrandName = findViewById(R.id.eproBrandName);

        textInputEditTextCatName.setFocusable(true);
        textInputEditTextCatName.isFocused();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textInputEditTextCatName.isFocusedByDefault();
        }

        //get the product
        Product product = room_db.productDao().findByProductId(product_id);
        textInputEditTextCode.setText(product.getProduct_code());
        category_id = product.getCategory_id();
        textInputEditTextCatName.setText(room_db.categoryDao().findByCategoryId(product.getCategory_id()).getCategory_name());
        textInputEditTextProdName.setText(product.getProduct_name());
        textInputEditTextProdQuantity.setText("" + product.getQuantity());
        textInputEditTextProdPrice.setText("" + product.getPrice());
        textInputEditTextProdDesc.setText(product.getProduct_description());
        textInputEditTextBrandName.setText(product.getBrand_name());
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
        filterDialog.show((DialogListener.Single) selectedItem -> {
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
        });
    }

    public void showList(View view) {
        showCategories();
    }

    public void saveProduct(View view) {
        String category_name = textInputEditTextCatName.getText().toString();
        String product_name = textInputEditTextProdName.getText().toString();
        String brand_name = textInputEditTextBrandName.getText().toString();
        String description = textInputEditTextProdDesc.getText().toString();
        String product_code = textInputEditTextCode.getText().toString();
        int quantity = Integer.parseInt(textInputEditTextProdQuantity.getText().toString());
        double price = Double.parseDouble(textInputEditTextProdPrice.getText().toString());

        if (category_name.isEmpty()) {
            textInputEditTextCatName.setError("Required field");
            return;
        }
        if (product_code.isEmpty()) {
            textInputEditTextCode.setError("Required field");
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

        Product product = new Product();
        product.setBrand_name(brand_name);
        product.setProduct_id(product_id);
        product.setCategory_id(category_id);
        product.setQuantity(Integer.parseInt(String.valueOf(quantity)));
        product.setProduct_description(description);
        product.setProduct_code(product_code);
        product.setProduct_name(product_name);
        product.setPrice(Double.parseDouble(String.valueOf(price)));
        room_db.productDao().updateProduct(product);
        progressDialog.showSuccessAlert("Product Updated!");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.closeDialog();
                Intent intent = new Intent(getApplicationContext(), ViewEditProductActivity.class);
                intent.putExtra("category_id", category_id);
                intent.putExtra("product_id", product_id);
                startActivity(intent);
                finish();

            }
        }, 1000);
    }

    public void deleteProduct(View view) {
        room_db.productDao().deleteProductById(product_id);
        progressDialog.showSuccessAlert("Product deleted, please wait!");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.closeDialog();
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("category_id", category_id);
                startActivity(intent);
                finish();

            }
        }, 1000);

    }

    public void goBackStep(View view) {
        onBackPressed();
    }
}