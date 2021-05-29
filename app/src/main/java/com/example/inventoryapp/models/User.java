package com.example.inventoryapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    int room_user_id;

    @ColumnInfo(name = "user_id")
    int user_id;

    @ColumnInfo(name = "role_id")
    int role_id;

    @ColumnInfo(name = "firstname")
    String firstname;
    @ColumnInfo(name = "lastname")
    String lastname;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "phone")
    String phone;

    @ColumnInfo(name = "photo")
    String photo;

    @ColumnInfo(name = "status")
    String status;

    @ColumnInfo(name = "email_verification_status")
    String email_verification_status;

    @ColumnInfo(name = "phone_verification_status")
    String phone_verification_status;

    @ColumnInfo(name = "date_registered")
    String date_registered;

    @ColumnInfo(name = "user_role")
    String user_role;

    public User(int user_id, int role_id, String firstname, String lastname, String email, String phone, String photo, String status, String email_verification_status, String phone_verification_status, String date_registered, String user_role) {
        this.user_id = user_id;
        this.role_id = role_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.status = status;
        this.email_verification_status = email_verification_status;
        this.phone_verification_status = phone_verification_status;
        this.date_registered = date_registered;
        this.user_role = user_role;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getRole_id() {
        return role_id;
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

    public String getPhone() {
        return phone;
    }

    public String getPhoto() {
        return photo;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail_verification_status() {
        return email_verification_status;
    }

    public String getPhone_verification_status() {
        return phone_verification_status;
    }

    public String getDate_registered() {
        return date_registered;
    }

    public String getUser_role() {
        return user_role;
    }

    public int getRoom_user_id() {
        return room_user_id;
    }

    public void setRoom_user_id(int room_user_id) {
        this.room_user_id = room_user_id;
    }
}

