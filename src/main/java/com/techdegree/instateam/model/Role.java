package com.techdegree.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
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
    @Pattern(regexp = "\\s*[a-zA-Z0-9]+(\\s+[a-zA-Z0-9]+)*\\s*")
    int name;
    public int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
    }

    public Role() {
        // default constructor for JPA
    }
}
