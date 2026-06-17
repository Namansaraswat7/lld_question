package org.lld.taskscheduler.domain;

import java.time.LocalTime;

public class PrintMessageTask implements Task {
    private final String message;

    public PrintMessageTask(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.printf("[%s] Executing PrintMessageTask: %s%n", LocalTime.now(), message);
    }

    @Override
    public String getName() {
        return message;
    }
}
