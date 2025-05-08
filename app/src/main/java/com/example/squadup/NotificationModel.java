package com.example.squadup;

public class NotificationModel {
    private String title;
    private String body;
    private long timestamp;

    public NotificationModel() {}

    public NotificationModel(String title, String body, long timestamp) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
