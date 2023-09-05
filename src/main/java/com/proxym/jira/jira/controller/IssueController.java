package com.proxym.jira.jira.controller;
import java.util.List;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;
import com.proxym.jira.jira.entity.IStatus;
import com.proxym.jira.jira.entity.Issue;
import com.proxym.jira.jira.entity.Project;
import com.proxym.jira.jira.entity.User;
import com.proxym.jira.jira.repository.UserRepository;
import com.proxym.jira.service.IssueService;
import com.proxym.jira.jira.repository.IssueRepository;
import com.proxym.jira.jira.repository.IssueStatusRepository;
import com.proxym.jira.jira.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/{projectId}/issue")
public class IssueController {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    IssueStatusRepository statusRepository;

    @Autowired
   private IssueRepository issueRepository;
    @Autowired
    private IssueService issueService;
    @Autowired
    UserRepository userRepository;
    @Operation(summary = "Get List Of Issue",description = "Get List Of Issue",tags ="Issue")
    @ApiResponse( responseCode = "200",description="List Of Issue",content = {@Content(mediaType ="application/json")})
    @GetMapping
    List<Issue> getIssues( @PathVariable Long projectId) {
        return issueRepository.findAllByProjectId(projectId);
    }
    
    
    @Operation(summary = "Add new Members",description = "Add New Members",tags ="Issue")
    @ApiResponse( responseCode = "200",description="Member Added sucessfully",content = {@Content(mediaType ="application/json")})
    @PostMapping("/{issueId}/member")
    public String addmemberToIssue  (
            @PathVariable Long issueId,
            @RequestParam("userId") Long userId
    ) {
        
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new ResourceAccessException("You can't Add members for an invalid issue"));
        
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceAccessException("You can't Add members with an invalid Id: "+userId));
        issue.issueEnrolledUsers(user);
        issueRepository.save(issue);
        return "User with name : "+userId+"successfully added";
    }
    @Operation(summary = "Add New Issue",description = "Add New Issue",tags ="Issue")
    @ApiResponse( responseCode = "200",description="New Issue Added",content = {@Content(mediaType ="application/json")})
    @PostMapping
    String createIssue(@RequestBody Issue issue, @PathVariable Long projectId) {

        Project project= projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+projectId));
      //statusRepository.findByStatus(issue.getStatus().getStatus()).orElseThrow(()->new ResourceAccessException("uncorrect status")); exception problem
        project.enrolledIssues(issue);
        issue.setProject(project);
        issueRepository.save(issue);
        return "Issue Created" ;
    }
   
    @Operation(summary = "Delete Member",description = "Delete Member",tags ="Issue")
    @ApiResponse( responseCode = "200",description="Member Deleted Successfully",content = {@Content(mediaType ="application/json")})
    @DeleteMapping("/{issueId}/member")
    public String deletememberFromIssue( @PathVariable Long issueId,@RequestParam("id") Long id){

        boolean test=false;
        
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new ResourceAccessException("invalid issue ID: "+issueId));
       
        userRepository.findById(id).orElseThrow(()->new ResourceAccessException("User with ID: "+id+"Not Exist"));
        for (User user :issue.getIssueEnrolledUsers()){
            if (user.getId()==id)  
                test=true;

        }
        if (test==false) throw new ResourceAccessException("user not exist in this issue");
        issueRepository.deleteFromEnrolledTheMember(id);
        return "Member sucessfully deleted";}
        @Operation(summary = "Search For Issues",description = "Search For Issues",tags = "Issue")
        @ApiResponse( responseCode = "200",description="Issues",content = {@Content(mediaType ="application/json")})
        @GetMapping("/search")
        public ResponseEntity<List<Issue>> searchIssues(@PathVariable Long projectId,@RequestParam("query") String query){
            return ResponseEntity.ok(issueService.searchIssues(projectId,query));
        }
        
        @Operation(summary = "Get Issue Members",description = "Get a List of Issue Members",tags = "Issue")
        @ApiResponse( responseCode = "200",description="Issue Members",content = {@Content(mediaType ="application/json")})
        @GetMapping("/{issueId}/member")
        Set<User> issueMembers(@PathVariable Long projectId,@PathVariable Long issueId) {
            issueRepository.findById(issueId).orElseThrow(()->new ResourceAccessException("You can't get members for an invalid issue ID: "+issueId));
            return issueRepository.findById(issueId).get().getIssueEnrolledUsers();
        }
    @Operation(summary = "Issue Info",description = "Get Issue Info",tags = "Issue")
    @ApiResponse( responseCode = "200",description="Issue Informations",content = {@Content(mediaType ="application/json")})
    @GetMapping("/{issueId}")
    public Issue issueInfo(@PathVariable Long projectId,@PathVariable Long issueId) {
        
        return issueRepository.findById(issueId).orElseThrow(()->new ResourceAccessException("Issue Not Found With ID:"+issueId));
    }
    @Operation(summary = "Delete Project",description = "Delete Project",tags ="Issue")
    @ApiResponse( responseCode = "200",description="Project Deleted",content = {@Content(mediaType ="application/json")})
    @DeleteMapping("/{issueId}")
    public String deleteIssue(@PathVariable Long projectId,@PathVariable Long issueId) {
       
        issueRepository.findById(issueId).orElseThrow(()->new ResourceAccessException("Issue Not Found With ID:"+issueId));
        issueRepository.deleteFromEnrolledUser(issueId);
        issueRepository.deleteIssueById(issueId);
        return "Issue with ID: "+issueId+" deleted ";
    }
    @Operation(summary = "Assign Status",description = "Assign Status",tags ="Issue")
    @ApiResponse( responseCode = "200",description="Status Assigned",content = {@Content(mediaType ="application/json")})
    @PutMapping("/{issueId}/status")
     public String assignStatusToIssue  (
            @PathVariable Long projectId,
            @PathVariable Long issueId,
            @RequestParam("statusId") Long statusId
    ) {
         statusRepository.findById(statusId).orElseThrow(()->new ResourceAccessException("invalid status ID: "+statusId));
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+issueId));
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ResourceAccessException("invalid project ID: "+projectId));
        for(IStatus status: project.enrolledStatus){

            if(status.getId()==statusId){
                issue.setStatus(status);
                issueRepository.save(issue);
                return "Status sucessfully changed";

            }

        }

        return "le status n existe pas dans ce project";
        
    }

    
    
  
}
