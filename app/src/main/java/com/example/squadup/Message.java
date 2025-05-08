package com.example.squadup;

public class Message {
    private String senderUid;
    private String receiverUid;
    private String message;
    private long timestamp;

    public Message() {}

    public Message(String senderUid, String receiverUid, String message, long timestamp) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
