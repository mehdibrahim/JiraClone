package com.proxym.jira.jira.entity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    @CreatedDate
	@Column(name = "created_date")
    @JsonIgnore
	private Date createdTime;

	@LastModifiedDate
	@Column(name = "last_modified_date")
    @JsonIgnore
	private Date lastModifiedTime;
    public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

    public Long getId() {
        return id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="owner_id")
    
    private User owner;



    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }
   

    @ManyToMany
    
    @JoinTable(
            name = "user_enrolled",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<User> enrolledUsers = new HashSet<>();

    @ManyToMany
    
    @JoinTable(
            name = "istatus_enrolled",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "istatus_id")
    )
    public Set<IStatus> enrolledStatus = new HashSet<>();


    


    public Set<IStatus> getEnrolledStatus() {
        return enrolledStatus;
    }

    public void setEnrolledStatus(Set<IStatus> enrolledStatus) {
        this.enrolledStatus = enrolledStatus;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonIgnore // car il ya un problem
    private PStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<Issue> issues;
    public Set<Issue> getIssues() {
        return this.issues;
    }

    public PStatus getStatus() {
        return status;
    }

    public void setStatus(PStatus status) {
        this.status = status;
    }

    public void setEnrolledUsers(Set<User> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }

    public Set<User> getEnrolledUsers() {
        return enrolledUsers;
    }

    

    public void enrolledUsers(User user) {
       this.enrolledUsers.add(user);
    }

    public void assignOwner(User user) {
        this.owner=user;
    }
    public void enrolledIssues(Issue issue) {
        this.issues.add(issue);
    }

    public void addNewStatus(IStatus status2) {
        this.enrolledStatus.add(status2);
    }

   
    

    

    
}
