package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.exception.NotFoundException;
import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.ProjectStatus;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.ProjectService;
import com.techdegree.instateam.service.RoleService;
import com.techdegree.instateam.web.FlashMessage;
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
            Model model,
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
        // save project to database
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
        // if model contains project, e.g. when we
        // user made a mistake, model will be filled with
        // with previously entered data
        if (!model.containsAttribute("project")) {
            // if not we add edited Project
            model.addAttribute("project", project);
        }
        return "/project/project-edit";
    }
    
    // save existing project POST request
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
        // save project to database
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

    // project detail page
    @RequestMapping("/projects/{projectId}/details")
    public String projectDetails(
            @PathVariable int projectId,
            Model model,
            RedirectAttributes redirectAttrbutes
    ) {
        // find project by id
        Project project = projectService.findById(projectId);
        // if not found show error page
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        // add project to the model
        model.addAttribute("project", project);
        return "project/project-details";
    }

//     edit collaborators page
    @RequestMapping("/projects/{projectId}/collaborators")
    public String editProjectCollaborators(
            @PathVariable int projectId,
            Model model,
            RedirectAttributes redirectAttributes) {
        // find project by id
        Project project = projectService.findById(projectId);
        // if not found throw error
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
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
        List<Collaborator> collaborators = projectOnlyWithCollaboratorsAndId
                .getCollaborators()
                .stream()
                .filter(collaborator -> collaborator.getId() != 0)
                .collect(Collectors.toList());

        // so for now here cannot be validation error, because projects
        // can exist with unassigned collaborators, and id, name, description
        // fields are simply hidden. So no check for errors

        // here we get the actual project from database, that is
        // needed because rolesNeeded are not saved|cannot be easily pushed
        // with some hidden input, so we take rolesNeeded from actual project
        // of db, and set these roles to project to be updated
        Project actualProjectToBeFilledWithCollaborators =
                projectService.findById(
                projectOnlyWithCollaboratorsAndId.getId()
        );

        // we set collaborators of project from thymeleaf with selected
        // collaborators
        actualProjectToBeFilledWithCollaborators
                .setCollaborators(collaborators);

        // update project to database
        projectService.save(actualProjectToBeFilledWithCollaborators);

        // add flash of successful save on top of the redirected page
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborators '" + actualProjectToBeFilledWithCollaborators.getName() +
                        "' were successfully added!",
                FlashMessage.Status.SUCCESS
        ));

       // redirect back to detail project page
       return "redirect:/projects/"
               + actualProjectToBeFilledWithCollaborators.getId() +
               "/details";
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
