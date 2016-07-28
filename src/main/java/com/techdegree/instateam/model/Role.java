package com.techdegree.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
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

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    private List<Collaborator> collaborators;

    @ManyToMany(mappedBy = "rolesNeeded",
    fetch = FetchType.EAGER)
    private List<Project> projects;

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
