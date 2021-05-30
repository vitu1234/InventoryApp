package com.example.inventoryapp.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.models.User;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.storage.SharedPrefManager;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.textfield.TextInputEditText;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText passwordTxt, emailTxt;
    AppDatabase room_db;
    SharedPrefManager sharedPrefManager;
    MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTxt = findViewById(R.id.emailLogin);
        passwordTxt = findViewById(R.id.passwordLogin);
        room_db = AppDatabase.getDbInstance(this);
        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new MyProgressDialog(this);
    }

    public void loginUser(View view) {
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (email.isEmpty()) {
            emailTxt.setError("required field");
            return;
        }
        if (password.isEmpty()) {
            passwordTxt.setError("required field");
            return;
        }
        progressDialog.showDialog("Processing request...");
        //check if user exists
        if (room_db.userDao().getSingleUserCountByEmail(email) > 0) {
            User user = room_db.userDao().findByUserEmail(email);
            String passwordDb = user.getPassword();
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), passwordDb);
//             result.verified == true
            progressDialog.closeDialog();
            if (result.verified) {
                progressDialog.showSuccessAlert("Success");
                sharedPrefManager.saveUser(user);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
                    finish();
                }, 800);
            } else {
                progressDialog.showDangerAlert("Invalid credentials");
            }
        } else {
            progressDialog.closeDialog();
            progressDialog.showDangerAlert("Invalid user!");
        }
    }
}