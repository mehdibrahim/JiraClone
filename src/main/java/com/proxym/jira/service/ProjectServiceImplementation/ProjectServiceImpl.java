package com.proxym.jira.service.ProjectServiceImplementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proxym.jira.jira.entity.Project;
import com.proxym.jira.jira.repository.ProjectRepository;
import com.proxym.jira.service.ProjectService;


@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    
    
    @Override
    public List<Project> searchProjects(String query) {
        List<Project> projects =projectRepository.searchProjects(query);
        return projects;
    }
    
}
