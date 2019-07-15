package com.sagar.android.chatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class User {
    @SerializedName("age")
    private int age;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("_id")
    private String Id;
    private Calendar avatarLastUpdated = Calendar.getInstance();

    public User() {
    }

    public User(int age, String email, String name, String id) {
        this.age = age;
        this.email = email;
        this.name = name;
        Id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public Calendar getAvatarLastUpdated() {
        return avatarLastUpdated;
    }

    public void setAvatarLastUpdated(Calendar avatarLastUpdated) {
        this.avatarLastUpdated = avatarLastUpdated;
    }
}
