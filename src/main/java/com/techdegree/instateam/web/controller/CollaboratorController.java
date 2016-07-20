package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.CollaboratorService;
import com.techdegree.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
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
        // Get all roles
        List<Role> roles = roleService.findAll();
        model.addAttribute("collaborators", collaborators);
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
}
