package com.example.squadup;

public class Invite {
    private String fromUid;
    private String toUid;
    private String status;
    private String message;
    private String inviteId; // Custom for Firestore ID

    public Invite() {}

    public Invite(String fromUid, String toUid, String status, String message) {
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.status = status;
        this.message = message;
    }

    public String getFromUid() {
        return fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }
}
