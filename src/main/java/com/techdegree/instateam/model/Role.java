package com.techdegree.instateam.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    // id column, member, auto generated
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // pattern can be read like this:
    // \s* - leading spaces, zero or more
    // [a-zA-Z0-9]+ - alphanumeric characters, one or more
    // (\s+[a-zA-Z0-9]+)* - Group: space (one or more) and new alphanumeric char
    //                      one or more. This group can be repeated zero
    //                      or more time, allowing many word combinations
    //                      like "a b", "a b c", and etc.
    // \s* - trailing spaces, zero or more
    // see RoleTest.java for more
    // for now no restriction on minimum or maximum length
    // name with just spaces is not allowed
    // inspired from
    // http://stackoverflow.com/questions/15472764/regular-expression-to-allow-spaces-between-words
    //
    // name column, VARCHAR, notnull, has pattern and message for validation
    @Column(name = "NAME", columnDefinition = "VARCHAR")
    @NotNull
    @Pattern(regexp = "\\s*[a-zA-Z0-9]+(\\s+[a-zA-Z0-9]+)*\\s*",
            message = "Name must consist of alphanumeric characters: a-Z, 0-9")
    private String name;

    // collaborators column, mapped by role, many collaborators has one role
    // on removal of role, right now collaborators are removed. Later will be
    // changed.
    // This list of collaborators is fetched eagerly, not lazily, I will
    // investigate in this later.
    @OneToMany(
            mappedBy = "role",
            fetch = FetchType.EAGER
    )
    private List<Collaborator> collaborators;

    // projects, are used to created relationship between Project class,
    // mapped by "rolesNeeded"
    @ManyToMany(mappedBy = "rolesNeeded")
    private List<Project> projects;

    //  Getters and setters

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

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public List<Project> getProjects() {
        return projects;
    }
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    public Role() {
        // default constructor for JPA
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        return name != null ? name.equals(role.name) : role.name == null;

    }
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
