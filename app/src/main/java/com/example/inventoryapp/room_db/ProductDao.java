package com.example.inventoryapp.room_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inventoryapp.models.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT *FROM product ORDER BY product_id DESC")
    List<Product> getAllProducts();

    @Query("SELECT * FROM product WHERE product_id = :id")
    Product findByProductId(int id);

    @Query("SELECT * FROM product WHERE product_name = :name")
    Product findByProductName(String name);

    @Query("SELECT * FROM product WHERE product_name = :name AND category_id = :id")
    Product findByProductNameWithCatId(String name, int id);

    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert()
    void insertProduct(Product product);


    @Update
    void updateProduct(Product product);

    // below line is use to delete a
    // specific car in our database.
    @Delete
    void deleteProduct(Product product);

    // on below line we are making query to
    @Query("DELETE FROM product")
    void deleteAllProducts();

    //count car
    @Query("SELECT * FROM Product WHERE product_id = :id")
    int getSingleProductCount(int id);

    @Query("SELECT * FROM Product WHERE product_name = :name AND category_id = :id")
    int getSingleProductCountByNameCatId(String name, int id);

    //count all car
    @Query("SELECT COUNT(*) FROM product ")
    int countAllProducts();
}
