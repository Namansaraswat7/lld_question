package org.lld.taskscheduler.domain;

import java.time.LocalTime;

public class DataBackupTask implements Task {
    private final String source;
    private final String destination;

    public DataBackupTask(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void execute() {
        System.out.printf("[%s] Executing DataBackupTask: Backing up from %s to %s...%n",
                LocalTime.now(), source, destination);
        System.out.printf("[%s] DataBackupTask: Backup complete.%n", LocalTime.now());
    }

    @Override
    public String getName() {
        return "DataBackupTask";
    }
}
