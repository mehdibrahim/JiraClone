package com.proxym.jira.jira.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "issues")
@EntityListeners(AuditingEntityListener.class)
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    @CreatedDate
	@Column(name = "created_date")
    @JsonIgnore
	private Date createdTime;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private IStatus status;

   

    public IStatus getStatus() {
        return status;
    }

    public void setStatus(IStatus status) {
        this.status = status;
    }


   
	

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	

    public Long getId() {
        return id;
    }

    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

 

    public Project getProject() {
        return project;
    }




    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToMany
    @JoinTable(
            name = "issue_enrolled",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<User> issueEnrolledUsers = new HashSet<>();

    
    
   

   
   
    
   

   

    public void setIssueEnrolledUsers(Set<User> enrolledUsers) {
        this.issueEnrolledUsers = enrolledUsers;
    }

    public Set<User> getIssueEnrolledUsers() {
        return issueEnrolledUsers;
    }

    

    public void issueEnrolledUsers(User user) {
       this.issueEnrolledUsers.add(user);
    }}

