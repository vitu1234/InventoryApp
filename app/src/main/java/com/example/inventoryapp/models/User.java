package com.example.inventoryapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    int user_id;


    @ColumnInfo(name = "firstname")
    String firstname;
    @ColumnInfo(name = "lastname")
    String lastname;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "password")
    String password;

    @ColumnInfo(name = "photo", defaultValue = "NULL")
    String photo;

    @ColumnInfo(name = "status")//1 = active, 2 inactive
    String status;



    public User() {
    }

    public User(int user_id, String firstname, String lastname, String email, String password, String photo, String status) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.status = status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoto() {
        return photo;
    }

    public String getStatus() {
        return status;
    }


}

