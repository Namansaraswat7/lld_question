package org.lld.taskscheduler.strategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class RecurringSchedulingStrategy implements ScheduleStrategy {
    private final Duration interval;

    public RecurringSchedulingStrategy(Duration interval) {
        this.interval = interval;
    }

    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        LocalDateTime baseTime = (lastExecutionTime == null) ? LocalDateTime.now() : lastExecutionTime;
        return Optional.of(baseTime.plus(interval));
    }
}
