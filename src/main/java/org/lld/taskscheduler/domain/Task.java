package org.lld.taskscheduler.domain;

public interface Task {
    void execute();

    String getName();
}
