package com.proxym.jira.jira.controller;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;


import com.proxym.jira.jira.entity.PStatus;
import com.proxym.jira.jira.entity.Project;
import com.proxym.jira.jira.entity.User;
import com.proxym.jira.jira.repository.ProjectRepository;
import com.proxym.jira.jira.repository.UserRepository;
import com.proxym.jira.service.ProjectService;
import com.proxym.jira.jira.repository.PStatusRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PStatusRepository pStatusRepository;
    
    @GetMapping
    @Operation(summary = "Get Projects",description = "Get a List of Projects",tags = "Project")
    @ApiResponse( responseCode = "200",description="There is the list of all projects",content = {@Content(mediaType ="application/json")})
    List<Project> getProjects() {
        return projectRepository.findAll(); 
    }
    @Operation(summary = "Create new Project",description = "Create new Project",tags = "Project")
    @ApiResponse( responseCode = "200",description="Project Created",content = {@Content(mediaType ="application/json")})
    @PostMapping
    Project createProject(@RequestBody Project project) {
        //erreur quand les utlisatuer sont correct
        //if (project.getStatus().getStatus().equals(pStatusRepository.findByStatus(project.getStatus().getStatus()))==false) throw new ResourceAccessException("Status Not Found ");
        //erreur quand les utlisatuer sont correct
        for (User user :project.getEnrolledUsers()){
            userRepository.findByUsername(user.getUsername()).orElseThrow(()->new ResourceAccessException("User Not Found With ID:"+user.getUsername()));
        }
        return projectRepository.save(project);
    }

    @Operation(summary = "Project Info",description = "Get Project Info",tags = "Project")
    @ApiResponse( responseCode = "200",description="Project Informations",content = {@Content(mediaType ="application/json")})
    @GetMapping("/{projectId}")
    public Project projectInfo(@PathVariable Long projectId) {
        
        return projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("Project Not Found With ID:"+projectId));
    }

    @Operation(summary = "Edit Project",description = "Edit Project",tags = "Project")
    @ApiResponse( responseCode = "200",description="Project Edited",content = {@Content(mediaType ="application/json")})   
    @PutMapping("/{projectId}")
       public Project editProject(@RequestBody Project project, @PathVariable Long projectId) {
        Project projectv = projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("Project Not Found With ID:"+projectId));
        projectv.setName(project.getName());
        
        projectv.setDescription(project.getDescription());
        projectv.setOwner(project.getOwner());
        projectv.setEnrolledUsers(project.getEnrolledUsers());
        return projectRepository.save(projectv);
    }

    @Operation(summary = "Delete Project",description = "Delete Project",tags ="Project")
    @ApiResponse( responseCode = "200",description="Project Deleted",content = {@Content(mediaType ="application/json")})
    @DeleteMapping("/{projectId}")
    public String deleteProject(@PathVariable Long projectId) {
       
        projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("Project Not Found With ID:"+projectId));
        projectRepository.deleteFromEnrolledUser(projectId);
        projectRepository.deleteProjectById(projectId);
        return "Project with ID: "+projectId+" deleted ";
    }

    @Operation(summary = "Search Projects",description = "Search for Projects",tags ="Project")
    @ApiResponse( responseCode = "200",description="There is The list of all projects You Want",content = {@Content(mediaType ="application/json")})
    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam("query") String query){
        return ResponseEntity.ok(projectService.searchProjects(query));
    }
    
    @Operation(summary = "Get Project Members",description = "Get a List of Project Members",tags = "Project")
    @ApiResponse( responseCode = "200",description="Project Members",content = {@Content(mediaType ="application/json")})
    @GetMapping("/{projectId}/member")
    Set<User> projectMembers(@PathVariable Long projectId) {
        projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("You can't get members for an invalid project ID: "+projectId));
        return projectRepository.findById(projectId).get().getEnrolledUsers();
    }





    
    @Operation(summary = "Add new Members",description = "Add New Members",tags ="Project")
    @ApiResponse( responseCode = "200",description="Member Added sucessfully",content = {@Content(mediaType ="application/json")})
    @PostMapping("/{projectId}/member")
    public String addmemberToProject  (
            @PathVariable Long projectId,
            @RequestParam("userId") Long userId
    ) {
        
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("You can't Add members for an invalid project ID: "+projectId));
        
        User  user=userRepository.findById(userId).orElseThrow(()->new ResourceAccessException("You can't Add members for an invalid User ID: "+projectId));
        project.enrolledUsers(user);
        projectRepository.save(project);
        return "User with Id "+userId+"successfully added";
    }
    @Operation(summary = "Delete Member",description = "Delete Member",tags ="Project")
    @ApiResponse( responseCode = "200",description="Member Deleted Successfully",content = {@Content(mediaType ="application/json")})
    @DeleteMapping("/{projectId}/member")
    public String deletememberFromProject( @PathVariable Long projectId,@RequestParam("id") Long id){

        boolean test=false;
        Project project =projectRepository.findById(projectId).get();
        projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+projectId));
        userRepository.findById(id).orElseThrow(()->new ResourceAccessException("User with ID: "+id+"Not Exist"));
        for (User user :project.getEnrolledUsers()){
            if (user.getId()==id)  
                test=true;

        }
        if (test==false) throw new ResourceAccessException("user not exist in this project");
        projectRepository.deleteFromEnrolledTheMember(id);
        return "Member sucessfully deleted";
    }
    
    
 
    @Operation(summary = "Assign Owner",description = "Assign Owner",tags ="Project")
    @ApiResponse( responseCode = "200",description="Owner Assigned",content = {@Content(mediaType ="application/json")})
    @PutMapping("/{projectId}/member")
     public String assignOwnerToProject  (
            @PathVariable Long projectId,
            @RequestParam("userId") Long userId
    ) {
        
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+projectId));
        
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceAccessException("User with ID: "+userId+"Not Exist"));
        project.assignOwner(user);
        project.enrolledUsers(user);
        projectRepository.save(project);
        
        return "Owner sucessfully changed";
    }

    @Operation(summary = "Assign Status",description = "Assign Status",tags ="Project")
    @ApiResponse( responseCode = "200",description="Status Assigned",content = {@Content(mediaType ="application/json")})
    @PutMapping("/{projectId}/status")
     public String assignStatusToProject  (
            @PathVariable Long projectId,
            @RequestParam("statusId") Long statusId
    ) {
        
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+projectId));
        
        PStatus status=pStatusRepository.findById(statusId).orElseThrow(()->new ResourceAccessException("Status with ID: "+statusId+"Not Exist"));
        project.setStatus(status);
        
        projectRepository.save(project);
        
        return "Status sucessfully changed";
    }

 }
