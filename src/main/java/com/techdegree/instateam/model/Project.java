package com.techdegree.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "\\s*[a-zA-Z0-9]+(\\s+[a-zA-Z0-9]+)*\\s*",
        message = "Name must consist of alphanumeric characters: a-Z, 0-9")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR")
    @NotNull(message = "Description cannot be empty")
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
    private ProjectStatus status;
    public ProjectStatus getStatus() {
        return status;
    }
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rolesNeeded=" + rolesNeeded +
                ", collaborators=" + collaborators +
                ", status=" + status +
                '}';
    }
}
