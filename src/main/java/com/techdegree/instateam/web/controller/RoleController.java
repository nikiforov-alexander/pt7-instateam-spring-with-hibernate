package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    // index of all categories
    @SuppressWarnings("unchecked")
    @RequestMapping("/roles")
    public String listRoles(Model model) {
        // Get all roles
        List<Role> roles = roleService.findAll();
        // add roles to model
        model.addAttribute("roles", roles);
        // if user typed wrong data it will be shown
        if (!model.containsAttribute("newRole")) {
            // add empty newRole so that we can fill it with
            // data, when making POST request on this page
            model.addAttribute("newRole", new Role());
        }
        return "role/roles";
    }

    // Form for adding new role
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String addNewRole(@Valid Role role,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("newRole", role);
            return "redirect:/roles";
        }
        roleService.save(role);
        return "redirect:/roles";
    }

    // Detail role page
    @RequestMapping(value = "/roles/{roleId}")
    public String roleDetails(@PathVariable int roleId,
                              Model model) {
        // here we check if user made wrong input, and show his wrong data
        if (!model.containsAttribute("role")) {
            Role role = roleService.findById(roleId);
            model.addAttribute("role", role);
        }
        return "role/role-details";
    }

    // Edit role
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.POST)
    public String editRole(@PathVariable int roleId,
                           @Valid Role role,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // we add flash to remember user's input
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/roles/" + roleId;
        }
        roleService.save(role);
        return "redirect:/roles";
    }

    //Delete role
    @RequestMapping(value = "/roles/{roleId}/delete")
    public String deleteRole(@PathVariable int roleId) {
        Role role = roleService.findById(roleId);
        roleService.delete(role);
        return "redirect:/roles";
    }
}
