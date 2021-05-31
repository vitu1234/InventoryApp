package com.example.inventoryapp.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.inventoryapp.models.Category;
import com.example.inventoryapp.models.Product;
import com.example.inventoryapp.models.User;


@Database(entities = {User.class, Category.class, Product.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();


    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {
        String DB_NAME = "app_room_db";
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
