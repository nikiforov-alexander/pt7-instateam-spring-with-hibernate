package com.techdegree.instateam.model;

public enum ProjectStatus {
    ACTIVE("Active","#72c38d"),
    ARCHIVED("Archived", "#bbbab9"),
    NOT_STARTED("Not Started", "white");

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    private final String description;
    private final String color;
    ProjectStatus(String description, String color) {
        this.description = description;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Status{" +
                "description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
