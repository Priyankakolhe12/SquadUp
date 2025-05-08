package com.example.squadup.models;

public class Message {
    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;

    public Message() {}

    public Message(String senderId, String receiverId, String content, String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
