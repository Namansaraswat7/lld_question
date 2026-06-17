package org.lld.taskscheduler.repository;

import org.lld.taskscheduler.domain.ScheduledTask;
import org.lld.taskscheduler.domain.TaskStatus;

import java.util.Optional;

public interface TaskRepository {

    void save(ScheduledTask task);

    Optional<ScheduledTask> findById(String taskId);

    Optional<TaskStatus> findStatusById(String taskId);
}
