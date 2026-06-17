package org.lld.taskscheduler.repository;

import org.lld.taskscheduler.domain.ScheduledTask;
import org.lld.taskscheduler.domain.TaskStatus;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryTaskRepository implements TaskRepository {

    private final ConcurrentHashMap<String, ScheduledTask> tasks = new ConcurrentHashMap<>();

    @Override
    public void save(ScheduledTask task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Optional<ScheduledTask> findById(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    @Override
    public Optional<TaskStatus> findStatusById(String taskId) {
        return findById(taskId).map(ScheduledTask::getStatus);
    }
}
