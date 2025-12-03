package org.lld.meetingroomreservation.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a time interval with start and end times.
 * Immutable class to ensure thread-safety.
 * Provides overlap detection for conflict checking.
 */
public class TimeSlot {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Checks if this time slot overlaps with another time slot.
     * Two time slots overlap if they share any common time point.
     * 
     * @param other The other time slot to check against
     * @return true if there is any overlap, false otherwise
     */
    public boolean overlaps(TimeSlot other) {
        if (other == null) {
            return false;
        }
        // Overlap occurs if: this.start < other.end AND this.end > other.start
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    /**
     * Checks if this time slot contains the given time point.
     * 
     * @param time The time point to check
     * @return true if the time point is within this slot (inclusive start, exclusive end)
     */
    public boolean contains(LocalDateTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(startTime, timeSlot.startTime) && Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

