package org.lld.taskscheduler.strategy;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleStrategy {

    Optional<LocalDateTime> getNextExecutionTime(LocalDateTime time);
}
