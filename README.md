# Techdegree project 7
### InstaTeam with Spring and Hibernate
<hr>
### Table of Contents
### Installation instructions
* [Eclipse installation instructions.] (#eclipse)

<hr>

### Misc
- [Structure of the project] (#structure)
- [Quick Links to files and directories] (#links)

<hr>

### Tasks
* [1.] (#task-1) 
    In the IDE of your choice, create a Gradle project. Add 
    dependencies for 
    Spring Boot with Thymeleaf, Spring ORM, Hibernate, Apache DBCP, and H2. 
    Create the directory and package structure of the application. Save all 
    static assets into the proper project directory.
    <hr>
* [2.] (#task-2) 
    Create the configuration files for Hibernate and an H2 database connection.
    <hr>
* [3.] (#task-3) 
    Create a Java class for starting the application as a `SpringApplication` 
    and a Spring configuration class with two `@Bean` methods:
    - Method for initializing a `LocalSessionFactoryBean`
    - Method for initializing a `DataSource`
    <hr>
* [4.] (#task-4) 
    Create the `Role` model class, which represents the roles each project could 
    contain, and that need to be filled. Each role will have the following 
    pieces of information associated with it:
    - `id`: auto-generated numeric identifier to serve as the table’s primary 
        key
    - `name`: alphanumeric, reader-friendly name to be displayed. Example 
        role names might be “developer”, “designer”, or “QA engineer”. 
        This is a required field for data validation.
    - Getters and setters for all fields
    - Default constructor

    <hr>
* [5.] (#task-5) 
    Create the `Role` model class, which represents the roles each project could 
    contain, and that need to be filled. Each role will have the following 
    pieces of information associated with it:
    - `id`: auto-generated numeric identifier to serve as the table’s primary 
        key
    - `name`: alphanumeric, reader-friendly name to be displayed. Example 
        role names might be “developer”, “designer”, or “QA engineer”. 
        This is a required field for data validation.
    - Getters and setters for all fields
    - Default constructor

    <hr>

* [6.] (#task-6) 
    Create the `Collaborator` model class, which represents a person who 
    is a candidate for working on any given project. Each collaborator 
    should contain the following:
    - `id`: auto-generated numeric identifier to serve as the table’s primary 
        key
    - `name`: first and last name of the collaborator. This is a required field 
        for data validation.
    - `role`: the single `Role` object that represents this collaborator’s skill. 
        For proper data association, it’s important to keep in mind that 
        there could be `many` collaborators associated with any `one` role. 
        This is a required field for data validation.
    - Getters and setters for all fields
    - Default constructor

    <hr>
* [7.] (#task-7) 
    Create the `Project` model class, which represents a project for which a 
    project manager is seeking collaborators. 
    Each project should contain the following:
    - `id`: auto-generated numeric identifier to serve as the table’s primary 
        key
    - `name`: alphanumeric, reader-friendly name to be displayed. 
        This is a required field for data validation.
    - `description`: longer description of the project. 
        This is a required field for data validation.
    - `status`: alphanumeric status of the project, 
        for example “recruiting” or “on hold”
    - `rolesNeeded`: collection of Role objects representing all 
        roles needed for this project, regardless of whether or not 
        they’ve been filled. For proper data association, keep 
        in mind that there could be `many` projects that contain `many` `Role` 
        objects. That is, each project can have many roles that 
        it needs, and each role can be needed by many projects.
    - `collaborators`: collection of `Collaborator` objects representing any 
        collaborators that have been assigned to this project. 
        For data association, use the fact that there could be `many` 
        projects that contain `many` Collaborator objects. 
        That is, each project can have many 
        collaborators, and each collaborator 
        can work on many projects.
    - Getters and setters for all fields
    - Default constructor

    <hr>
* [8.] (#task-8) 
    Add JPA annotations to all model classes.
    <hr>
* [9.] (#task-9) 
    Create a DAO interface and implementation for each model class.
    <hr>
* [10.] (#task-10) 
    Create a service interface and implementation for each model class. 
    <hr>
* [11.] (#task-11) 
    Create the `RoleController` and Thymeleaf views necessary for viewing, 
    adding, and editing roles.
    <hr>
* [12.] (#task-12) 
    Create the `CollaboratorController` and Thymeleaf views 
    necessary for viewing, 
    adding, and editing collaborators.
    <hr>
* [13.] (#task-13) 
    Create the `ProjectController` and Thymeleaf views necessary for viewing, 
    adding, and editing projects, without including the ability to 
    assign each role to a specific collaborator.
    <hr>
* [14.] (#task-14) 
    Add the methods to `ProjectController`, and the Thymeleaf views 
    necessary for assigning and unassigning collaborators to and from a 
    project’s needed roles.
    <hr>
