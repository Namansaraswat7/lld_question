package org.lld.customersupportsystem.service;

import org.lld.customersupportsystem.domain.Agent;
import org.lld.customersupportsystem.domain.Issue;
import org.lld.customersupportsystem.domain.IssueType;
import org.lld.customersupportsystem.repository.AgentRepository;
import org.lld.customersupportsystem.strategy.IssueAssignmentStrategy;

import java.util.*;
import java.util.stream.Collectors;

public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final IssueService issueService;
    private final IssueAssignmentStrategy assignmentStrategy;

    public AgentServiceImpl(AgentRepository agentRepository,
                            IssueService issueService,
                            IssueAssignmentStrategy assignmentStrategy) {
        this.agentRepository = agentRepository;
        this.issueService = issueService;
        this.assignmentStrategy = assignmentStrategy;
    }

    @Override
    public Agent addAgent(String email, String name, List<IssueType> issueTypes) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (issueTypes == null || issueTypes.isEmpty()) {
            throw new IllegalArgumentException("Issue types cannot be null or empty");
        }

        // Check if agent with this email already exists
        Optional<Agent> existingAgent = agentRepository.findByEmail(email);
        if (existingAgent.isPresent()) {
            throw new IllegalArgumentException("Agent with email " + email + " already exists");
        }

        String agentId = agentRepository.getNextAgentId();
        Agent agent = new Agent(agentId, email, name, issueTypes);

        return agentRepository.save(agent);
    }

    @Override
    public Optional<Agent> getAgent(String agentId) {
        if (agentId == null || agentId.trim().isEmpty()) {
            return Optional.empty();
        }
        return agentRepository.findById(agentId);
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @Override
    public List<Agent> getAgentsByIssueType(IssueType issueType) {
        return agentRepository.findBySpecializedIssueType(issueType);
    }

    @Override
    public List<Agent> getAvailableAgents() {
        return agentRepository.findAvailableAgents();
    }

    @Override
    public boolean assignIssue(String issueId) {
        Optional<Issue> issueOpt = issueService.getIssue(issueId);

        if (issueOpt.isEmpty()) {
            return false;
        }

        Issue issue = issueOpt.get();

        // Get all available agents
        List<Agent> availableAgents = getAvailableAgents();

        // Use strategy to find suitable agent
        Optional<Agent> selectedAgent = assignmentStrategy.assignIssue(issue, availableAgents);

        if (selectedAgent.isPresent()) {
            Agent agent = selectedAgent.get();

            // Assign issue to agent
            agent.assignIssue(issueId);
            agentRepository.update(agent);

            // Update issue with assigned agent
            issueService.assignIssueToAgent(issueId, agent.getAgentId());

            return true;
        }

        return false; // No suitable agent found
    }

    @Override
    public Map<String, List<String>> getAgentsWorkHistory() {
        return agentRepository.findAll().stream()
                .collect(Collectors.toMap(
                        agent -> agent.getAgentId(),
                        agent -> agent.getWorkHistory()
                ));
    }

//    @Override
//    public List<String> getAgentWorkHistory(String agentId) {
//        Optional<Agent> agent = agentRepository.findById(agentId);
//        return agent.map(Agent::getWorkHistory).orElse(new ArrayList<>());
//    }

//    @Override
//    public void setAgentAvailability(String agentId, boolean available) {
//        Optional<Agent> agentOpt = agentRepository.findById(agentId);
//
//        if (agentOpt.isPresent()) {
//            Agent agent = agentOpt.get();
//            agent.setAvailable(available);
//            agentRepository.update(agent);
//        } else {
//            throw new IllegalArgumentException("Agent with ID " + agentId + " not found");
//        }
//    }
}
