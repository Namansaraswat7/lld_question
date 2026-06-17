package org.lld.taskscheduler.strategy;

import java.time.LocalDateTime;
import java.util.Optional;

public class OneTimeScheduleStrategy implements ScheduleStrategy {

    private final LocalDateTime executionTime;

    public OneTimeScheduleStrategy(LocalDateTime executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        if (lastExecutionTime == null) {
            return Optional.of(executionTime);
        }
        return Optional.empty();
    }
}
