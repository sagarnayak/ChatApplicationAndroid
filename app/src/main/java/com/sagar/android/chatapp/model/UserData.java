package com.sagar.android.chatapp.model;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;

    public UserData() {
    }

    public UserData(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
