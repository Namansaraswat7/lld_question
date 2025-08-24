package org.lld.customersupportsystem.service;

import org.lld.customersupportsystem.domain.Issue;
import org.lld.customersupportsystem.domain.IssueType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IssueService {

    Issue createIssue(String transactionId, IssueType issueType, String subject,
                      String description, String email);

    Optional<Issue> getIssue(String issueId);

    List<Issue> getIssues(Map<String, String> filter);

    Issue updateIssue(String issueId, String status, String resolution);

    Issue resolveIssue(String issueId, String resolution);

    boolean assignIssueToAgent(String issueId, String agentId);

}
