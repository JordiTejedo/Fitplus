package com.example.jordi.fitplus.Models;

import java.util.Date;

/**
 * Created by Jordi on 03/08/2018.
 */

public class User {

    public String name;
    public String lastName;
    public String dniNie;
    public String email;
    public String phone;
    public String address;
    public String postalCode;
    public Date creationDate;
    public Date lastLoginDate;
    public Date lockedDate;
    public String type;
    public Double saldo;

    public User () { }

    public  User (String name, String lastName, String dniNie, String email, String phone, String address, String postalCode) {
        this.name = name;
        this.lastName = lastName;
        this.dniNie = dniNie;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.postalCode = postalCode;
    }

}
