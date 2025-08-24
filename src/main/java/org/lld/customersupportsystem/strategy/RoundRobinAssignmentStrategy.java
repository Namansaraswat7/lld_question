package org.lld.customersupportsystem.strategy;

import org.lld.customersupportsystem.domain.Agent;
import org.lld.customersupportsystem.domain.Issue;

import java.util.List;
import java.util.Optional;

public class RoundRobinAssignmentStrategy implements IssueAssignmentStrategy{
    @Override
    public Optional<Agent> assignIssue(Issue issue, List<Agent> availableAgents) {
        return Optional.empty();
    }
}
