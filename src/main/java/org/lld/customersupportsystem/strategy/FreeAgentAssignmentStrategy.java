package org.lld.customersupportsystem.strategy;

import org.lld.customersupportsystem.domain.Agent;
import org.lld.customersupportsystem.domain.Issue;

import java.util.List;
import java.util.Optional;

public class FreeAgentAssignmentStrategy implements IssueAssignmentStrategy{

    @Override
    public Optional<Agent> assignIssue(Issue issue, List<Agent> availableAgents) {

        if(availableAgents.isEmpty()) return Optional.empty();

        // filter agents who can handle this issue type

        List<Agent> capableAgents = availableAgents.stream()
                .filter(agent -> agent.canHandleIssueType(issue.getIssueType()))
                .filter(Agent::isAvailable)
                .toList();

        if(capableAgents.isEmpty()) return Optional.empty();

        // First try to find completely free agent

        Optional<Agent> freeAgent = capableAgents.stream()
                .filter(Agent::isFree)
                .findFirst();

        if (freeAgent.isPresent()) return freeAgent;

        // if no free agent, assign to agent with lowest workload
        return capableAgents.stream()
                .min((a1,a2) -> Integer.compare(a1.getCurrentWorkload(),a2.getCurrentWorkload()));

    }
}
