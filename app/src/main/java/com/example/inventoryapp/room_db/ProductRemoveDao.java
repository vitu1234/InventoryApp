package com.example.inventoryapp.room_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inventoryapp.models.RemovedProduct;

import java.util.List;

@Dao
public interface ProductRemoveDao {
    @Query("SELECT *FROM product ORDER BY product_id DESC")
    List<RemovedProduct> getAllProducts();

    @Query("SELECT * FROM removedproduct WHERE product_id = :id")
    RemovedProduct findByProductId(int id);

    @Query("SELECT * FROM removedproduct WHERE product_name = :name")
    RemovedProduct findByProductName(String name);

    @Query("SELECT * FROM removedproduct WHERE product_code = :code")
    RemovedProduct findByProductCode(String code);

    @Query("SELECT * FROM removedproduct WHERE category_id = :id")
    List<RemovedProduct> findByProductWithCatId(int id);

    @Query("SELECT COUNT( *) FROM removedproduct WHERE product_code = :code")
    int countByProductCode(String code);

    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert()
    void insertProduct(RemovedProduct product);


    @Update
    void updateProduct(RemovedProduct product);

    // below line is use to delete a
    // specific car in our database.
    @Delete
    void deleteProduct(RemovedProduct product);

    // on below line we are making query to
    @Query("DELETE FROM removedproduct")
    void deleteAllProducts();

    //count car
    @Query("SELECT * FROM removedproduct WHERE product_id = :id")
    int getSingleProductCount(int id);

    @Query("SELECT COUNT(*) FROM removedproduct WHERE category_id = :id")
    int getSingleProductCountByCategory(int id);

    @Query("SELECT * FROM removedproduct WHERE product_name = :name AND category_id = :id")
    int getSingleProductCountByNameCatId(String name, int id);

    //count all car
    @Query("SELECT COUNT(*) FROM removedproduct ")
    int countAllProducts();
}
