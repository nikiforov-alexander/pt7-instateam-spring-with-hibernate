package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    private ProjectService mProjectService;

    // index of all categories
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listProjects(Model model) {
        // Get all projects
        List<Project> projects = mProjectService.findAll();
        model.addAttribute("projects", projects);
        return "index";
    }
}
