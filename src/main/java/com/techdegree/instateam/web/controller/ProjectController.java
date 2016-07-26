package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.ProjectStatus;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.ProjectService;
import com.techdegree.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

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
        model.addAttribute("roles", roles);
        // if model contains project, e.g. when we
        // user made a mistake, model will be filled with
        // with previously entered data
        if (!model.containsAttribute("project")) {
            // if not we add fresh new Project
            Project project = new Project();
            project.setRolesNeeded(roles);
            model.addAttribute("project", project);
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
        // if user's input is not valid
        System.out.println(project);
        if (result.hasErrors()) {
            // redirect back to form
            return "redirect:/projects/add-new";
        }
        // redirect back to main page
        return "redirect:/";
    }
}
