package com.example.squadup;

import java.util.List;

public class EventModel {

    private String eventId;
    private String title;
    private String description;
    private String date;
    private String mode;
    private List<String> skillsRequired;
    private String eventLink;
    private String platform;
    private String lastDateToRegister;
    private String notes;
    private String posterUrl;

    // Empty constructor (Important for Firebase)
    public EventModel() {}

    // Constructor
    public EventModel(String eventId, String title, String description, String date, String mode, List<String> skillsRequired, String eventLink, String platform, String lastDateToRegister, String notes, String posterUrl) {
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

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(List<String> skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLastDateToRegister() {
        return lastDateToRegister;
    }

    public void setLastDateToRegister(String lastDateToRegister) {
        this.lastDateToRegister = lastDateToRegister;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
