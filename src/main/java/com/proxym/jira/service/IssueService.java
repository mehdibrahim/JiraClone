package com.proxym.jira.service;

import java.util.List;

import com.proxym.jira.jira.entity.Issue;

public interface IssueService {
    List<Issue> searchIssues(Long projectId, String query);
}
