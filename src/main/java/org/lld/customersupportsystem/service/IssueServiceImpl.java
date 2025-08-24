package org.lld.customersupportsystem.service;

import org.lld.customersupportsystem.domain.Issue;
import org.lld.customersupportsystem.domain.IssueStatus;
import org.lld.customersupportsystem.domain.IssueType;
import org.lld.customersupportsystem.repository.IssueRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;

    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Issue createIssue(String transactionId, IssueType issueType, String subject,
                             String description, String email) {

        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        if (issueType == null) {
            throw new IllegalArgumentException("Issue type cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String issueId = issueRepository.getNextIssueId();
        Issue issue = new Issue(issueId, transactionId, issueType, subject, description, email);

        return issueRepository.save(issue);
    }

    @Override
    public Optional<Issue> getIssue(String issueId) {
        if (issueId == null || issueId.trim().isEmpty()) {
            return Optional.empty();
        }
        return issueRepository.findById(issueId);
    }

    @Override
    public List<Issue> getIssues(Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return issueRepository.findAll();
        }
        return issueRepository.findByFilters(filters);
    }

    @Override
    public Issue updateIssue(String issueId, String status, String resolution) {
        Optional<Issue> issueOpt = issueRepository.findById(issueId);

        if (issueOpt.isEmpty()) {
            throw new IllegalArgumentException("Issue with ID " + issueId + " not found");
        }

        Issue issue = issueOpt.get();

        // Update status if provided
        if (status != null && !status.trim().isEmpty()) {
            try {
                IssueStatus issueStatus = parseStatus(status);
                issue.setIssueStatus(issueStatus);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        }

        // Update resolution if provided
        if (resolution != null && !resolution.trim().isEmpty()) {
            issue.setResolution(resolution);
        }

        return issueRepository.update(issue);
    }

    @Override
    public Issue resolveIssue(String issueId, String resolution) {
        Optional<Issue> issueOpt = issueRepository.findById(issueId);

        if (issueOpt.isEmpty()) {
            throw new IllegalArgumentException("Issue with ID " + issueId + " not found");
        }

        Issue issue = issueOpt.get();
        issue.updateStatus(IssueStatus.RESOLVED, resolution);

        return issueRepository.update(issue);
    }

    @Override
    public boolean assignIssueToAgent(String issueId, String agentId) {
        Optional<Issue> issueOpt = issueRepository.findById(issueId);

        if (issueOpt.isEmpty()) {
            return false;
        }

        Issue issue = issueOpt.get();
        issue.setAssignedAgentId(agentId);
        issueRepository.update(issue);

        return true;
    }

    private IssueStatus parseStatus(String status) {
        for (IssueStatus issueStatus : IssueStatus.values()) {
            if (issueStatus.name().equalsIgnoreCase(status)) {
                return issueStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
