package com.example.squadup.models;

import java.util.List;

public class Team {
    private List<String> members;
    private String status;

    public Team() {}

    public Team(List<String> members, String status) {
        this.members = members;
        this.status = status;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getStatus() {
        return status;
    }
}
