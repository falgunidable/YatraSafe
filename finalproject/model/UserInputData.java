package com.example.finalproject.model;

import java.io.Serializable;

public final class UserInputData implements Serializable {
    private String name;
    private String suggestion;
    private String location;
    private String date;

    public UserInputData() {
    }

    public UserInputData(String name, String suggestion, String location, String date) {
        this.name = name;
        this.suggestion = suggestion;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
