package com.techdegree.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME", columnDefinition = "VARCHAR")
    @NotNull
    @Pattern(regexp = "\\s*[a-zA-Z0-9]+(\\s+[a-zA-Z0-9]+)*\\s*")
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR")
    @NotNull
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany
    private List<Role> rolesNeeded;
    public void setRolesNeeded(List<Role> rolesNeeded) {
        this.rolesNeeded = rolesNeeded;
    }
    public List<Role> getRolesNeeded() {
        return rolesNeeded;
    }

    @ManyToMany
    private List<Collaborator> collaborators;
    public List<Collaborator> getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }


    @Enumerated
    public Status status;
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        ACTIVE("Active","#123456"),
        ARCHIVED("Archived", "#123456"),
        NOT_STARTED("Not Started", "#123456");

        public String getDescription() {
            return description;
        }

        public String getColor() {
            return color;
        }

        private final String description;
        private final String color;
        Status(String description, String color) {
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
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rolesNeeded=" + rolesNeeded +
                ", collaborators=" + collaborators +
                '}';
    }
}
