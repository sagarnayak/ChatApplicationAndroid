package com.sagar.android.chatapp.model;

public class FcmTokenData {
    private String fcmToken;

    public FcmTokenData() {
    }

    public FcmTokenData(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
