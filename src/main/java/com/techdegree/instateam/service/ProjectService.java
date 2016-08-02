package com.techdegree.instateam.service;

import com.techdegree.instateam.model.Project;

public interface ProjectService extends GenericService<Project> {
    Project findByIdWithRoleCollaboratorsInitialization(int projectId);
}
