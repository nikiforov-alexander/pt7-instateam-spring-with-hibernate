package com.techdegree.instateam.model;

public enum ProjectStatus {
    ACTIVE("Active","lightgreen"),
    ARCHIVED("Archived", "black"),
    NOT_STARTED("Not Started", "#ffffff");

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
