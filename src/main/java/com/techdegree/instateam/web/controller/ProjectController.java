package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    private ProjectService mProjectService;

    // main page with all projects
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listProjects(Model model) {
        // Get all projects
        List<Project> projects = mProjectService.findAll();
        // add projects to model
        model.addAttribute("projects", projects);
        return "index";
    }

    // add new project page
    @RequestMapping("/projects/add-new")
    public String addNewProject(Model model) {
        // add statuses values
        model.addAttribute("statuses", Project.Status.values());
        // we add action attribute because this template
        // will be re-used for both edit and add new project
        model.addAttribute("action", "add-new");
        // if model contains project, e.g. when we
        // user made a mistake, model will be filled with
        // with previously entered data
        if (!model.containsAttribute("project")) {
           // if not we add fresh new Project
           model.addAttribute("project", new Project());
        }
        return "/project/project-edit";
    }
}
