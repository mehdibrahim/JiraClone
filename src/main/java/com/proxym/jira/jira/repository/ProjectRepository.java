package com.proxym.jira.jira.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.proxym.jira.jira.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
   @Query(value = "SELECT * FROM projects p WHERE "+
   "p.name LIKE CONCAT('%',:query,'%')"+
   "OR p.description LIKE CONCAT('%',:query,'%')",nativeQuery=true)
   List<Project> searchProjects(String query);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM projects p where p.id=:projectId ",nativeQuery = true)
    public void deleteProjectById(Long projectId);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM user_enrolled e where e.project_id=:projectId",nativeQuery = true)
    public void deleteFromEnrolledUser(Long projectId);
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM user_enrolled e where e.user_id=:id",nativeQuery = true)
    public void deleteFromEnrolledTheMember(Long id);

}

