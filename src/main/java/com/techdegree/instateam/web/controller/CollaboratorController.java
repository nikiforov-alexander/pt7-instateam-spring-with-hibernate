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
    // suppress warnings because of findAll()
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
        // add roles to view
        model.addAttribute("roles", roles);
        // add empty newCollaborator, so that we can fill it with
        // data when making POST request. If user howoever already typed
        // wrong name his input is saved in input field
        if (!model.containsAttribute("newCollaborator")) {
            model.addAttribute("newCollaborator", new Collaborator());
        }
        return "collaborator/collaborators";
    }

    // Form for adding new collaborator
    @RequestMapping(value = "/collaborators/add-new",
            method = RequestMethod.POST)
    public String addNewCollaborator(
            @Valid Collaborator collaborator,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // we check for role.id == 0 because, we leave the option where
        // user didn't select any role , see template file
        // it can be quite strange, but I thought Ok for now
        if (result.hasErrors() || collaborator.getRole().getId() == 0) {
            // this way we remember user's wrong input, leaving him to it
            redirectAttributes.addFlashAttribute("newCollaborator",
                    collaborator);
            // add error flash attribute if user didn't select a role
            if (collaborator.getRole().getId() == 0) {
                redirectAttributes.addFlashAttribute(
                        "invalidRoleMessage",
                        "Please select a Role");
            }
            // add error flash attribute for invalid name, to remember user's
            // input
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
        // saveOrUpdate collaborator in database
        collaboratorService.saveOrUpdate(collaborator);
        // find selected Role to print in flash message
        Role selectedRole =
                roleService.findById(collaborator.getRole().getId());
        // set successful flash message on top
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
        // find all collaborators in database
        List<Collaborator> collaboratorsInDatabase =
                collaboratorService.findAll();

        // number of collaborators updated: used in flash later on
        int numberOfCollaboratorsUpdated = 0;
        // cycle through list of collaborators to check which roles were
        // changed and update them
        for (int i = 0; i < collaboratorsInDatabase.size(); i++) {

            // get old role from database
            Role oldRole = collaboratorsInDatabase.get(i).getRole();

            // id from old role.
            int oldRoleId;

            // if old Role is not null we safely get id
            if (oldRole != null) {
                oldRoleId = oldRole.getId();
            } else {
                // If old role is null, we set its id to 0
                oldRoleId = 0;
                // and create new Role()
                oldRole = new Role();
            }

            // get new role from user's input
            Role newRole = project.getCollaborators().get(i).getRole();

            // here we know that id will be set: to 0 if unassigned,
            // to actual new role id, when new role is picked
            int newRoleId = newRole.getId();

            // if id is changed, we proceed
            if (oldRoleId != newRoleId) {

                // get collaborator with changed role from database
                Collaborator newCollaborator = collaboratorsInDatabase.get(i);

                // if new role id is not equal to 0
                if (newRoleId != 0) {
                    // because new role exists, in this if we update id of
                    // the old role only.

                    // 1. set new id for collaborator's role
                    oldRole.setId(newRoleId);

                    // 2. here we set oldRole to include the case when
                    // collaborator had no role before. If old role was not
                    // null, this line is not needed
                    newCollaborator.setRole(oldRole);
                } else {
                    // in this else we set collaborator role to null, because
                    // user did not select any role
                    newCollaborator.setRole(null);
                }

                // update database
                collaboratorService.saveOrUpdate(newCollaborator);

                // set number of collaborators updated for successful flash
                numberOfCollaboratorsUpdated++;

            }
        }
        // show flash message with success on top if at least one collaborator
        // was updated
        if (numberOfCollaboratorsUpdated > 0) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                    numberOfCollaboratorsUpdated +
                            " Collaborator(-s) were successfully updated!",
                    FlashMessage.Status.SUCCESS
            ));
        }
        // redirect back to collaborators page
        return "redirect:/collaborators";
    }

    // Detail collaborator page
    @RequestMapping(value = "/collaborators/{collaboratorId}")
    public String collaboratorDetails(
            @PathVariable int collaboratorId,
            Model model) {
        // if there was wrong input from saveOrUpdateCollaborator method with
        // POST request, it should be saved
        // if not we fill collaborator from database
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

    // saveOrUpdate or update collaborator on detail page
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
       // saveOrUpdate collaborator to database
       collaboratorService.saveOrUpdate(collaborator);
       // set success flash message on top
       redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborator '" + collaborator.getName() +
                        "' is saved!", FlashMessage.Status.SUCCESS
       ));
       // redirect back to main collaborators page
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
        // set success flash message on top
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                "Collaborator '" + collaborator.getName() +
                "' is successfully deleted!", FlashMessage.Status.SUCCESS
        ));
        // redirect back to collaborators page
        return "redirect:/collaborators";
    }
}
