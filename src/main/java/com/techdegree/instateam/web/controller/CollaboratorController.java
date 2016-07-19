package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CollaboratorController {
    @Autowired
    private CollaboratorService mCollaboratorService;

    // index of all categories
    @SuppressWarnings("unchecked")
    @RequestMapping("/collaborators")
    public String listCollaborators(Model model) {
        // Get all collaborators
        List<Collaborator> collaborators = mCollaboratorService.findAll();
        model.addAttribute("collaborators", collaborators);
        return "collaborator/collaborators";
    }
}
