package org.lld.customersupportsystem.repository;

import org.lld.customersupportsystem.domain.Agent;
import org.lld.customersupportsystem.domain.IssueType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AgentRepositoryImpl implements AgentRepository {

    private final Map<String, Agent> agents;

    private final AtomicInteger agentCounter;

    public AgentRepositoryImpl() {
        this.agents = new ConcurrentHashMap<>();
        this.agentCounter = new AtomicInteger(0);
    }

    @Override
    public Agent save(Agent agent) {
        agents.put(agent.getAgentId(),agent);
        return agent;
    }

    @Override
    public Optional<Agent> findById(String agentId) {
        return Optional.ofNullable(agents.get(agentId));
    }

    @Override
    public Optional<Agent> findByEmail(String email) {
        return agents.values().stream()
                .filter(agent -> agent.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Agent> findAll() {
        return new ArrayList<>(agents.values());
    }

    @Override
    public List<Agent> findBySpecializedIssueType(IssueType issueType) {
        return agents.values().stream()
                .filter(agent -> agent.canHandleIssueType(issueType))
                .collect(Collectors.toList());
    }

    @Override
    public List<Agent> findAvailableAgents() {
        return agents.values().stream()
                .filter(Agent::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Agent update(Agent agent) {
        if(agents.containsKey(agent.getAgentId())) {
            agents.put(agent.getAgentId(),agent);
            return agent;
        }
        throw new IllegalArgumentException("Agent with ID " + agent.getAgentId() + " not found");
    }

    @Override
    public String getNextAgentId() {
        int nextId = agentCounter.incrementAndGet();
        return "A" + nextId;
    }
}
