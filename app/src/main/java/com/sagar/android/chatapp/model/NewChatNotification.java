package com.sagar.android.chatapp.model;

public class NewChatNotification {
    private String roomName;
    private String roomId;
    private String message;
    private String authorId;
    private String authorName;
    private String createdAt;

    public NewChatNotification() {
    }

    public NewChatNotification(String roomName, String roomId, String message, String authorId, String authorName, String createdAt) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.message = message;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
