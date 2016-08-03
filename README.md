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

### Extra Credit
* [15.] (#task-15) 
    Extract the common code of each DAO implementation to an abstract class 
    that the DAO implementations extend.
    <hr>
* [16.] (#task-16) 
    Add the ability to delete projects, roles, and contractors and 
    ensure data integrity for all relationships. 
    For example, when a collaborator is deleted, make sure that all 
    roles previously assigned to this collaborator become unassigned.
    <hr>
* [17.] (#task-17) 
    Include a start date on projects, and sort chronologically by start date on 
    the project index view.
    <hr>

<!--Links-->

<!--External URLs-->
[spark_blog_readme]: 
    https://github.com/nikiforov-alexander/pt4-spark-blog#eclipse "https://github.com/nikiforov-alexander/pt4-spark-blog#eclipse"
[codesenior_generic_dao_service_impl]:
    http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation

<!--Directories-->
[data]: data "data directory with H2 Database"
[resources]:
    src/main/resources "directory with static resources, application properties file and hibernate configuration file: src/main/resources"
[templates]:
    src/main/resources/templates "Thymeleaf templates directory: src/main/resources/templates"
[css]:
    src/main/resources/static/css "Directory with CSS files: src/main/resources/static/css"
[initial_project_files]:
    initial-project-files "directory with initial project files from Treeshouse"

<!--Files-->
[instateam_mv_db]: 
    data/instateam.mv.db "H2 databased used in project: instateam.mv.db"
[h2-1.4.192.jar]:
    h2-1.4.192.jar "H2 database jar file, used to launch server h2-1.4.192.jar"

<!--Configuration files-->
[hibernate_cfg_xml]: 
    src/main/resources/hibernate.cfg.xml "Hibernate configuration file: src/main/resources/hibernate.cfg.xml"
[application_properties]:
    src/main/resources/application.properties "Spring application properties file: application.properties"

<!--JavaScript files-->
[app_js]:
    src/main/resources/static/app.js "JavaScript file with all JavaScript functions used: src/main/resources/static/app.js"
[favicon_ico]:
    src/main/resources/static/favicon.ico "Icon used in tabs of the website: src/main/resources/static/favicon.ico"

<!--CSS files-->
[normalize_css]:
    src/main/resources/static/css/normalize.css "Normalize CSS, unchanged: src/main/resources/static/css/normalize.css"
[site_css]:
    src/main/resources/static/css/site.css "Main CSS file with custom styles added by me: src/main/resources/static/css/site.css"

<!--Eclipse files-->
[.project]:
    .project "Eclipse .project file, generated by IntellijIdea"
[.userlibraries]:
pt7-instateam-spring-with-hibernate.userlibraries "Eclipse .userlibraries file, generated by IntellijIdea: pt7-instateam-spring-with-hibernate.userlibraries"

<!--Thymeleaf template files-->
[layout.html]:
    ./src/main/resources/templates/layout.html "./src/main/resources/templates/layout.html"
[error.html]:
    ./src/main/resources/templates/error.html "./src/main/resources/templates/error.html"
[index.html]:
    ./src/main/resources/templates/index.html "./src/main/resources/templates/index.html"
[project-details.html]:
    ./src/main/resources/templates/project/project-details.html "./src/main/resources/templates/project/project-details.html"
[project-edit.html]:
    ./src/main/resources/templates/project/project-edit.html "./src/main/resources/templates/project/project-edit.html"
[project-collaborators.html]:
    ./src/main/resources/templates/project/project-collaborators.html "./src/main/resources/templates/project/project-collaborators.html"
[collaborator-details.html]:
    ./src/main/resources/templates/collaborator/collaborator-details.html "./src/main/resources/templates/collaborator/collaborator-details.html"
[collaborators.html]:
    ./src/main/resources/templates/collaborator/collaborators.html "./src/main/resources/templates/collaborator/collaborators.html"
[role-details.html]:
    ./src/main/resources/templates/role/role-details.html "./src/main/resources/templates/role/role-details.html"
[roles.html]:
    ./src/main/resources/templates/role/roles.html "./src/main/resources/templates/role/roles.html"

<!--Classes-->
[RoleTest]:
    ./src/test/java/com/techdegree/instateam/model/RoleTest.java "./src/test/java/com/techdegree/instateam/model/RoleTest.java"
[RoleService]:
    ./src/main/java/com/techdegree/instateam/service/RoleService.java "./src/main/java/com/techdegree/instateam/service/RoleService.java"
[CollaboratorServiceImpl]:
    ./src/main/java/com/techdegree/instateam/service/CollaboratorServiceImpl.java "./src/main/java/com/techdegree/instateam/service/CollaboratorServiceImpl.java"
[CollaboratorService]:
    ./src/main/java/com/techdegree/instateam/service/CollaboratorService.java "./src/main/java/com/techdegree/instateam/service/CollaboratorService.java"
[GenericService]:
    ./src/main/java/com/techdegree/instateam/service/GenericService.java "./src/main/java/com/techdegree/instateam/service/GenericService.java"
[RoleServiceImpl]:
    ./src/main/java/com/techdegree/instateam/service/RoleServiceImpl.java "./src/main/java/com/techdegree/instateam/service/RoleServiceImpl.java"
[ProjectServiceImpl]:
    ./src/main/java/com/techdegree/instateam/service/ProjectServiceImpl.java "./src/main/java/com/techdegree/instateam/service/ProjectServiceImpl.java"
[GenericServiceImpl]:
    ./src/main/java/com/techdegree/instateam/service/GenericServiceImpl.java "./src/main/java/com/techdegree/instateam/service/GenericServiceImpl.java"
[ProjectService]:
    ./src/main/java/com/techdegree/instateam/service/ProjectService.java "./src/main/java/com/techdegree/instateam/service/ProjectService.java"
[Application]:
    ./src/main/java/com/techdegree/instateam/Application.java "./src/main/java/com/techdegree/instateam/Application.java"
[FlashMessage]:
    ./src/main/java/com/techdegree/instateam/web/FlashMessage.java "./src/main/java/com/techdegree/instateam/web/FlashMessage.java"
[CollaboratorController]:
    ./src/main/java/com/techdegree/instateam/web/controller/CollaboratorController.java "./src/main/java/com/techdegree/instateam/web/controller/CollaboratorController.java"
[ProjectController]:
    ./src/main/java/com/techdegree/instateam/web/controller/ProjectController.java "./src/main/java/com/techdegree/instateam/web/controller/ProjectController.java"
[RoleController]:
    ./src/main/java/com/techdegree/instateam/web/controller/RoleController.java "./src/main/java/com/techdegree/instateam/web/controller/RoleController.java"
[Role]:
    ./src/main/java/com/techdegree/instateam/model/Role.java "./src/main/java/com/techdegree/instateam/model/Role.java"
[ProjectStatus]:
    ./src/main/java/com/techdegree/instateam/model/ProjectStatus.java "./src/main/java/com/techdegree/instateam/model/ProjectStatus.java"
[Project]:
    ./src/main/java/com/techdegree/instateam/model/Project.java "./src/main/java/com/techdegree/instateam/model/Project.java"
[Collaborator]:
    ./src/main/java/com/techdegree/instateam/model/Collaborator.java "./src/main/java/com/techdegree/instateam/model/Collaborator.java"
[NotFoundException]:
    ./src/main/java/com/techdegree/instateam/exception/NotFoundException.java "./src/main/java/com/techdegree/instateam/exception/NotFoundException.java"
[ProjectDaoImpl]:
    ./src/main/java/com/techdegree/instateam/dao/ProjectDaoImpl.java "./src/main/java/com/techdegree/instateam/dao/ProjectDaoImpl.java"
[RoleDaoImpl]:
    ./src/main/java/com/techdegree/instateam/dao/RoleDaoImpl.java "./src/main/java/com/techdegree/instateam/dao/RoleDaoImpl.java"
[RoleDao]:
    ./src/main/java/com/techdegree/instateam/dao/RoleDao.java "./src/main/java/com/techdegree/instateam/dao/RoleDao.java"
[CollaboratorDao]:
    ./src/main/java/com/techdegree/instateam/dao/CollaboratorDao.java "./src/main/java/com/techdegree/instateam/dao/CollaboratorDao.java"
[GenericDaoImpl]:
    ./src/main/java/com/techdegree/instateam/dao/GenericDaoImpl.java "./src/main/java/com/techdegree/instateam/dao/GenericDaoImpl.java"
[GenericDao]:
    ./src/main/java/com/techdegree/instateam/dao/GenericDao.java "./src/main/java/com/techdegree/instateam/dao/GenericDao.java"
[CollaboratorDaoImpl]:
    ./src/main/java/com/techdegree/instateam/dao/CollaboratorDaoImpl.java "./src/main/java/com/techdegree/instateam/dao/CollaboratorDaoImpl.java"
[ProjectDao]:
    ./src/main/java/com/techdegree/instateam/dao/ProjectDao.java "./src/main/java/com/techdegree/instateam/dao/ProjectDao.java"
[AppConfig]:
    ./src/main/java/com/techdegree/instateam/config/AppConfig.java "./src/main/java/com/techdegree/instateam/config/AppConfig.java"
[DataConfig]:
    ./src/main/java/com/techdegree/instateam/config/DataConfig.java "./src/main/java/com/techdegree/instateam/config/DataConfig.java"


### Eclipse Installation instructions
<hr> <a id="eclipse"></a>
    I generated necessary [.project] and 
    [.userlibraries]. This time without `.classpath`. Decided to experiment
    I tested it once again. As always there is a problem with `BuildPath` in 
    `Eclipse`.
    So it is better to set `src/main/java` as a source in `BuildPath`
    options, if `Eclipse` does not understand it. Here is a link to old
    [Spark Blog README.md][spark-blog-readme] just in case. 
    <br>
    *Important*: Before run the app itself, we have to launch the Server:
    This is done in the following way:
    - On the classpath there is "H2" database jar file: [h2-1.4.192.jar]
    - In order to launch server, one has to go to project directory:
        This is important, because pathway to database in [data] 
        directory is relative: `./data/instateam`, see 
        [hibernate.cfg.xml]. (Although I didn't try to run server from
        other directory - May be it will work too).
    - And there run in terminal the following 
        `java -cp h2-1.4.192.jar org.h2.tools.Server`
    - Then should be opened Firefox window, where one can look at how
        my database [instateam.mv.db] looks like, keeping in mind
        setting of [hibernate.cfg.xml]
    
    After that `bootRun` Gradle task can be executed to run the 
    application.
<hr>
### Tasks
1. <a id="task-1"></a>
