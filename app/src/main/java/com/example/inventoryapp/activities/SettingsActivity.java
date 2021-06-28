package com.example.inventoryapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.models.RemovedProduct;
import com.example.inventoryapp.models.User;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.storage.SharedPrefManager;

import net.ozaydin.serkan.easy_csv.EasyCsv;
import net.ozaydin.serkan.easy_csv.FileCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    //initialize views
    EditText passwordTxt1, passwordTxt2, passwordTxt3, phone_number;
    TextView driver_name, driver_phone, driver_email;

    CircleImageView img;
    AppDatabase room_db;

    SharedPrefManager sharedPrefManager;
    private static final int PICKER_REQUEST_CODE = 100;
    File imgFile;
    public final int WRITE_PERMISSON_REQUEST_CODE = 1;
    EasyCsv easyCsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        room_db = AppDatabase.getDbInstance(this);
        easyCsv = new EasyCsv(this);

        //hooks
        passwordTxt1 = findViewById(R.id.current_pwd);
        passwordTxt2 = findViewById(R.id.new_pwd);
        passwordTxt3 = findViewById(R.id.con_pwd);
        driver_email = findViewById(R.id.profile_driver_email);
        driver_name = findViewById(R.id.profile_driver_name);
        phone_number = findViewById(R.id.new_email);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());


        //stored user
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        driver_name.setText(user.getFirstname() + " " + user.getLastname());
        driver_email.setText(user.getEmail());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            passwordTxt1.isFocusedByDefault();
            passwordTxt2.isFocusedByDefault();
            passwordTxt3.isFocusedByDefault();
        }
    }

    public void exportData(View view) {
        if (room_db.productDao().countAllProducts() > 0) {
            exportAvailableProducts();
        }
        if (room_db.productRemoveDao().countAllProducts() > 0) {
            exportSoldProducts();
        }

    }

    public void exportAvailableProducts() {
        List<String> headerList = new ArrayList<>();
        headerList.add("Category Name.Product Name.Brand Name.Quantity.Price (MWK).Description-");
        //get all products and categories
        List<Product> productList = room_db.productDao().getAllProducts();

        List<String> dataList = new ArrayList<>();

        for (int i = 0; i < productList.size(); i++) {
            int cat_id = productList.get(i).getCategory_id();
            String cat_name = room_db.categoryDao().findByCategoryId(cat_id).getCategory_name();
            String product_name = productList.get(i).getProduct_name();
            String brand_name = productList.get(i).getBrand_name();
            int qty = productList.get(i).getQuantity();
            double price = productList.get(i).getPrice();
            String desc = productList.get(i).getProduct_description();
            dataList.add(cat_name + "." + product_name + "." + brand_name + "." + qty + "." + price + "." + desc + "-");
        }

        easyCsv.setSeparatorColumn(".");
        easyCsv.setSeperatorLine("-");

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHH:mm:ss");
        Date date = new Date();
        String fileName = "AvailableProducts" + formatter.format(date);

        easyCsv.createCsvFile(fileName, headerList, dataList, WRITE_PERMISSON_REQUEST_CODE, new FileCallback() {
            @Override
            public void onSuccess(File file) {
                Toast.makeText(getApplicationContext(), "Saved file in " + file.getPath(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String err) {
                Toast.makeText(getApplicationContext(), "Error " + err, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void exportSoldProducts() {
        List<String> headerList = new ArrayList<>();
        headerList.add("Category Name.Product Name.Brand Name.Quantity.Price (MWK).Description" + "Discount." + "VAT."+"Net Total-");
        //get all products and categories
        List<RemovedProduct> productList = room_db.productRemoveDao().getAllProducts();

        List<String> dataList = new ArrayList<>();

        for (int i = 0; i < productList.size(); i++) {
            int cat_id = productList.get(i).getCategory_id();
            String cat_name = room_db.categoryDao().findByCategoryId(cat_id).getCategory_name();
            String product_name = productList.get(i).getProduct_name();
            String brand_name = productList.get(i).getBrand_name();
            int qty = productList.get(i).getQuantity();
            double price = productList.get(i).getPrice();
            String desc = productList.get(i).getProduct_description();
            double discount = productList.get(i).getProduct_discount();
            double net_total = productList.get(i).getProduct_net_total();
            String vat = "16.5%";
            dataList.add(cat_name + "." + product_name + "." + brand_name + "." + qty + "." + price + "." + desc + "." + discount + "." + vat + "."+net_total+"-");
        }

        easyCsv.setSeparatorColumn(".");
        easyCsv.setSeperatorLine("-");

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHH:mm:ss");
        Date date = new Date();
        String fileName = "SoldProducts" + formatter.format(date);

        easyCsv.createCsvFile(fileName, headerList, dataList, WRITE_PERMISSON_REQUEST_CODE, new FileCallback() {
            @Override
            public void onSuccess(File file) {
            }

            @Override
            public void onFail(String err) {
                Toast.makeText(getApplicationContext(), "Error " + err, Toast.LENGTH_SHORT).show();

            }
        });
    }
}