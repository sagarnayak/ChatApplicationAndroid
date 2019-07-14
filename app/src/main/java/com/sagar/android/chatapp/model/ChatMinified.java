package com.sagar.android.chatapp.model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ChatMinified {
    private String author;
    private String authorId;
    private String message;
    private String createdAt;

    private Calendar calendarCreatedAt;

    public ChatMinified() {
    }

    public ChatMinified(String author, String authorId, String message, String createdAt) {
        this.author = author;
        this.authorId = authorId;
        this.message = message;
        this.createdAt = createdAt;
    }

    private void createCalendarCreatedAt() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormatUTC =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormatUTC.setTimeZone(
                TimeZone.getTimeZone("UTC")
        );
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormatLocal =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormatLocal.setTimeZone(
                TimeZone.getDefault()
        );
        calendarCreatedAt = Calendar.getInstance();
        try {
            Date utcDate = simpleDateFormatUTC.parse(
                    createdAt
            );
            String localDate = simpleDateFormatLocal.format(
                    utcDate
            );
            calendarCreatedAt.setTime(
                    simpleDateFormatLocal.parse(localDate)
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getCalendarCreatedAt() {
        if (calendarCreatedAt == null)
            createCalendarCreatedAt();
        return calendarCreatedAt;
    }
}
