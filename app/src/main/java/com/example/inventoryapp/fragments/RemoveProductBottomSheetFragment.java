package com.example.inventoryapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.inventoryapp.R;
import com.example.inventoryapp.activities.ProductListActivity;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class RemoveProductBottomSheetFragment extends BottomSheetDialogFragment {

    MaterialButton buttonRemove;
    TextInputEditText textInputEditTextCatName;
    AppDatabase room_db;
    MyProgressDialog progressDialog;

    public RemoveProductBottomSheetFragment() {
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
        View view = inflater.inflate(R.layout.fragment_remove_product_bottom_sheet, container, false);

        buttonRemove = view.findViewById(R.id.btnRemoveProdCode);
        textInputEditTextCatName = view.findViewById(R.id.txtBarcodeValueRemove);
        room_db = AppDatabase.getDbInstance(this.getContext());
        progressDialog = new MyProgressDialog(this.getContext());

        buttonRemove.setOnClickListener(v -> {
            String product_code = textInputEditTextCatName.getText().toString();

            if (textInputEditTextCatName.getText().toString().isEmpty()) {
                textInputEditTextCatName.setError("Required field");
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
                product1.setQuantity((product.getQuantity() - Integer.parseInt(textInputEditTextCatName.getText().toString())));
                room_db.productDao().updateProduct(product1);

                progressDialog.showSuccessAlert("Removed, please wait!");

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.closeDialog();
                        Intent intent = new Intent(getActivity(), ProductListActivity.class);
                        intent.putExtra("category_id", product.getCategory_id());
                        startActivity(intent);
                        RemoveProductBottomSheetFragment.this.dismiss();

                    }
                }, 1000);
            } else {
                progressDialog.showDangerAlert("item with that code not found, try entering manually or adding it first!");
            }

        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        return displayMetrics.heightPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity) getContext()).getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().height() - insets.top - insets.bottom;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }
}