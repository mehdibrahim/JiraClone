package com.proxym.jira.service;

import java.util.List;

import com.proxym.jira.jira.entity.Project;

public interface ProjectService {
    List<Project> searchProjects(String query);
}
