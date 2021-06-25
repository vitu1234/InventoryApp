package com.example.inventoryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.models.User;
import com.example.inventoryapp.storage.SharedPrefManager;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    //initialize views
    EditText passwordTxt1, passwordTxt2, passwordTxt3, phone_number;
    TextView driver_name, driver_phone, driver_email;

    CircleImageView img;

    SharedPrefManager sharedPrefManager;
    private static final int PICKER_REQUEST_CODE = 100;
    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
}