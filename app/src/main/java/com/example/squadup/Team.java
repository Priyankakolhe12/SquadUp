package com.example.squadup;

import java.util.List;

public class Team {
    public String id;
    public String leaderId;
    public String teamName;
    public String eventName;
    public String description;
    public List<String> members;

    public Team() {}

    public Team(String leaderId, String teamName, String eventName, String description, List<String> members) {
        this.leaderId = leaderId;
        this.teamName = teamName;
        this.eventName = eventName;
        this.description = description;
        this.members = members;
    }
}
