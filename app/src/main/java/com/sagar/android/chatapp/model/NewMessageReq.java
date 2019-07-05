package com.sagar.android.chatapp.model;

public class NewMessageReq {
    private String message;
    private String roomId;

    public NewMessageReq() {
    }

    public NewMessageReq(String message, String roomId) {
        this.message = message;
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
