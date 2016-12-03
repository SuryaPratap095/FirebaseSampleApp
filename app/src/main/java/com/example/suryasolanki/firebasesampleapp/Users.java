package com.example.suryasolanki.firebasesampleapp;

/**
 * Created by surya.solanki on 03-12-2016.*
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {

    public String FirstName;
    public String LastName;
    public String Address;

    public Users() {
    }

    public Users(String firstName, String lastName, String address) {
        FirstName = firstName;
        LastName = lastName;
        Address = address;
    }
}
