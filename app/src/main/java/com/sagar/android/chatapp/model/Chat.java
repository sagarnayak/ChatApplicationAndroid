package com.sagar.android.chatapp.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Chat {
    @SerializedName("updatedAt")
    private String updatedat;
    @SerializedName("createdAt")
    private String createdat;
    @SerializedName("readBy")
    private List<String> readby;
    @SerializedName("sentTo")
    private List<String> sentto;
    @SerializedName("room")
    private String room;
    @SerializedName("author")
    private String author;
    @SerializedName("message")
    private String message;
    @SerializedName("_id")
    private String Id;
    @SerializedName("authorDetail")
    private User authorDetail;

    private Calendar calendarCreated;

    public Calendar getCalendarCreated() {
        if (calendarCreated == null) {
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
            calendarCreated = Calendar.getInstance();
            try {
                Date utcDate = simpleDateFormatUTC.parse(
                        createdat
                );
                String localDate = simpleDateFormatLocal.format(
                        utcDate
                );
                calendarCreated.setTime(
                        simpleDateFormatLocal.parse(localDate)
                );
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return calendarCreated;
    }

    public Chat() {
    }

    public Chat(String updatedat, String createdat, List<String> readby, List<String> sentto, String room, String author, String message, String id, User authorDetail) {
        this.updatedat = updatedat;
        this.createdat = createdat;
        this.readby = readby;
        this.sentto = sentto;
        this.room = room;
        this.author = author;
        this.message = message;
        Id = id;
        this.authorDetail = authorDetail;
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

    public List<String> getReadby() {
        return readby;
    }

    public void setReadby(List<String> readby) {
        this.readby = readby;
    }

    public List<String> getSentto() {
        return sentto;
    }

    public void setSentto(List<String> sentto) {
        this.sentto = sentto;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public User getAuthorDetail() {
        return authorDetail;
    }

    public void setAuthorDetail(User authorDetail) {
        this.authorDetail = authorDetail;
    }
}
