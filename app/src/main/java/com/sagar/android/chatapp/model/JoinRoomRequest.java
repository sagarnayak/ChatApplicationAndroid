package com.sagar.android.chatapp.model;

public class JoinRoomRequest {
    private String roomId;

    public JoinRoomRequest() {
    }

    public JoinRoomRequest(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
