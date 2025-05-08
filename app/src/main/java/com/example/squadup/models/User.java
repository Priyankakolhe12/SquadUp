package com.example.squadup.models;

public class User {
    private String email;
    private String profilePicture;
    private String bio;
    private String[] skills;
    private String[] preferences;

    public User() {}

    public User(String email, String profilePicture, String bio, String[] skills, String[] preferences) {
        this.email = email;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.skills = skills;
        this.preferences = preferences;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public String[] getSkills() {
        return skills;
    }

    public String[] getPreferences() {
        return preferences;
    }
}
