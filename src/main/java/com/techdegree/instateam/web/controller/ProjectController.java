package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.exception.NotFoundException;
import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.ProjectStatus;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.CollaboratorService;
import com.techdegree.instateam.service.ProjectService;
import com.techdegree.instateam.service.RoleService;
import com.techdegree.instateam.web.FlashMessage;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RoleService roleService;

    // main page with all projects
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listProjects(Model model) {
        // Get all projects
        List<Project> projects = projectService.findAll();
        // add projects to model
        model.addAttribute("projects", projects);
        return "index";
    }

    // add new project page
    @RequestMapping("/projects/add-new")
    public String addNewProject(Model model,
                                RedirectAttributes redirectAttributes) {
        // add roles available to model
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        // if there are no roles we redirect to role page
        if (roles.size() == 0) {
            // set flash message to roles
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                    "No roles available for project to be created, " +
                            "please add some.",
                    FlashMessage.Status.SUCCESS
            ));
            return "redirect:/roles";
        }
        // add statuses values to model
        // first of all add default one
        model.addAttribute("defaultStatus", ProjectStatus.NOT_STARTED);
        // then add others. Here for simplicity there are no for each loops,
        // because we have two statuses. Can be improved by necessity
        List<ProjectStatus> projectStatusListWithoutDefaultOne =
                new ArrayList<>();
        projectStatusListWithoutDefaultOne.add(ProjectStatus.ARCHIVED);
        projectStatusListWithoutDefaultOne.add(ProjectStatus.ACTIVE);
        model.addAttribute("statusesWithoutDefaultOne",
                projectStatusListWithoutDefaultOne);
        // we add action attribute because this template
        // will be re-used for both edit and add new project
        model.addAttribute("action", "/projects/add-new");
        // if model contains project, e.g. when we
        // user made a mistake, model will be filled with
        // with previously entered data
        if (!model.containsAttribute("project")) {
            // if not we add fresh new Project
            model.addAttribute("project", new Project());
        }
        return "/project/project-edit";
    }

    // add new project POST request
    @RequestMapping(value = "/projects/add-new", method = RequestMethod.POST)
    public String saveNewProject(
            @Valid Project project,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // first we obtain roles needed array from project object filled in
        // form. This roles list will have null values for roles, that were
        // not selected, hence we simply filter these roles, to get valid
        // array of roles selected by user
        List<Role> rolesNeeded = project.getRolesNeeded().stream()
                .filter(role -> role != null)
                .collect(Collectors.toList());
        // here unfortunately, I cannot directly use validation, because I
        // have this error:
        // Field error in object 'project' on field 'rolesNeeded[0]': rejected value
        // which is different from error on field 'rolesNeeded'
        // In order to avoid this error, because I know that my roles are
        // selected properly, all I care about is that user selected some roles.
        // So if size of this filtered array will be zero
        if (rolesNeeded.size() == 0) {
            // I manually reject whole 'rolesNeeded' field, so that later it
            // can be processed just like any other error fields
            // found solution here:
            // http://stackoverflow.com/questions/12107503/adding-error-message-to-spring-3-databinder-for-custom-object-fields
            result.rejectValue("rolesNeeded",
                    "error.project",
                    "Please select at least one role");
        }
        // here I check for error for every field manually including them in
        // unfortunately, because of reject error see above.
        if (result.hasFieldErrors("name")
                || result.hasFieldErrors("description")
                || result.hasFieldErrors("status")
                || result.hasFieldErrors("rolesNeeded")) {
            // add flash attribute of project
            redirectAttributes.addFlashAttribute("project", project);
            // add flash attribute with errors for every field
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.project",
                    result);
            // redirect back to form
            return "redirect:/projects/add-new";
        }
        // add right neededRoles to project
        project.setRolesNeeded(rolesNeeded);
        // VERY important here we "save" new project to database
        projectService.save(project);
        // add flash of successful save on top of the redirected page
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Project '" + project.getName() +
                "' was successfully saved!",
                FlashMessage.Status.SUCCESS
        ));
        // redirect back to main page
        return "redirect:/";
    }

    // generate roles needed list synchronized with all roles list, by
    // index and size, because initially we can have:
    // all roles: [Role(1), Role(2)]
    // project.rolesNeeded: [Role(2)]
    // After synchronization will be
    // [null, Role(2)]
    // used in method below `editProject`
    private List<Role> generateSynchronizedWithAllRolesRolesNeededList(
            List<Role> allRoles, List<Role> rolesNeeded
    ) {
        // initialize synchronized array
        List<Role> synchronizedRolesNeededListWithNulls =
                new ArrayList<>();
        // cycle through all roles
        for (Role role: allRoles) {
            // if rolesNeeded has role, we put this role in new list
            // at the right index. Else we put null
            if (rolesNeeded.contains(role)) {
                synchronizedRolesNeededListWithNulls.add(role);
            } else {
                synchronizedRolesNeededListWithNulls.add(null);
            }
        }
        return synchronizedRolesNeededListWithNulls;
    }


    // edit project page
    @RequestMapping("/projects/{projectId}/edit")
    public String editProject(
            @PathVariable int projectId,
            Model model,
            RedirectAttributes redirectAttributes) {
        // find project by id
        Project project = projectService.findById(projectId);
        // if not found return 404
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        // add roles available to model
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        // if there are no roles we redirect to role page
        if (roles.size() == 0) {
            // set flash message to roles
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                    "No roles available for project to be created, " +
                            "please add some.",
                    FlashMessage.Status.SUCCESS
            ));
            return "redirect:/roles";
        }
        // add statuses values to model
        // first of all add default one
        model.addAttribute("defaultStatus", ProjectStatus.NOT_STARTED);
        // then add others. Here for simplicity there are no for each loops,
        // because we have two statuses. Can be improved by necessity
        List<ProjectStatus> projectStatusListWithoutDefaultOne =
                new ArrayList<>();
        projectStatusListWithoutDefaultOne.add(ProjectStatus.ARCHIVED);
        projectStatusListWithoutDefaultOne.add(ProjectStatus.ACTIVE);
        model.addAttribute("statusesWithoutDefaultOne",
                projectStatusListWithoutDefaultOne);
        // we add action attribute because this template
        // will be re-used for both "projects/save" and
        // "projects/add-new" project
        model.addAttribute("action", "/projects/save");

        // Problem: project roles that were selected by user will not be
        //   shown properly if rolesNeeded of project and allRoles from database
        //   are not synchronized: that because of the way we interact with
        //   thymeleaf: cycle in cycle.
        // Solution:
        //   We have to make projects rolesNeeded same size with all roles,
        //   putting on the missing position nulls. This way our arrays being
        //   synchronized by size and index will be correctly displayed in
        //   thymeleaf template
        project.setRolesNeeded(
                generateSynchronizedWithAllRolesRolesNeededList(
                        roles, project.getRolesNeeded()
                )
        );

        // if model contains project, e.g. when we
        // user made a mistake, model will be filled with
        // with previously entered data
        if (!model.containsAttribute("project")) {
            // if not we add edited Project
            model.addAttribute("project", project);
        }
        return "/project/project-edit";
    }

    // method used in saveExistingProject method below
    // it generates disappeared roles needed from two Lists:
    // old roles needed and new roles needed: simple lambda with
    // `does not contains` condition, collected to list
    private List<Role> generateDisappearedRolesNeededArray(
            List<Role> oldRolesNeeded,
            List<Role> newRolesNeeded) {
        return oldRolesNeeded.stream()
                .filter( role -> !newRolesNeeded.contains(role) )
                .collect(Collectors.toList());
    }

    // saveOrUpdate existing project POST request
    // this method is exactly like saveNewProject. But I don't know
    // yet how to re use the method here. It is definitely something
    // to do
    @RequestMapping(value = "/projects/save",
            method = RequestMethod.POST)
    public String saveExistingProject(
            @Valid Project project,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // first we obtain roles needed array from project object filled in
        // form. This roles list will have null values for roles, that were
        // not selected, hence we simply filter these roles, to get valid
        // array of roles selected by user
        List<Role> rolesNeeded = project.getRolesNeeded().stream()
                .filter(role -> role != null)
                .collect(Collectors.toList());

        // here unfortunately, I cannot directly use validation, because I
        // have this error:
        // Field error in object 'project' on field 'rolesNeeded[0]': rejected value
        // which is different from error on field 'rolesNeeded'
        // In order to avoid this error, because I know that my roles are
        // selected properly, all I care about is that user selected some roles.
        // So if size of this filtered array will be zero
        if (rolesNeeded.size() == 0) {
            // I manually reject whole 'rolesNeeded' field, so that later it
            // can be processed just like any other error fields
            // found solution here:
            // http://stackoverflow.com/questions/12107503/adding-error-message-to-spring-3-databinder-for-custom-object-fields
            result.rejectValue("rolesNeeded",
                    "error.project",
                    "Please select at least one role");
        }

        // here I check for error for every field manually including them in
        // unfortunately, because of reject error see above.
        if (result.hasFieldErrors("name")
                || result.hasFieldErrors("description")
                || result.hasFieldErrors("status")
                || result.hasFieldErrors("rolesNeeded")) {
            // add flash attribute of project
            redirectAttributes.addFlashAttribute("project", project);
            // add flash attribute with errors for every field
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.project",
                    result);
            // redirect back to form
            return "redirect:/projects/" + project.getId() + "/edit";
        }

        // add right neededRoles to project
        project.setRolesNeeded(rolesNeeded);

        // Goal: before we update project in database we add collaborators if
        //   they exist. So that we don't lost collaborators when we
        //   edit existing project:
        // Solution:
        //   1. we get project from database : we know it exists
        Project projectFromDatabase =
                projectService.findById(project.getId());
        //   2. we update edited project with collaborators
        project.setCollaborators(projectFromDatabase.getCollaborators());

        // Problem: when there old roles needed generated for project
        //   are disappeared upon edit of the project then we have
        //   to remove collaborators that had these old needed roles
        // Solution:
        //   1. Generate array of disappeared roles
        //   2. Cycle through each disappered role
        //     * For each disappeared role, check project collaborator
        //       - if collaborator had disappered role, generate new
        //         collaborators list for project
        //   3. set new collaborators to project

        // Step-by-step solution
        // 0. set new collaborators list
        List<Collaborator> collaboratorsWithoutThoseWhoseRolesDisappear =
                new ArrayList<>();
        // 1. get old roles needed from database project
        List<Role> oldRolesNeeded = projectFromDatabase.getRolesNeeded();
        // 2. cycle through generated disappered roles
        for (Role disappearedRole :
                generateDisappearedRolesNeededArray(
                        oldRolesNeeded,
                        rolesNeeded)
                ) {
            // 3. cycle through project collaborators
            // 4. if collaborator's role has disappeared
            // 5. generate new updated list of collaborators
            collaboratorsWithoutThoseWhoseRolesDisappear
                .addAll(
                    project.getCollaborators().stream()
                            .filter(
                            collaborator ->
                                    collaborator.getRole().getId()
                                    != disappearedRole.getId()
                            )
                            .collect(Collectors.toList())
                );
        }
        // 6. set new collaborators for project
        project.setCollaborators(collaboratorsWithoutThoseWhoseRolesDisappear);

        // Important: here we UPDATE project to database
        projectService.update(project);

        // add flash of successful update on top of the redirected page
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Project '" + project.getName() +
                        "' was successfully saved!",
                FlashMessage.Status.SUCCESS
        ));
        // redirect back to main page
        return "redirect:/";
    }

    // this method is used in both project details and project collaborators
    // page: `projectDetails` and `editProjectCollaborators`. It is needed
    // to create synchronized with rolesNeeded list of collaborators, ultimately
    // leading to proper list of roles and collaborators together visible to
    // user
    private List<Collaborator>
        generateSynchronizedWithRolesNeededCollaboratorsList(Project project) {
        // list of collaborators synchronized with roles needed list by
        // index and size: so that
        // Role(1) -> null
        // Role(2) --> Collaborator(withRole(2))
        // for case where rolesNeeded are: [Role(1), Role(2)]
        // and project.collaborators are: [Collaborator(withRole(2))]
        // This list in opposite to project.collaborators will look like
        // [null, Collaborator(withRole(2))]
        List<Collaborator> projectCollaboratorsWithNullsForUnAssigned =
                new ArrayList<>();

        // cycle through roles needed
        for (Role roleNeeded : project.getRolesNeeded()) {
            boolean collaboratorIsAssigned = false;

            // cycle through projectCollaborators
            for (Collaborator projectCollaborator : project.getCollaborators()) {

                // if collaborator is assigned to this role: we check by
                // unique ids
                if (projectCollaborator.getRole().getId() ==
                        roleNeeded.getId()) {

                    // we assign collaborator
                    collaboratorIsAssigned = true;

                    // add this collaborated synchronized with role
                    projectCollaboratorsWithNullsForUnAssigned
                            .add(projectCollaborator);

                    // break the cycle
                    break;
                }
            }

            // if after cycling through all collaborators we found that
            // no collaborator was assigned for this roles, we add
            // null at this index in our new array
            if (!collaboratorIsAssigned) {
                projectCollaboratorsWithNullsForUnAssigned.add(null);
            }
        }
        return projectCollaboratorsWithNullsForUnAssigned;
    }

    // project detail page
    @RequestMapping("/projects/{projectId}/details")
    public String projectDetails(
            @PathVariable int projectId,
            Model model
        ) {
        // find project by id
        Project project = projectService.findById(projectId);

        // if not found show error page
        if (project == null) {
            throw new NotFoundException("Project not found");
        }

        // add project to the model
        model.addAttribute("project", project);

        if (project.getCollaborators().size() > 0) {
            // here is tricky part, we add nulls for unassigned collaborators,
            // to print unassigned collaborators in thymeleaf. Here is an
            // explaination try: If I put in thymeleaf list with 3 Roles, and 1
            // collaborator, it is hard to print it easily, because thymeleaf is
            // limited in capabilities. However if I put 3 Roles and 3
            // Collaborators list, where missing collaborators will be nulls,
            // then printing will be done easily by thymeleaf,
            // because we iterate both arrays
            // synchronously and can easily check which collaborator is
            // unassigned
            // So here is method generating such list (see above)
            // is added this synchronized list to model
            model.addAttribute("projectCollaboratorsWithNullsForUnAssigned",
                    generateSynchronizedWithRolesNeededCollaboratorsList(
                            project)
            );
        }
        return "project/project-details";
    }

    // edit collaborators page
    @RequestMapping("/projects/{projectId}/collaborators")
    public String editProjectCollaborators(
            @PathVariable int projectId,
            Model model,
            RedirectAttributes redirectAttributes) {
        // find project by id with role collaborators initialization
        // now I'm not sure, whether this is important. But I trieed my best
        // here we initialize collaborators for only project roles, so that
        // when we use in template role.collaborators, we don't initialize
        // whole database of roles. Now I don't know how to check how better
        // this approach is: it seems logic, But i don't know how to test
        // this
        Project project =
                projectService.findByIdWithRoleCollaboratorsInitialization(
                        projectId
                );
        // if not found throw error
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        // if there are no roles in project we redirect back to edit
        // project page to add some
        if (project.getRolesNeeded().size() == 0) {
            // set according flash attrbitute
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                    "No roles are picked for this project, please pick some",
                    FlashMessage.Status.SUCCESS
            ));
            return "redirect:/projects/" + projectId + "/edit";
        }
        // here we synchronize collaborators with roles needed, so that user
        // can see assigned collaborators for his roles.
        List<Collaborator> collaboratorsSynchronized =
                generateSynchronizedWithRolesNeededCollaboratorsList(project);
        project.setCollaborators(collaboratorsSynchronized);
        // add project to model
        model.addAttribute("project", project);
        return "project/project-collaborators";
    }

    // save collaborators to project POST method
    @RequestMapping(value = "/projects/save-collaborators",
        method = RequestMethod.POST
    )
    public String saveColaboratorsForProject(
            @Valid Project projectOnlyWithCollaboratorsAndId,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // first we obtain collaborators needed array from project object
        // filled in form. This collaborators list will have null values for
        // everything except ids. We then using this ids find collaborators
        // from collaborator service, and pass them to project
        List<Collaborator> collaborators =
                projectOnlyWithCollaboratorsAndId
                .getCollaborators()
                .stream()
                .filter(collaborator -> collaborator.getId() != 0)
                .collect(Collectors.toList());

        // so for now here cannot be validation error, because projects
        // can exist with unassigned collaborators, and id, name, description
        // fields are simply hidden. So no check for errors, for now

        // here we get the actual project from database, that is
        // needed because rolesNeeded are not saved|cannot be easily pushed
        // with some hidden input, so we take rolesNeeded from actual project
        // of db, and set these roles to project to be updated
        Project actualProjectToBeFilledWithCollaborators =
                projectService.findById(
                projectOnlyWithCollaboratorsAndId.getId()
        );

        // we set collaborators of project from thymeleaf with selected
        // collaborators. Here is magic: somehow knowing only ids of these
        // collaborators is enough to be updated in database. Also if all
        // user picks all collaborators unassigned, then null collaborators
        // array is updated just fine in database as well. Truly magic, although
        // the only thing to prove that is testing: that is a thing to do later
        actualProjectToBeFilledWithCollaborators
                .setCollaborators(collaborators);

        // UPDATE project to database: dateCreated is not changed
        projectService.update(actualProjectToBeFilledWithCollaborators);

        // add flash of successful update on top of the redirected page
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborators were successfully added/changed !",
                FlashMessage.Status.SUCCESS
        ));

       // redirect back to detail project page
       return "redirect:/projects/"
               + actualProjectToBeFilledWithCollaborators.getId() +
               "/details";
    }
    // delete project request
    @RequestMapping(value = "/projects/{projectId}/delete")
    public String deleteProject(
            @PathVariable int projectId,
            RedirectAttributes redirectAttributes
    ) {
        // find project by id
        Project project = projectService.findById(projectId);

        // if project is not found throw not found error page
        if (project == null) {
            throw new NotFoundException("Project not found");
        }

        // delete Project from database
        projectService.delete(project);

        // set flash message on top in redirected page
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Project '" + project.getName() +
                        "' was successfully deleted!",
                FlashMessage.Status.SUCCESS
        ));
        // redirect back to home page
        return "redirect:/";
    }
    // If anywhere NotFoundException is thrown we return error page,
    // i set custom status here, because for some reason otherwise
    // status is 200 :(
    @ExceptionHandler(NotFoundException.class)
    public String projectNotFound(Model model) {
        model.addAttribute("custom_status", 404);
        return "error";
    }
}
