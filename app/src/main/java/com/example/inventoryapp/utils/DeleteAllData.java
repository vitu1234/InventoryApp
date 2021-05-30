package com.example.inventoryapp.utils;

import android.content.Context;

import com.example.inventoryapp.room_db.AppDatabase;

public class DeleteAllData {
    Context context;
    AppDatabase room_db;

    public DeleteAllData(Context context, AppDatabase room_db) {
        this.context = context;
        this.room_db = room_db;

    }

    public void deleteAllData(){
        room_db.userDao().deleteAllUsers();
        room_db.categoryDao().deleteAllCategorys();
    }


}
