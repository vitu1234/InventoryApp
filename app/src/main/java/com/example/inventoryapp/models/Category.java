package com.example.inventoryapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    int category_id;
    @ColumnInfo(name = "category_name")
    String category_name;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int categoty_id) {
        this.category_id = categoty_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Category(String category_name) {
        this.category_name = category_name;
    }
}
