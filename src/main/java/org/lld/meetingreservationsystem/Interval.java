package org.lld.meetingreservationsystem;

import java.time.LocalDateTime;

public class Interval {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Interval(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDateTime = startDate;
        this.endDateTime = endDate;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
