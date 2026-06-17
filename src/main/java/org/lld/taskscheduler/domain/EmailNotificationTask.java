package org.lld.taskscheduler.domain;

import java.time.LocalTime;

public class EmailNotificationTask implements Task {
    private final String recipient;
    private final String subject;
    private final String body;

    public EmailNotificationTask(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public void execute() {
        System.out.printf("[%s] Executing EmailNotificationTask: sending to %s%n",
                LocalTime.now(), recipient);
        System.out.printf("[%s] Subject: %s%n", LocalTime.now(), subject);
        System.out.printf("[%s] Body: %s%n", LocalTime.now(), body);
        System.out.printf("[%s] EmailNotificationTask: sent successfully.%n", LocalTime.now());
    }

    @Override
    public String getName() {
        return "EmailNotificationTask";
    }
}
