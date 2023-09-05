package com.proxym.jira.jira.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proxym.jira.jira.entity.Issue;



@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query(value = "SELECT * FROM issues i WHERE (i.title LIKE CONCAT('%',:query,'%') OR i.description LIKE CONCAT('%',:query,'%')) AND i.project_id=:projectId",nativeQuery=true)
    List<Issue> searchIssues(Long projectId,String query);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM issues i where i.id=:issueId ",nativeQuery = true)
    public void deleteIssueById(Long issueId);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM issue_enrolled e where e.issue_id=:issueId",nativeQuery = true)
    public void deleteFromEnrolledUser(Long issueId);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM issue_enrolled e where e.user_id=:id",nativeQuery = true)
    public void deleteFromEnrolledTheMember(Long id);

    @Modifying
    @Transactional
    @Query(value ="SELECT * FROM  issues e where e.project_id=:projectId",nativeQuery = true)
    List<Issue> findAllByProjectId(Long projectId);

}
