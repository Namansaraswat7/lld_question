package org.lld.customersupportsystem.service;

import org.lld.customersupportsystem.domain.Agent;
import org.lld.customersupportsystem.domain.IssueType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AgentService {

    Agent addAgent(String email, String name, List<IssueType> issueTypes);

    Optional<Agent> getAgent(String agentId);

    List<Agent> getAllAgents();

    List<Agent> getAgentsByIssueType(IssueType issueType);

    List<Agent> getAvailableAgents();

    boolean assignIssue(String issueId);

    Map<String, List<String>> getAgentsWorkHistory();
}
