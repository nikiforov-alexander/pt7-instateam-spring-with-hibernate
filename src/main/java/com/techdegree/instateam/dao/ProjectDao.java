package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Project;

public interface ProjectDao extends GenericDao<Project> {
    Project findByIdWithRoleCollaboratorsInitialization(int projectId);
}
