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
