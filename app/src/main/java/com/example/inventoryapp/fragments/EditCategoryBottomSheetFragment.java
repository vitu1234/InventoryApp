package com.example.inventoryapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inventoryapp.R;
import com.example.inventoryapp.activities.DashboardActivity;
import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class EditCategoryBottomSheetFragment extends BottomSheetDialogFragment {
    int category_id = -1;
    AppDatabase room_db;
    TextInputEditText textInputEditTextCatName;
    MaterialButton buttonEdit, buttonDel;
    MyProgressDialog progressDialog;

    public EditCategoryBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_id = getArguments().getInt("category_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_category_bottomsheet, container, false);
        room_db = AppDatabase.getDbInstance(this.getActivity());
        progressDialog = new MyProgressDialog(this.getActivity());

        textInputEditTextCatName = view.findViewById(R.id.eproductCatNamee);
        buttonEdit = view.findViewById(R.id.btnEditCategory);
        buttonDel = view.findViewById(R.id.btnDelCategory);

        buttonEdit.setOnClickListener(v -> {
            //get text
            String category_name = textInputEditTextCatName.getText().toString();
            if (category_name.isEmpty()) {
                textInputEditTextCatName.setError("Required field!");
                return;
            }

            Category category = new Category();
            category.setCategory_id(category_id);
            category.setCategory_name(category_name);

            room_db.categoryDao().updateCategory(category);
            progressDialog.showSuccessAlert("Category Name Updated!");

            Intent intent = getActivity().getIntent();
            intent.putExtra("category_id", category_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();

            getActivity().overridePendingTransition(0, 0);
            startActivity(intent);

            EditCategoryBottomSheetFragment.this.dismiss();
        });

        buttonDel.setOnClickListener(v -> {
            room_db.categoryDao().deleteCategory(category_id);
            progressDialog.showSuccessAlert("Deleted!");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.closeDialog();
                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    startActivity(intent);
                    EditCategoryBottomSheetFragment.this.dismiss();

                }
            }, 1000);


        });

        textInputEditTextCatName.setText(room_db.categoryDao().findByCategoryId(category_id).getCategory_name());
        return view;
    }
}