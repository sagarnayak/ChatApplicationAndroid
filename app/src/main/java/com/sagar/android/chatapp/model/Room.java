package com.sagar.android.chatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Room {
    @SerializedName("id")
    private String id;
    @SerializedName("updatedAt")
    private String updatedat;
    @SerializedName("createdAt")
    private String createdat;
    @SerializedName("members")
    private List<Members> members;
    @SerializedName("name")
    private String name;
    @SerializedName("users")
    private ArrayList<User> users;

    public Room() {
    }

    public Room(String id, String updatedat, String createdat, List<Members> members, String name, ArrayList<User> users) {
        this.id = id;
        this.updatedat = updatedat;
        this.createdat = createdat;
        this.members = members;
        this.name = name;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public List<Members> getMembers() {
        return members;
    }

    public void setMembers(List<Members> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public class Members {
        @SerializedName("user")
        private String user;
        @SerializedName("_id")
        private String Id;

        public Members() {
        }

        public Members(String user, String id) {
            this.user = user;
            Id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }
    }
}
