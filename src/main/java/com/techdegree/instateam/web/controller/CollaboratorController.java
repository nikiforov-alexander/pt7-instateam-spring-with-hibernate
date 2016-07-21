package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.CollaboratorService;
import com.techdegree.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CollaboratorController {
    @Autowired
    private CollaboratorService collaboratorService;
    @Autowired
    private RoleService roleService;

    // index of all categories
    @SuppressWarnings("unchecked")
    @RequestMapping("/collaborators")
    public String listCollaborators(Model model) {
        // Get all collaborators
        List<Collaborator> collaborators = collaboratorService.findAll();
        model.addAttribute("collaborators", collaborators);
        // Make project with all collaborators
        if (!model.containsAttribute("projectWithAllCollaborators")) {
            Project projectWithAllCollaborators = new Project();
            projectWithAllCollaborators.setCollaborators(collaborators);
            model.addAttribute("projectWithAllCollaborators",
                    projectWithAllCollaborators);
        }
        // Get all roles
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        // add empty newCollaborator, so that we can fill it with
        // data when making POST request
        if (!model.containsAttribute("newCollaborator")) {
            model.addAttribute("newCollaborator", new Collaborator());
        }
        return "collaborator/collaborators";
    }

    // Form for adding new collaborator
    @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
    public String addNewCollaborator(@Valid Collaborator collaborator,
                                     BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.hasErrors());
            return "redirect:/collaborators";
        }
        collaboratorService.save(collaborator);
        return "redirect:/collaborators";
    }

    // Form for saving all collaborators' roles
    @RequestMapping(value = "/collaborators/save-roles", method = RequestMethod.POST)
    public String saveCollaboratorsRoles(Project project) {
        System.out.println(project);
        List<Collaborator> collaboratorsInDatabase =
                collaboratorService.findAll();

        for (int i = 0; i < collaboratorsInDatabase.size(); i++) {
            // get old role from database
            Role oldRole = collaboratorsInDatabase.get(i).getRole();
            // get id from old role
            int oldRoleId = oldRole.getId();
            // get new role id
            int newRoleId = project.getCollaborators().get(i).getRole().getId();
            // if id is changed, we proceed
            if (oldRoleId != newRoleId) {
                // get collaborator from database
                Collaborator newCollaborator = collaboratorsInDatabase.get(i);
                // get new Role from old one
                Role newRole = oldRole;
                // set new id for this role
                newRole.setId(newRoleId);
                // set this role to collaborator
                newCollaborator.setRole(newRole);
                // update database
                collaboratorService.save(newCollaborator);
            } else {
                System.out.println("Nothing is changed");
            }
        }
        return "redirect:/collaborators";
    }
//    @RequestMapping(value = "/collaborators/save-roles", method = RequestMethod.POST)
//    public String saveCollaboratorsRoles(@Valid Project project,
//                                         BindingResult result) {
//        System.out.println(project);
//        if (result.hasErrors()) {
//            System.out.println("errors, redirecting");
//            return "redirect:/collaborators";
//        }
//        return "redirect:/collaborators";
//    }
}
