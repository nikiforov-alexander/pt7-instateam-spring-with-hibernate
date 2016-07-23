package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.exception.NotFoundException;
import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.CollaboratorService;
import com.techdegree.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if (result.hasErrors() || collaborator.getRole().getId() == 0) {
            return "redirect:/collaborators";
        }
        collaboratorService.save(collaborator);
        return "redirect:/collaborators";
    }

    // Form for saving all collaborators' roles
    @RequestMapping(value = "/collaborators/save-roles",
            method = RequestMethod.POST)
    public String saveCollaboratorsRoles(Project project) {
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
                // set new id for old collaborator's role
                oldRole.setId(newRoleId);
                // update database
                collaboratorService.save(newCollaborator);
            }
        }
        return "redirect:/collaborators";
    }

    // Detail collaborator page
    @RequestMapping(value = "/collaborators/{collaboratorId}")
    public String collaboratorDetails(
            @PathVariable int collaboratorId,
            Model model) {
        if (!model.containsAttribute("collaborator")) {
            Collaborator collaborator =
                    collaboratorService.findById(collaboratorId);
            if (collaborator == null) {
                throw new NotFoundException("Collaborator is not found");
            }
            model.addAttribute("collaborator", collaborator);
        }
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "collaborator/collaborator-details";
    }
    @ExceptionHandler(NotFoundException.class)
    public String collaboratorNotFound(Model model) {
        model.addAttribute("custom_status", 404);
        return "error";
    }

    // save or update collaborator on detail page
    @RequestMapping(value = "/collaborators/{collaboratorId}/edit",
            method = RequestMethod.POST)
    public String saveOrUpdateCollaborator(
            @PathVariable int collaboratorId,
            @Valid Collaborator collaborator,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
       // if user input is not correct or role is not selected
       if (bindingResult.hasErrors() || collaborator.getRole().getId() == 0) {
           System.out.println(collaborator);
           redirectAttributes.addFlashAttribute("collaborator", collaborator);
           return "redirect:/collaborators/" + collaboratorId;
       }
       collaboratorService.save(collaborator);
       return "redirect:/collaborators";
    }
}
