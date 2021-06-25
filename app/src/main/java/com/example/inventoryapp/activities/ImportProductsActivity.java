package com.example.inventoryapp.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.barisatalay.filterdialog.FilterDialog;
import com.barisatalay.filterdialog.model.DialogListener;
import com.barisatalay.filterdialog.model.FilterItem;
import com.example.inventoryapp.R;
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.CSVReader;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportProductsActivity extends AppCompatActivity {
    AppDatabase room_db;
    List<Category> categoryList;
    String category_name;
    int category_id = -1;
    MyProgressDialog progressDialog;


    TextInputEditText textInputEditTextCatName;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_products);
        room_db = AppDatabase.getDbInstance(this);
        progressDialog = new MyProgressDialog(this);
        textInputEditTextCatName = findViewById(R.id.productsCatName);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String filepath = data.getData().getPath();
                        Log.e("File path", filepath);


                        if (filepath.contains("/root_path"))
                            filepath = filepath.replace("/root_path", "");

                        Log.e("New File path", filepath);

                        Uri filce = Uri.fromFile(new File(filepath));
                        String fileExt = MimeTypeMap.getFileExtensionFromUrl(filce.toString());
                        Log.e("fileExt", fileExt);

                        if (fileExt.toLowerCase().equals("csv")) {
                            progressDialog.showDialog("Importing...");
                            File file = new File(filepath);
                            CSVReader csvReader = new CSVReader(this);
                            List csv = csvReader.read(file);
                            if (csv.size() > 0) {
                                String[] header_row = (String[]) csv.get(0);
                                if (header_row.length > 1) {
                                    String col1 = header_row[0];
                                    String col2 = header_row[1];
//                                Log.e("roheader", col1);
                                }

                                for (int i = 1; i < csv.size(); i++) {
                                    String[] row = (String[]) csv.get(i);
//                                    for (int y = 0; y < row.length; y++) {
                                    String brand_name = row[0];
                                    String product_name = row[1];
                                    String product_code = row[2];
                                    String qty = row[3];
                                    String price = row[4];
                                    String desc = row[5];
//                                    Log.e("product", brand_name + " " + product_name + " " + qty + " " + desc);

                                    Product product = new Product();
                                    product.setBrand_name(brand_name);
                                    product.setCategory_id(category_id);
                                    product.setQuantity(Integer.parseInt(qty));
                                    product.setProduct_description(desc);
                                    product.setProduct_code(product_code);
                                    product.setProduct_name(product_name);
                                    product.setPrice(Double.parseDouble(price));
                                    if (room_db.productDao().getSingleProductCountByCode(product_code) == 0) {
                                        room_db.productDao().insertProduct(product);
                                    } else {
                                        Toast.makeText(this, "Product code: " + product_code + " skipped, already exists!", Toast.LENGTH_SHORT).show();
                                    }

//                                    }
                                }

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    progressDialog.closeDialog();
                                    progressDialog.showSuccessAlert("Success");
                                }, 1000);

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    progressDialog.closeDialog();
                                    Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                                    intent.putExtra("category_id", category_id);
                                    startActivity(intent);
                                    finish();

                                }, 1500);


                            }
                        } else {
                            Toast.makeText(this, "Unsupported file type, please select a csv file!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void showList(View view) {
        showCategories();
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
        filterDialog.setOnCloseListener(view -> filterDialog.dispose());

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

    public void attachFile(View view) {
        if (category_id != -1 && category_id > 0) {
            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
            fileintent.setType("text/csv");
            fileintent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                someActivityResultLauncher.launch(fileintent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No activity can handle picking a file. Showing alternatives.", Toast.LENGTH_SHORT).show();
            }
        } else {
            textInputEditTextCatName.setError("required!");
            return;
        }

    }
}
