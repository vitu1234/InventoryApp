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

    @Query("SELECT * FROM product WHERE product_code = :code")
    Product findByProductCode(String code);

    @Query("SELECT * FROM product WHERE category_id = :id")
    List<Product> findByProductWithCatId(int id);

    @Query("SELECT * FROM product INNER JOIN category ON product.category_id = category.category_id  WHERE product_name LIKE '%' || :name  || '%' OR category_name LIKE '%' || :name  || '%'")
    List<Product> searchByProductName(String name);



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

    @Query("DELETE FROM product WHERE product_id =:id")
    void deleteProductById(int id);

    //count car
    @Query("SELECT * FROM product WHERE product_id = :id")
    int getSingleProductCount(int id);

    @Query("SELECT COUNT(*) FROM product WHERE product_code = :code")
    int getSingleProductCountByCode(String code);

    @Query("SELECT COUNT(*) FROM product WHERE category_id = :id")
    int getSingleProductCountByCategory(int id);

    @Query("SELECT * FROM product WHERE product_name = :name AND category_id = :id")
    int getSingleProductCountByNameCatId(String name, int id);

    //count all car
    @Query("SELECT COUNT(*) FROM product ")
    int countAllProducts();

    @Query("SELECT COUNT(*) FROM product WHERE category_id =:id ")
    int countProductsByCategory(int id);
}
