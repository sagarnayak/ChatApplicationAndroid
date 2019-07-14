package com.sagar.android.chatapp.model;

import java.util.ArrayList;

public class RoomMinified {
    private String roomId;
    private String roomName;
    private ArrayList<ChatMinified> chats = new ArrayList<>();

    public RoomMinified() {
    }

    public RoomMinified(String roomId, String roomName, ArrayList<ChatMinified> chats) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.chats = chats;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<ChatMinified> getChats() {
        return chats;
    }

    public void setChats(ArrayList<ChatMinified> chats) {
        this.chats = chats;
    }
}
