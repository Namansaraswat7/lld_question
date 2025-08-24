package org.lld.customersupportsystem;

import org.lld.customersupportsystem.domain.Issue;
import org.lld.customersupportsystem.domain.IssueType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerSupportDemo {

    public static void main(String[] args) {

        CustomerSupportSystem css = new CustomerSupportSystem();

        System.out.println("--- Customer support system demo ---");
        System.out.println();

        // create issues
        System.out.println("--- creating issues ---");
        String issueDetails1 =  css.createIssue("t1", IssueType.PAYMENT,"Payment failed",
                "Payment failed but money is debited", "testUser1@test.com");
        System.out.println(issueDetails1);

        String issueDetails2 =  css.createIssue("t2", IssueType.MUTUAL_FUND,"Purchase failed",
                "Unable to purchase mutual fund", "testUser2@test.com");
        System.out.println(issueDetails2);

        String issueDetails3 =  css.createIssue("t3", IssueType.PAYMENT,"Payment failed",
                "Payment status is showing as in progress", "testUser2@test.com");
        System.out.println(issueDetails3);
        System.out.println();

        // add Agents

        System.out.println("--- adding agents ---");

        String agentDetails1 = css.addAgent("agent1@test.com","Agent 1", Arrays.asList(IssueType.GOLD,IssueType.PAYMENT));
        System.out.println(agentDetails1);

        String agentDetails2 = css.addAgent("agent2@test.com","Agent 2", Arrays.asList(IssueType.MUTUAL_FUND,IssueType.PAYMENT));
        System.out.println(agentDetails2);
        System.out.println();

        // Assign Issues
        System.out.println("--- Assigning issues ---");

        String issueAssignmentDetail1 = css.assignIssue("I1");
        System.out.println(issueAssignmentDetail1);

        String issueAssignmentDetail2 = css.assignIssue("I2");
        System.out.println(issueAssignmentDetail2);

        String issueAssignmentDetail3= css.assignIssue("I3");
        System.out.println(issueAssignmentDetail3);

        System.out.println();

        //Get Issues by email filter
        System.out.println("4. Get Issues by Email Filter:");
        Map<String, String> emailFilter = new HashMap<>();
        emailFilter.put("email", "testUser2@test.com");

        List<Issue> issuesForUser2 = css.getIssues(emailFilter);
        System.out.println(">>> Issues for testUser2@test.com:");
        for (Issue issue : issuesForUser2) {
            System.out.println(issue.toString());
        }

        System.out.println();

        // Get Issues by type filter
        System.out.println("5. Get Issues by Type Filter:");
        Map<String, String> typeFilter = new HashMap<>();
        typeFilter.put("type", "PAYMENT");

        List<Issue> paymentIssues = css.getIssues(typeFilter);
        System.out.println(">>> Issues for Payment Related type:");
        for (Issue issue : paymentIssues) {
            System.out.println(issue.toString());
        }

        System.out.println();

        // Update Issue Status
        System.out.println("6. Updating Issue Status:");
        System.out.println(">>> " + css.updateIssue("I3", "IN_PROGRESS", "Waiting for payment confirmation"));

        System.out.println();

        // Test 7: Resolve Issue
        System.out.println("7. Resolving Issue:");
        System.out.println(">>> " + css.resolveIssue("I3", "PaymentFailed debited amount will get reversed"));

        System.out.println();

        // Test 8: View Agents Work History
        System.out.println("8. View Agents Work History:");
        System.out.println(">>> " + css.viewAgentsWorkHistoryFormatted());

        System.out.println();
        System.out.println("=== Demo Completed Successfully ===");
















    }
}
