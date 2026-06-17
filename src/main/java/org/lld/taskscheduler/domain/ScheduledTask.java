package org.lld.taskscheduler.domain;

import org.lld.taskscheduler.strategy.ScheduleStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ScheduledTask implements Delayed {

    private final String id;
    private final Task task;
    private final ScheduleStrategy strategy;
    private final long sequenceNumber; // Tie T1 and T1 have same time then we need something else to compare
    // Task T1 at time 3s seq = 123 -> executed at 5s High
    // TaskT2 at time 3.1 seqNo = 124 executed at 5s Low



    // Task Priority -> HIGH / MEDIUM /LOW

    private volatile LocalDateTime nextExecutionTime;
    private volatile LocalDateTime lastExecutionTime;
    private TaskStatus status;

    public ScheduledTask(Task task, ScheduleStrategy strategy, long sequenceNumber) {
        this.id = UUID.randomUUID().toString();
        this.task = Objects.requireNonNull(task, "task");
        this.strategy = Objects.requireNonNull(strategy, "strategy");
        this.sequenceNumber = sequenceNumber;
        this.status = TaskStatus.SCHEDULED;
        updateNextExecutionTime();
    }

    public void updateNextExecutionTime() {
        Optional<LocalDateTime> nextTime = strategy.getNextExecutionTime(lastExecutionTime);
        nextExecutionTime = nextTime.orElse(null);
    }

    public void updateLastExecutionTime() {
        lastExecutionTime = nextExecutionTime;
    }

    public boolean hasMoreExecutions() {
        return nextExecutionTime != null;
    }

    public boolean isCancelled() {
        return status == TaskStatus.CANCELLED;
    }

    public synchronized boolean cancel() {
        if (status != TaskStatus.SCHEDULED) {
            return false;
        }
        status = TaskStatus.CANCELLED;
        return true;
    }

    public synchronized boolean tryMarkRunning() {
        if (status != TaskStatus.SCHEDULED) {
            return false;
        }
        status = TaskStatus.RUNNING;
        return true;
    }

    public synchronized void setStatus(TaskStatus status) {
        this.status = Objects.requireNonNull(status, "status");
    }

    @Override
    public long getDelay(TimeUnit unit) {
        if (nextExecutionTime == null) {
            return 0;
        }
        long millis = Duration.between(LocalDateTime.now(), nextExecutionTime).toMillis();
        return unit.convert(Math.max(0, millis), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        if (other instanceof ScheduledTask otherTask) {
            if (nextExecutionTime == null && otherTask.nextExecutionTime == null) {
                return Long.compare(sequenceNumber, otherTask.sequenceNumber);
            }
            if (nextExecutionTime == null) {
                return 1;
            }
            if (otherTask.nextExecutionTime == null) {
                return -1;
            }
            int byTime = nextExecutionTime.compareTo(otherTask.nextExecutionTime);
            if (byTime != 0) {
                return byTime;
            }
            return Long.compare(sequenceNumber, otherTask.sequenceNumber);
        }
        return Long.compare(getDelay(TimeUnit.MILLISECONDS), other.getDelay(TimeUnit.MILLISECONDS));
    }

    public String getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public ScheduleStrategy getStrategy() {
        return strategy;
    }

    public LocalDateTime getNextExecutionTime() {
        return nextExecutionTime;
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime;
    }

    public synchronized TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ScheduledTask{" +
                "id='" + id + '\'' +
                ", task=" + task.getName() +
                '}';
    }
}
