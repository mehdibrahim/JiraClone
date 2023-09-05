package com.proxym.jira.service.ProjectServiceImplementation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proxym.jira.jira.entity.Issue;
import com.proxym.jira.jira.repository.IssueRepository;
import com.proxym.jira.service.IssueService;



@Service
public class IssueServiceImpl implements IssueService {
    @Autowired
    private IssueRepository issueRepository;
    
    
    @Override
    public List<Issue> searchIssues(Long projectId,String query) {
        List<Issue> issues =issueRepository.searchIssues(projectId,query);
        return issues;
    }
    
}
