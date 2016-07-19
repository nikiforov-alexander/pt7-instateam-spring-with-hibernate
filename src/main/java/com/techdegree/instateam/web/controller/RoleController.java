package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Role;
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
public class RoleController {
    @Autowired
    private RoleService roleService;

    // index of all categories
    @SuppressWarnings("unchecked")
    @RequestMapping("/roles")
    public String listRoles(Model model) {
        // Get all roles
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "role/roles";
    }

    // Form for adding new role
    @RequestMapping(value = "/roles",
        method = RequestMethod.POST)
    public String addNewRole(@Valid Role role,
                             BindingResult result,
                             Model model) {
        model.addAttribute("role", new Role());
        if (result.hasErrors()) {
            return "redirect:/roles";
        }
        roleService.save(role);
        return "redirect:/roles";
    }
}
