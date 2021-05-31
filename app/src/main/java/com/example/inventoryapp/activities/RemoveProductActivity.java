package com.example.inventoryapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.inventoryapp.R;
import com.example.inventoryapp.fragments.RemoveProductBottomSheetFragment;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class RemoveProductActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";

    TextInputEditText textInputEditTextQty;

    AppDatabase room_db;

    MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_product);

        room_db = AppDatabase.getDbInstance(this);
        progressDialog = new MyProgressDialog(this);


        initViews();
    }

    public void showScanView(View view) {
        cameraSource.stop();
        RemoveProductBottomSheetFragment bottomSheetFragment = new RemoveProductBottomSheetFragment();
        bottomSheetFragment.setAllowEnterTransitionOverlap(true);
        bottomSheetFragment.show(this.getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValueRemove);
        surfaceView = findViewById(R.id.surfaceViewRemove);
        textInputEditTextQty = findViewById(R.id.rmQuantity);
        textInputEditTextQty.setText(1 + "");
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
                    if (ActivityCompat.checkSelfPermission(RemoveProductActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(RemoveProductActivity.this, new
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
                            cameraSource.stop();
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.closeDialog();
                                    removeProduct(intentData);

                                }
                            }, 1000);


                        }
                    });

                }
            }
        });
    }

    private void removeProduct(String product_code) {
        if (textInputEditTextQty.getText().toString().isEmpty()) {
            textInputEditTextQty.setError("Required field");
            return;
        }
        //check if product exist
        if (room_db.productDao().getSingleProductCountByCode(product_code) > 0) {
            Product product = room_db.productDao().findByProductCode(product_code);
            Product product1 = new Product();

            product1.setProduct_id(product.getProduct_id());
            product1.setCategory_id(product.getCategory_id());
            product1.setPrice(product.getPrice());
            product1.setProduct_code(product_code);
            product1.setProduct_description(product.getProduct_description());
            product1.setProduct_name(product.getProduct_name());
            product1.setQuantity((product.getQuantity() - Integer.parseInt(textInputEditTextQty.getText().toString())));

            room_db.productDao().updateProduct(product1);

            progressDialog.showSuccessAlert("Removed, please wait!");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.closeDialog();
                    startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
                    Intent intent = new Intent(RemoveProductActivity.this, ProductListActivity.class);
                    intent.putExtra("category_id", product.getCategory_id());
                    startActivity(intent);

                }
            }, 1000);
        } else {
            progressDialog.showDangerAlert("item with that code not found, try entering manually or adding it first!");
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

    public void removeProd(View view) {
        onBackPressed();
    }
}