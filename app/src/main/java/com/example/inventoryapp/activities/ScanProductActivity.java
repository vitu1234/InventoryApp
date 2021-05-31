package com.example.inventoryapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.inventoryapp.R;
import com.example.inventoryapp.common.OnBoardingActivity;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanProductActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";

    AppDatabase room_db;

    String product_name, category_name, product_description;
    int category_id, product_quantity;
    double product_price;

    MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);
        room_db = AppDatabase.getDbInstance(this);
        progressDialog = new MyProgressDialog(this);

        Intent intent = getIntent();
        product_name = intent.getStringExtra("product_name");
        category_name = intent.getStringExtra("category_name");
        category_id = intent.getIntExtra("category_id", -1);
        product_description = intent.getStringExtra("product_description");
        product_quantity = intent.getIntExtra("product_quantity", -1);
        product_price = intent.getDoubleExtra("product_price", -1);

        initViews();
    }


    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanProductActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanProductActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                            surfaceView.setVisibility(View.GONE);
                            addProduct(intentData);
                        }
                    });

                }
            }
        });
    }

    private void addProduct(String product_code) {
        //check if product exist
        if (room_db.productDao().getSingleProductCountByNameCatId(product_name, category_id) == 0) {
            Product product = new Product();
            product.setCategory_id(category_id);
            product.setPrice(product_price);
            product.setProduct_code(product_code);
            product.setProduct_description(product_description);
            product.setProduct_name(product_name);
            product.setQuantity(product_quantity);

            room_db.productDao().insertProduct(product);
            progressDialog.showSuccessAlert("Added, please wait!");
            Log.e("e", String.valueOf(room_db.productDao().countAllProducts()));

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.closeDialog();
                    startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
                    finish();

                }
            }, 1000);
        } else {
            progressDialog.showDangerAlert("Product with same name and category already exists!");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

}