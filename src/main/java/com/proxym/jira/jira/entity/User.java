package com.proxym.jira.jira.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;




@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;
    @JsonIgnore
    private String name;
    private String username;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    @ManyToMany(mappedBy = "enrolledUsers")
    private Set<Project> projects = new HashSet<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "issueEnrolledUsers")
    private Set<Issue> issues = new HashSet<>();

   
    public Set<Project> getProjects() {
        return projects;
    }
    @JsonIgnore
    @OneToMany(mappedBy="owner")
    private Set<Project> ownerProject = new HashSet<>();

    public Set<Project> getOwnerProject() {
        return ownerProject;
    }

   


}
