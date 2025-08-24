package org.lld.customersupportsystem;

import org.lld.customersupportsystem.domain.Agent;
import org.lld.customersupportsystem.domain.Issue;
import org.lld.customersupportsystem.domain.IssueType;
import org.lld.customersupportsystem.repository.AgentRepository;
import org.lld.customersupportsystem.repository.AgentRepositoryImpl;
import org.lld.customersupportsystem.repository.IssueRepository;
import org.lld.customersupportsystem.repository.IssueRepositoryImpl;
import org.lld.customersupportsystem.service.AgentService;
import org.lld.customersupportsystem.service.AgentServiceImpl;
import org.lld.customersupportsystem.service.IssueService;
import org.lld.customersupportsystem.service.IssueServiceImpl;
import org.lld.customersupportsystem.strategy.FreeAgentAssignmentStrategy;
import org.lld.customersupportsystem.strategy.IssueAssignmentStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerSupportSystem {

    private final IssueService issueService;
    private final AgentService agentService;

    public CustomerSupportSystem() {

        IssueRepository issueRepository = new IssueRepositoryImpl();
        AgentRepository agentRepository = new AgentRepositoryImpl();
        IssueService issueService = new IssueServiceImpl(issueRepository);
        IssueAssignmentStrategy assignmentStrategy = new FreeAgentAssignmentStrategy();

        this.issueService = issueService;
        this.agentService = new AgentServiceImpl(agentRepository,issueService,assignmentStrategy);

    }

    /**
     * Create a new customer issue
     * @param transactionId Transaction ID associated with the issue
     * @param issueType Type of issue (e.g., "Payment Related", "Mutual Fund Related")
     * @param subject Brief subject of the issue
     * @param description Detailed description of the issue
     * @param email Customer's email address
     * @return Success message with issue ID
     */
    public String createIssue(String transactionId, IssueType issueType, String subject,
                              String description, String email) {
        try {
            Issue issue = issueService.createIssue(transactionId, issueType, subject, description, email);
            return String.format("Issue %s created against transaction \"%s\"",
                    issue.getIssueId(), transactionId);
        } catch (Exception e) {
            return "Error creating issue: " + e.getMessage();
        }
    }
    /**
     * Add a new agent to the system
     * @param email Agent's email address
     * @param name Agent's name
     * @param issueTypes List of issue types the agent can handle
     * @return Success message with agent ID
     */
    public String addAgent(String email, String name, List<IssueType> issueTypes) {
        try {
            Agent agent = agentService.addAgent(email, name, issueTypes);
            return String.format("Agent %s created", agent.getAgentId());
        } catch (Exception e) {
            return "Error creating agent: " + e.getMessage();
        }
    }
    /**
     * Assign an issue to an available agent based on the assignment strategy
     * @param issueId ID of the issue to assign
     * @return Success message or information about assignment
     */
    public String assignIssue(String issueId) {
        try {
            Optional<Issue> issueOpt = issueService.getIssue(issueId);
            if (issueOpt.isEmpty()) {
                return "Issue " + issueId + " not found";
            }

            Issue issue = issueOpt.get();
            boolean assigned = agentService.assignIssue(issueId);

            if (assigned) {
                // Get updated issue to see which agent was assigned
                Optional<Issue> updatedIssue = issueService.getIssue(issueId);
                if (updatedIssue.isPresent() && updatedIssue.get().getAssignedAgentId() != null) {
                    return String.format("Issue %s assigned to agent %s",
                            issueId, updatedIssue.get().getAssignedAgentId());
                } else {
                    return String.format("Issue %s assigned", issueId);
                }
            } else {
                // Check if there are any agents who can handle this issue type but are busy
                List<Agent> capableAgents = agentService.getAgentsByIssueType(issue.getIssueType());
                if (!capableAgents.isEmpty()) {
                    // Find the least busy agent for waitlist
                    Agent leastBusyAgent = capableAgents.stream()
                            .min((a1, a2) -> Integer.compare(a1.getCurrentWorkload(), a2.getCurrentWorkload()))
                            .orElse(null);

                    if (leastBusyAgent != null) {
                        return String.format("Issue %s added to waitlist of Agent %s",
                                issueId, leastBusyAgent.getAgentId());
                    }
                }
                return "No suitable agent found for issue " + issueId;
            }
        } catch (Exception e) {
            return "Error assigning issue: " + e.getMessage();
        }
    }
    /**
     * Get issues based on filters
     * @param filters Map of filter criteria (e.g., {"email": "user@test.com"})
     * @return List of issues matching the criteria
     */
    public List<Issue> getIssues(Map<String, String> filters) {
        return issueService.getIssues(filters);
    }
    /**
     * Get issues formatted as strings based on filters (for display purposes)
     * @param filters Map of filter criteria
     * @return List of formatted issue strings
     */
    public List<String> getIssuesFormatted(Map<String, String> filters) {
        List<Issue> issues = issueService.getIssues(filters);
        return issues.stream()
                .map(Issue::toString)
                .collect(Collectors.toList());
    }
    /**
     * Update an issue's status and resolution
     * @param issueId ID of the issue to update
     * @param status New status for the issue
     * @param resolution Resolution description
     * @return Success message
     */
    public String updateIssue(String issueId, String status, String resolution) {
        try {
            issueService.updateIssue(issueId, status, resolution);
            return String.format("%s status updated to %s", issueId, status);
        } catch (Exception e) {
            return "Error updating issue: " + e.getMessage();
        }
    }
    /**
     * Resolve an issue with a resolution
     * @param issueId ID of the issue to resolve
     * @param resolution Resolution description
     * @return Success message
     */
    public String resolveIssue(String issueId, String resolution) {
        try {
            Issue issue = issueService.resolveIssue(issueId, resolution);

            // Remove issue from agent's assigned list when resolved
            if (issue.getAssignedAgentId() != null) {
                Optional<Agent> agentOpt = agentService.getAgent(issue.getAssignedAgentId());
                if (agentOpt.isPresent()) {
                    Agent agent = agentOpt.get();
                    agent.removeAssignedIssue(issueId);
                }
            }

            return String.format("%s issue marked resolved", issueId);
        } catch (Exception e) {
            return "Error resolving issue: " + e.getMessage();
        }
    }
    /**
     * View work history for all agents
     * @return Map of agent ID to list of transaction IDs they worked on
     */
    public Map<String, List<String>> viewAgentsWorkHistory() {
        Map<String, List<String>> workHistory = agentService.getAgentsWorkHistory();

        // Convert issue IDs to transaction IDs for display
        Map<String, List<String>> transactionHistory = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : workHistory.entrySet()) {
            String agentId = entry.getKey();
            List<String> issueIds = entry.getValue();

            List<String> transactionIds = issueIds.stream()
                    .map(issueId -> {
                        Optional<Issue> issue = issueService.getIssue(issueId);
                        return issue.map(Issue::getTransactionId).orElse("Unknown");
                    })
                    .collect(Collectors.toList());

            transactionHistory.put(agentId, transactionIds);
        }

        return transactionHistory;
    }
    /**
     * Get formatted work history for display
     * @return Formatted string showing agent work history
     */
    public String viewAgentsWorkHistoryFormatted() {
        Map<String, List<String>> history = viewAgentsWorkHistory();

        return history.entrySet().stream()
                .map(entry -> String.format("%s -> {%s}",
                        entry.getKey(),
                        String.join(", ", entry.getValue())))
                .collect(Collectors.joining(",\n"));
    }
    /**
     * Get all agents in the system
     */
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }
    /**
     * Get a specific issue by ID
     */
    public Optional<Issue> getIssue(String issueId) {
        return issueService.getIssue(issueId);
    }
}
