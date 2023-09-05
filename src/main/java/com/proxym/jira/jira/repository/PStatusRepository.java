package com.proxym.jira.jira.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proxym.jira.jira.entity.PStatus;


@Repository
public interface PStatusRepository extends JpaRepository<PStatus, Long> {

    }