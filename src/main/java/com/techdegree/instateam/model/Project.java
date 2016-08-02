package com.techdegree.instateam.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    // primary key - ID, should be changed to Long to implement Serializable
    // but that's when I get testing for this done
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // project name: alphanumeric VARCHAR
    @Column(name = "NAME", columnDefinition = "VARCHAR")
    @NotNull
    @Pattern(regexp = "\\s*[a-zA-Z0-9]+(\\s+[a-zA-Z0-9]+)*\\s*",
        message = "Name must consist of alphanumeric characters: a-Z, 0-9")
    private String name;

    // project description: for now it simply cannot be empty or null
    // can be changed later
    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR")
    @NotNull(message = "Description cannot be empty")
    private String description;

    // project roles
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> rolesNeeded;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Collaborator> collaborators;

    // Project status is enum class, see definition, can be ACTIVE,
    // NOT_STARTED, or ARCHIVED. In table it comes as INTEGER, has
    // also style attributes, that makes project look different depending on
    // status
    @Enumerated
    private ProjectStatus status;

    //
    // Getters and setters
    //
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setRolesNeeded(List<Role> rolesNeeded) {
        this.rolesNeeded = rolesNeeded;
    }
    public List<Role> getRolesNeeded() {
        return rolesNeeded;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public ProjectStatus getStatus() {
        return status;
    }
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

}
