package com.example.inventoryapp.room_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inventoryapp.models.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT *FROM Category")
    List<Category> getAllCategorys();

    @Query("SELECT * FROM Category WHERE category_id = :id")
    Category findByCategoryId(int id);

    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert()
    void insertCategory(Category Category);


    @Update
    void updateCategory(Category Category);

    // below line is use to delete a
    // specific car in our database.
    @Delete
    void deleteCategory(Category Category);

    // on below line we are making query to
    @Query("DELETE FROM Category")
    void deleteAllCategorys();

    //count car
    @Query("SELECT * FROM Category WHERE category_id = :id")
    int getSingleCategoryCount(int id);

    //count all car
    @Query("SELECT COUNT(*) FROM Category ")
    int countAllCategorys();
}