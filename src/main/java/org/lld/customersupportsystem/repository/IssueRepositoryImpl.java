package org.lld.customersupportsystem.repository;

import org.lld.customersupportsystem.domain.Issue;
import org.lld.customersupportsystem.domain.IssueStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IssueRepositoryImpl implements IssueRepository{

    private final Map<String , Issue> issues;

    private final AtomicInteger issueCounter;

    public IssueRepositoryImpl() {
        this.issues = new ConcurrentHashMap<>();
        this.issueCounter = new AtomicInteger(0);
    }
    @Override
    public Issue save(Issue issue) {
        issues.put(issue.getIssueId(),issue);
        return issue;
    }

    @Override
    public Optional<Issue> findById(String issueId) {
        return Optional.ofNullable(issues.get(issueId));
    }

    @Override
    public List<Issue> findAll() {
        return new ArrayList<>(issues.values());
    }

    @Override
    public List<Issue> findByAssignedAgentId(String agentId) {
        return issues.values().stream()
                .filter(issue -> issue.getAssignedAgentId().equals(agentId))
                .toList();
    }

    @Override
    public List<Issue> findByFilters(Map<String, String> filters) {
        return issues.values().stream()
                .filter(issue -> matchesFilters(issue,filters))
                .collect(Collectors.toList());
    }


    private boolean matchesFilters(Issue issue, Map<String, String> filters) {
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String key = filter.getKey().toLowerCase();
            Object value = filter.getValue();

            switch (key) {
                case "email":
                    if (!issue.getCustomerEmail().equals(value)) {
                        return false;
                    }
                    break;
                case "type":
                case "issuetype":
                    if (!issue.getIssueType().name().equals(value)) {
                        return false;
                    }
                    break;
                case "status":
                    if (!issue.getIssueStatus().equals(value)) {
                        return false;
                    }
                    break;
                case "transactionid":
                    if (!issue.getTransactionId().equals(value)) {
                        return false;
                    }
                    break;
                case "assignedagent":
                    if (!Objects.equals(issue.getAssignedAgentId(), value)) {
                        return false;
                    }
                    break;
                default:
                    // Ignore unknown filters
                    break;
            }
        }
        return true;
    }


    @Override
    public Issue update(Issue issue) {
        if(issues.containsKey(issue.getIssueId())) {
            issues.put(issue.getIssueId(),issue);
            return issue;
        }
        throw new IllegalArgumentException("Issue with ID " + issue.getIssueId() + " not found");
    }

    @Override
    public String getNextIssueId() {
        int count = issueCounter.incrementAndGet();
        return "I" + count;
    }
}
