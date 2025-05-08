package com.example.squadup;

import java.util.List;

public class Event {
    private String eventId;
    private String title;
    private String description;
    private String date;
    private String mode; // Online or Offline
    private List<String> skillsRequired;
    private String eventLink;
    private String platform;
    private String lastDateToRegister;
    private String notes;
    private String posterUrl;

    public Event() {
    }

    public Event(String eventId, String title, String description, String date, String mode,
                 List<String> skillsRequired, String eventLink, String platform,
                 String lastDateToRegister, String notes, String posterUrl) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.mode = mode;
        this.skillsRequired = skillsRequired;
        this.eventLink = eventLink;
        this.platform = platform;
        this.lastDateToRegister = lastDateToRegister;
        this.notes = notes;
        this.posterUrl = posterUrl;
    }

    public String getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getMode() { return mode; }
    public List<String> getSkillsRequired() { return skillsRequired; }
    public String getEventLink() { return eventLink; }
    public String getPlatform() { return platform; }
    public String getLastDateToRegister() { return lastDateToRegister; }
    public String getNotes() { return notes; }
    public String getPosterUrl() { return posterUrl; }
}
