package com.techdegree.instateam.web.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
    public String addNewProject(Model model) {
        // add statuses values
        model.addAttribute("statuses", ProjectStatus.values());
        // we add action attribute because this template
        // will be re-used for both edit and add new project
        model.addAttribute("action", "add-new");
        // add roles available
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        // if model contains project, e.g. when we
        // user made a mistake, model will be filled with
        // with previously entered data
        if (!model.containsAttribute("project")) {
            // if not we add fresh new Project
            model.addAttribute("project", new Project());
        }
        return "/project/project-edit";
    }

    // add new project
    @RequestMapping(value = "/projects/add-new", method = RequestMethod.POST)
    public String saveProject(
            @Valid Project project,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        // create new array rolesNeeded
        List<Role> rolesNeeded = new ArrayList<>();
        // if user's input is not valid
        if (result.hasErrors()) {
            // here we take care of simple errors like description and
            // name field errors
            if(result.hasFieldErrors("name")
                    || result.hasFieldErrors("description")) {
                // add flash attribute of project, NOTE: checked roles
                // probably won't be saved. May be later
                redirectAttributes.addFlashAttribute("project", project);
                // add flash attribute with errors for name or description
                // fields
                redirectAttributes.addFlashAttribute(
                        "org.springframework.validation.BindingResult.project",
                        result);
                // redirect back to form
                return "redirect:/projects/add-new";
            }
            // here we take care of rolesNeeded errors
            // create array of ids of roles that were checked : that will be
            // the roles with id != 0. Don't know any other way. Tried lots of
            // stuff
            int[] arrayOfRolesIndicesThatWereChanged =
                    project.getRolesNeeded().stream()
                            .filter(role -> role.getId() != 0)
                            .mapToInt(Role::getId)
                            .toArray();
            if (arrayOfRolesIndicesThatWereChanged.length == 0) {
                // add other project fields typed by user
                redirectAttributes.addFlashAttribute("project", project);
                // add flash with error please select at least one role
                redirectAttributes.addFlashAttribute("rolesError",
                        "Please select at least one role");
                // redirect back to form with errors that some roles
                // have to be in project for now
                return "redirect:/projects/add-new";
            } else {
                // if length is not zero, we obtain valid rolesNeeded list
                for (int selectedRoleId : arrayOfRolesIndicesThatWereChanged) {
                    rolesNeeded.add(roleService.findById(selectedRoleId));
                }
            }
        }
        // add neededRoles to project
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
}
