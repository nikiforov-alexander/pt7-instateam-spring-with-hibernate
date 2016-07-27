package com.techdegree.instateam.model;

public enum ProjectStatus {
    ACTIVE("Active", "#72c38d", "active"),
    ARCHIVED("Archived", "#bbbab9", "archived"),
    NOT_STARTED("Not Started", "white", "not-started");

    public String getDescription() {
        return description;
    }
    public String getColor() {
        return color;
    }
    public String getStyleClass() {
        return styleClass;
    }

    private final String description;
    private final String color;
    private final String styleClass;

    ProjectStatus(String description, String color, String styleClass) {
        this.description = description;
        this.color = color;
        this.styleClass = styleClass;
    }
}
