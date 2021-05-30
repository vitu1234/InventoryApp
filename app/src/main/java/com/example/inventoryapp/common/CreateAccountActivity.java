package com.example.inventoryapp.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapp.R;
import com.example.inventoryapp.activities.DashboardActivity;
import com.example.inventoryapp.models.User;
import com.example.inventoryapp.room_db.AppDatabase;
import com.example.inventoryapp.storage.SharedPrefManager;
import com.example.inventoryapp.utils.MyProgressDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CreateAccountActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutFname, textInputLayoutLname, textInputLayoutEmail, textInputLayoutPassword;

    AppDatabase room_db;

    MyProgressDialog progressDialog;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        textInputLayoutFname = findViewById(R.id.fnameLayout);
        textInputLayoutLname = findViewById(R.id.lnameLayout);
        textInputLayoutEmail = findViewById(R.id.emailLayout);
        textInputLayoutPassword = findViewById(R.id.passwordLayout);

        room_db = AppDatabase.getDbInstance(this);
        progressDialog = new MyProgressDialog(this);
        sharedPrefManager = new SharedPrefManager(this);
    }


    public boolean validationEmail() {
        String email = textInputLayoutEmail.getEditText().getText().toString();

        if (email.isEmpty()) {
            textInputLayoutEmail.setErrorEnabled(true);
            textInputLayoutEmail.setError("Fill field");
            return true;
        } else {
            if (isValidMail(email)) {
                textInputLayoutEmail.setErrorEnabled(false);
                textInputLayoutEmail.setError(null);
                return true;

            } else {
                textInputLayoutEmail.setErrorEnabled(true);
                textInputLayoutEmail.setError("Invalid email");
                return true;
            }

        }
    }

    public boolean validationPassName(TextInputLayout textInputLayout) {
        String email = textInputLayout.getEditText().getText().toString();

        if (email.isEmpty()) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Fill field");
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
            return true;


        }
    }

    private boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
    }

    public void signUpUser(View view) {
        if (validationPassName(textInputLayoutFname) && validationPassName(textInputLayoutLname) && validationEmail() && validationPassName(textInputLayoutPassword)) {

            progressDialog.showDialog("Processing request...");

            String fname = textInputLayoutFname.getEditText().getText().toString();
            String lname = textInputLayoutLname.getEditText().getText().toString();
            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            //check if user with the email exists
            if (room_db.userDao().getSingleUserCountByEmail(email) == 0) {
                User user = new User();
                user.setFirstname(fname);
                user.setLastname(lname);
                user.setEmail(email);
                user.setPassword(hashedPassword);
                user.setPhoto("NULL");
                user.setStatus("active");
                room_db.userDao().insertUser(user);
                sharedPrefManager.saveUser(user);
                progressDialog.closeDialog();

                progressDialog.showSuccessAlert("Account created!");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    finish();
                }, 800);
            } else {
                progressDialog.closeDialog();
                progressDialog.showDangerAlert("Email already in use, use a different email!");
            }

        }
    }
}