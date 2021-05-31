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
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class AddCategoryBottomSheetFragment extends BottomSheetDialogFragment {

    MaterialButton buttonAdd;
    TextInputEditText textInputEditTextCatName;
    AppDatabase room_db;
    MyProgressDialog progressDialog;

    public AddCategoryBottomSheetFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_category_bottom_sheet, container, false);
        buttonAdd = view.findViewById(R.id.btnAddCategory);
        textInputEditTextCatName = view.findViewById(R.id.productCatNamee);
        room_db = AppDatabase.getDbInstance(this.getContext());
        progressDialog = new MyProgressDialog(this.getContext());

        buttonAdd.setOnClickListener(v -> {
            String category_name = textInputEditTextCatName.getText().toString();

            if (category_name.isEmpty()) {
                textInputEditTextCatName.setError("Required field!");
                return;
            }

            //check if category name already exists
            if (room_db.categoryDao().getSingleCategoryCountByName(category_name) == 0) {
                Category category = new Category();
                category.setCategory_name(category_name);
                room_db.categoryDao().insertCategory(category);

                progressDialog.showSuccessAlert("Saved!");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.closeDialog();
                        Intent intent = getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().finish();

                        getActivity().overridePendingTransition(0, 0);
                        startActivity(intent);

                        AddCategoryBottomSheetFragment.this.dismiss();
                    }
                }, 800);
            } else {
                progressDialog.showDangerAlert("Category name already exists!");
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