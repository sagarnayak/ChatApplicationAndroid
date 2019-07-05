package com.sagar.android.chatapp.model;

public class GetChatsRequest {
    private String limit;
    private String skip;
    private String roomId;

    public GetChatsRequest() {
    }

    public GetChatsRequest(String limit, String skip, String roomId) {
        this.limit = limit;
        this.skip = skip;
        this.roomId = roomId;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
