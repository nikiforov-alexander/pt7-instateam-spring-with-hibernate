package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.exception.NotFoundException;
import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.CollaboratorService;
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
        // add collaborators to view
        model.addAttribute("collaborators", collaborators);
        // Make project with all collaborators, is needed to update many
        // collaborators at once
        if (!model.containsAttribute("projectWithAllCollaborators")) {
            Project projectWithAllCollaborators = new Project();
            // fill project with existing collaborators from database
            projectWithAllCollaborators.setCollaborators(collaborators);
            // add to view
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
    public String addNewCollaborator(
            @Valid Collaborator collaborator,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // we check for role.id == 0 because, we leave the option where
        // user didn't select any role selected, see template file
        // it can be quite strange, but I thought Ok for now
        if (result.hasErrors() || collaborator.getRole().getId() == 0) {
            // this way we remember user's wrong input, leaving him to it
            redirectAttributes.addFlashAttribute("newCollaborator",
                    collaborator);
            // add error flash attribute for invalid role
            if ( collaborator.getRole().getId() == 0) {
                redirectAttributes.addFlashAttribute(
                        "invalidRoleMessage",
                        "Please select a Role");
            }
            // add error flash attribute for invalid name
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.newCollaborator",
                    result);
            // add error flash message because this page is going to be really
            // big and it is hard to see error
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                    "Oops! Something went wrong, please see below",
                    FlashMessage.Status.FAILURE
            ));
            // return back to collaborators page
            return "redirect:/collaborators";
        }
        // save collaborator in database
        collaboratorService.save(collaborator);
        Role selectedRole =
                roleService.findById(collaborator.getRole().getId());
        // set successful flash message
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborator '" + collaborator.getName() +
                 "' with Role: '" + selectedRole.getName() +
                 "' is succesfully added!", FlashMessage.Status.SUCCESS
        ));
        // redirect back to collaborators page
        return "redirect:/collaborators";
    }

    // Form for saving all collaborators' roles
    @RequestMapping(value = "/collaborators/save-roles",
            method = RequestMethod.POST)
    public String saveCollaboratorsRoles(
            Project project,
            RedirectAttributes redirectAttributes) {
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
                // show flash message with success
                redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                        "Collaborators were successfully updated!",
                        FlashMessage.Status.SUCCESS
                ));
            }
        }
        return "redirect:/collaborators";
    }

    // Detail collaborator page
    @RequestMapping(value = "/collaborators/{collaboratorId}")
    public String collaboratorDetails(
            @PathVariable int collaboratorId,
            Model model) {
        // if there was wrong input it should be saved
        // if not we fill collaborator
        if (!model.containsAttribute("collaborator")) {
            // find collaborator in database
            Collaborator collaborator =
                    collaboratorService.findById(collaboratorId);
            // if not found we return error page with 404
            if (collaborator == null) {
                throw new NotFoundException("Collaborator is not found");
            }
            // add found collaborator to model
            model.addAttribute("collaborator", collaborator);
        }
        // find all roles in database
        List<Role> roles = roleService.findAll();
        // add them to model
        model.addAttribute("roles", roles);
        return "collaborator/collaborator-details";
    }
    // If anywhere NotFoundException is thrown we return error page,
    // i set custom status here, because for some reason otherwise
    // status is 200 :(
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
            BindingResult result,
            RedirectAttributes redirectAttributes) {
       // if user input is not correct or role is not selected
       if (result.hasErrors() || collaborator.getRole().getId() == 0) {
           // this way we remember user's wrong input, leaving him to it
           redirectAttributes.addFlashAttribute("collaborator",
                   collaborator);
           // add error flash attribute for invalid role
           if (collaborator.getRole().getId() == 0) {
               redirectAttributes.addFlashAttribute(
                       "invalidRoleMessage",
                       "Please select a Role");
           }
           // add error flash attribute for invalid name
           redirectAttributes.addFlashAttribute(
                   "org.springframework.validation.BindingResult.collaborator",
                   result);
           // return back to edit page
           return "redirect:/collaborators/" + collaboratorId;
       }
       // save collaborator to database
       collaboratorService.save(collaborator);
       // set success flash message
       redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborator '" + collaborator.getName() +
                        "' is saved!", FlashMessage.Status.SUCCESS
       ));
       return "redirect:/collaborators";
    }

    // delete collaborator
    @RequestMapping(value = "/collaborators/{collaboratorId}/delete")
    public String deleteCollaborator(
            @PathVariable int collaboratorId,
            RedirectAttributes redirectAttributes) {
        // find collaborator by id
        Collaborator collaborator =
                collaboratorService.findById(collaboratorId);
        // if not found error page is generated with 404
        if (collaborator == null) {
            throw new NotFoundException("Collaborator not found");
        }
        // delete collaborator from database
        collaboratorService.delete(collaborator);
        // set success flash message
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborator '" + collaborator.getName() +
                "' is successfully deleted!", FlashMessage.Status.SUCCESS
        ));
        // redirect back to collaborators page
        return "redirect:/collaborators";
    }
}
