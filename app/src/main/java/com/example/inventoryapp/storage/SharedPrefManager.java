package com.example.inventoryapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inventoryapp.models.User;


public class SharedPrefManager {
    private static String SHARED_PREF_NAME = "USER_DATA";
    private static String SHARED_PREF_NAME1 = "USER_ACC";
    private static String SHARED_PREF_NAME2 = "USER_ACC";
    private Context context;
    private static SharedPrefManager mInstance;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //store user into the pref
    public void saveUser(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putInt("user_id", user.getUser_id());
        editor.putInt("role_id", user.getRole_id());
        editor.putString("firstname", user.getFirstname());
        editor.putString("lastname", user.getLastname());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("photo", user.getPhoto());
        editor.putString("status", user.getStatus());
        editor.putString("email_verification_status", user.getEmail_verification_status());
        editor.putString("phone_verification_status", user.getPhone_verification_status());
        editor.putString("date_registered", user.getDate_registered());
        editor.putString("user_role", user.getUser_role());
        editor.apply();
    }

    public void SaveAccountType(String type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("account_type", type);
        editor.apply();
    }

    public void saveLicense(boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("license", false);
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //if value = -1 that means not logged in
        if (sharedPreferences.getInt("user_id", -1) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public String getAccountType() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        return sharedPreferences.getString("account_type", null);
    }

    public boolean hasLicense(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("license",false);
    }

    public User getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User(
                sharedPreferences.getInt("user_id", -1),
                sharedPreferences.getInt("role_id", -1),
                sharedPreferences.getString("firstname", null),
                sharedPreferences.getString("lastname", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("photo", null),
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("email_verification_status", null),
                sharedPreferences.getString("phone_verification_status", null),
                sharedPreferences.getString("date_registered", null),
                sharedPreferences.getString("user_role", null)
        );
        return user;

    }

    public void logoutUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        editor.clear();
        editor1.clear();
        editor2.clear();

        editor.apply();
        editor1.apply();
        editor2.apply();
    }

}