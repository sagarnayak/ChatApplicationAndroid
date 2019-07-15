package com.sagar.android.chatapp.model;

public class ReadAllNotificationForRoomReq {
    private String roomId;

    public ReadAllNotificationForRoomReq() {
    }

    public ReadAllNotificationForRoomReq(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
