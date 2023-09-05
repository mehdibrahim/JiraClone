package com.proxym.jira.jira.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.proxym.jira.jira.entity.IStatus;
import com.proxym.jira.jira.entity.Issue;

@Repository
public interface IssueStatusRepository extends JpaRepository<IStatus, Long>{

    @Modifying
    @Transactional
    @Query(value ="DELETE FROM istatus_enrolled e where e.project_id=:projectId",nativeQuery = true)
    public void deleteFromEnrolledStatus(Long projectId);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM iStatus p where p.id=:id ",nativeQuery = true)
    public void deleteStatusById(Long id);

    @Query(value ="SELECT * FROM iStatus p where p.status=:status ",nativeQuery = true)
    public Optional<Issue> findByStatus(String status);
}
