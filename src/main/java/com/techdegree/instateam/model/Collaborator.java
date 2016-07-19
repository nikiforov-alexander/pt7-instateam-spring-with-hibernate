package com.techdegree.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "collaborators")
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // same as name in Role class
    @Column(name = "NAME", columnDefinition = "VARCHAR")
    @Pattern(regexp = "\\s*[a-zA-Z0-9]+(\\s+[a-zA-Z0-9]+)*\\s*")
    String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToOne
    Role role;
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Collaborator() {
    }
}
