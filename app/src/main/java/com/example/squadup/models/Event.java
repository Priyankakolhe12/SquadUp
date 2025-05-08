package com.example.squadup.models;

public class Event {
    private String title;
    private String date;
    private String organizer;
    private String mode;
    private String skills;

    public Event() {}

    public Event(String title, String date, String organizer, String mode, String skills) {
        this.title = title;
        this.date = date;
        this.organizer = organizer;
        this.mode = mode;
        this.skills = skills;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getMode() {
        return mode;
    }

    public String getSkills() {
        return skills;
    }
}
