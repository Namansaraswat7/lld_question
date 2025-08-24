package org.lld.customersupportsystem.domain;

import java.time.LocalDateTime;

public class Issue {
    private String issueId;
    private String transactionId;
    private IssueType issueType;
    private String subject;
    private String description;
    private String customerEmail;
    private IssueStatus issueStatus;
    private String resolution;
    private String assignedAgentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Issue(String issueId, String transactionId, IssueType issueType, String subject, String description, String customerEmail) {
        this.issueId = issueId;
        this.transactionId = transactionId;
        this.issueType = issueType;
        this.subject = subject;
        this.description = description;
        this.customerEmail = customerEmail;
        this.issueStatus = IssueStatus.OPEN;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAssignedAgentId() {
        return assignedAgentId;
    }

    public void setAssignedAgentId(String assignedAgentId) {
        this.assignedAgentId = assignedAgentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void updateStatus(IssueStatus issueStatus, String resolution) {
        this.issueStatus = issueStatus;
        this.resolution = resolution;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Issue{" +
                "issueId='" + issueId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", issueType=" + issueType +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", issueStatus=" + issueStatus +
                ", resolution='" + resolution + '\'' +
                ", assignedAgentId='" + assignedAgentId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
