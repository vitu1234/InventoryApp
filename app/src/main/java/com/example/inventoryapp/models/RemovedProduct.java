package com.example.inventoryapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "removedproduct", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = Category.class,
                parentColumns = "category_id", childColumns = "category_id")},
        indices = {
                @Index("category_id"),
        })
public class RemovedProduct  {

    @PrimaryKey(autoGenerate = true)
    int product_id;

    @ColumnInfo(name = "category_id")
    int category_id;

    @ColumnInfo(name = "quantity")
    int quantity;

    @ColumnInfo(name = "price")
    double price;

    @ColumnInfo(name = "product_name")
    String product_name;

    @ColumnInfo(name = "brand_name")
    String brand_name;

    @ColumnInfo(name = "product_code")
    String product_code;

    @ColumnInfo(name = "product_description")
    String product_description;

    @ColumnInfo(name = "product_discount")
    double product_discount;

    @ColumnInfo(name = "product_net_total")
    double product_net_total;


    public RemovedProduct() {
    }

    public RemovedProduct(int product_id, int category_id, int quantity, double price, String product_name, String brand_name, String product_code, String product_description, double product_discount, double product_net_total) {
        this.product_id = product_id;
        this.category_id = category_id;
        this.quantity = quantity;
        this.price = price;
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.product_code = product_code;
        this.product_description = product_description;
        this.product_discount = product_discount;
        this.product_net_total = product_net_total;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public double getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(double product_discount) {
        this.product_discount = product_discount;
    }

    public double getProduct_net_total() {
        return product_net_total;
    }

    public void setProduct_net_total(double product_net_total) {
        this.product_net_total = product_net_total;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
}
