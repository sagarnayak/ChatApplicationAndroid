package com.sagar.android.chatapp.model;

import com.sagar.android.chatapp.core.Enums;

public class Result {
    private Enums.Result result;
    private String message;

    public Result() {
    }

    public Result(Enums.Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public Enums.Result getResult() {
        return result;
    }

    public void setResult(Enums.Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
