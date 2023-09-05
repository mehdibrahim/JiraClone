package com.proxym.jira.jira.controller;




import io.swagger.v3.oas.annotations.media.Content;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.proxym.jira.jira.entity.IStatus;
import com.proxym.jira.jira.entity.Project;
import com.proxym.jira.jira.repository.IssueStatusRepository;
import com.proxym.jira.jira.repository.ProjectRepository;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/project/{projectId}/issuestatus")
public class IStatusController {


    @Autowired
    IssueStatusRepository statusRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Operation(summary = "Get List Of Status",description = "Get List Of Status",tags ="Issue Status")
    @ApiResponse( responseCode = "200",description="List Of Status",content = {@Content(mediaType ="application/json")})
    @GetMapping
    Set<IStatus> getStatus(@PathVariable Long projectId) {
        Project project =projectRepository.findById(projectId).get();
        return project.getEnrolledStatus() ;
    }
    @Operation(summary = " New Status",description = " New Status",tags ="Issue Status")
    @ApiResponse( responseCode = "200",description=" Status Added",content = {@Content(mediaType ="application/json")})
    @PostMapping("/{projectId}/issuestatus/add")
    String createIssueStatus(@PathVariable Long projectId,@RequestBody IStatus status) {
        
        statusRepository.save(status);
        
        Project project=projectRepository.findById(projectId).get();
        project.addNewStatus(status);
        projectRepository.save(project);
        return " Status Created ";
    }




    @Operation(summary = "Delete Status",description = "Delete Status",tags ="Issue Status")
    @ApiResponse( responseCode = "200",description="Issue Deleted Successfully",content = {@Content(mediaType ="application/json")})
    @DeleteMapping
    public String deleteStatusFromProject( @PathVariable Long projectId,@RequestParam("id") Long id){

        boolean test=false;
        Project project =projectRepository.findById(projectId).get();
        projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+projectId));
        statusRepository.findById(id).orElseThrow(()->new ResourceAccessException("Status with ID: "+id+"Not Exist"));
        for (IStatus status :project.getEnrolledStatus()){
            if (status.getId()==id)  
                test=true;

        }
        if (test==false) throw new ResourceAccessException("issue status not exist in this project");
        statusRepository.deleteFromEnrolledStatus(projectId);
        statusRepository.deleteStatusById(id);
        return "status sucessfully deleted";
    }
    
}
