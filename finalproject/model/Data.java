package com.example.finalproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class Data implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("gender_")
    private String gender_;
    @SerializedName("email")
    private String email;
    private String contact;
    private String gender;
    private String userType;
    private String password;
    private String age;
    private String symptoms;
    private String quarantine;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender_() {
        return gender_;
    }

    public void setGender_(String gender_) {
        this.gender_ = gender_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getQuarantine() {
        return quarantine;
    }

    public void setQuarantine(String quarantine) {
        this.quarantine = quarantine;
    }

    public Data() {
    }

    public Data(String name, String contact,String age) {
        this.name = name;
        this.phone = contact;
        this.age = age;
    }


    public Data(String username, String email, String contact, String gender, String userType, String password) {
        this.username = username;
        this.email = email;
        this.contact = contact;
        this.gender = gender;
        this.userType = userType;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
