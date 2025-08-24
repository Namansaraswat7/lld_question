package org.lld.customersupportsystem.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Agent {
    private String agentId;
    private String email;
    private String name;
    private List<IssueType> specializedIssueTypes;
    private Set<String> assignedIssueIds;
    private List<String> workHistory;

    private boolean isAvailable;


    public Agent(String agentId, String email, String name, List<IssueType> specializedIssueTypes) {
        this.agentId = agentId;
        this.email = email;
        this.name = name;
        this.specializedIssueTypes = specializedIssueTypes;
        this.assignedIssueIds = new HashSet<>();
        this.workHistory = new ArrayList<>();
        this.isAvailable = true;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IssueType> getSpecializedIssueTypes() {
        return specializedIssueTypes;
    }

    public void setSpecializedIssueTypes(List<IssueType> specializedIssueTypes) {
        this.specializedIssueTypes = specializedIssueTypes;
    }

    public Set<String> getAssignedIssueIds() {
        return assignedIssueIds;
    }

    public void setAssignedIssueIds(Set<String> assignedIssueIds) {
        this.assignedIssueIds = assignedIssueIds;
    }

    public List<String> getWorkHistory() {
        return workHistory;
    }

    public void setWorkHistory(List<String> workHistory) {
        this.workHistory = workHistory;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // business methods

    public boolean canHandleIssueType(IssueType issueType) {
        return specializedIssueTypes.contains(issueType);
    }

    public void assignIssue(String issueId) {
        assignedIssueIds.add(issueId);
        if(!workHistory.contains(issueId)) {
            workHistory.add(issueId);
        }
    }

    public void removeAssignedIssue(String issueId) {
        assignedIssueIds.remove(issueId);
    }

    public boolean isFree() {
        return assignedIssueIds.isEmpty();
    }

    public int getCurrentWorkload() {
        return assignedIssueIds.size();
    }

}
