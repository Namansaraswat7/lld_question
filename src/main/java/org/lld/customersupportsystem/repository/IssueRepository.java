package org.lld.customersupportsystem.repository;

import org.lld.customersupportsystem.domain.Issue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IssueRepository {

    Issue save(Issue issue);

    Optional<Issue> findById(String issueId);

    List<Issue> findAll();

    List<Issue> findByAssignedAgentId(String agentId);

    List<Issue> findByFilters(Map<String, String> filters);

    Issue update(Issue issue);

    String getNextIssueId();
}
