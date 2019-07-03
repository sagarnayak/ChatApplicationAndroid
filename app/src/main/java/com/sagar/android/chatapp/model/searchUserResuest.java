package com.sagar.android.chatapp.model;

import java.util.ArrayList;

public class searchUserResuest {
    private String containing;
    private String limit;
    private String skip;
    private ArrayList<String> alreadyUsed;

    public searchUserResuest() {
    }

    public searchUserResuest(String containing, String limit, String skip, ArrayList<String> alreadyUsed) {
        this.containing = containing;
        this.limit = limit;
        this.skip = skip;
        this.alreadyUsed = alreadyUsed;
    }

    public String getContaining() {
        return containing;
    }

    public void setContaining(String containing) {
        this.containing = containing;
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

    public ArrayList<String> getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(ArrayList<String> alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }
}
