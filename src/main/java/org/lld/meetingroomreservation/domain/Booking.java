package org.lld.meetingroomreservation.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a booking for a meeting room.
 * Thread-safe with proper status management.
 */
public class Booking {
    private static final AtomicLong bookingIdGenerator = new AtomicLong(0);

    private final String bookingId;
    private final String employeeId;
    private final String roomId;
    private final TimeSlot timeSlot;
    private final LocalDateTime createdAt;
    private volatile BookingStatus status;

    public Booking(String employeeId, String roomId, TimeSlot timeSlot) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (timeSlot == null) {
            throw new IllegalArgumentException("Time slot cannot be null");
        }
        this.bookingId = "BK-" + bookingIdGenerator.incrementAndGet();
        this.employeeId = employeeId;
        this.roomId = roomId;
        this.timeSlot = timeSlot;
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.ACTIVE;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRoomId() {
        return roomId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Booking status cannot be null");
        }
        this.status = status;
    }

    public boolean isActive() {
        return status == BookingStatus.ACTIVE;
    }

    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }

    /**
     * Checks if this booking overlaps with the given time slot.
     * Only active bookings are considered for overlap.
     * 
     * @param timeSlot The time slot to check against
     * @return true if there is an overlap and booking is active, false otherwise
     */
    public boolean overlaps(TimeSlot timeSlot) {
        if (!isActive()) {
            return false;
        }
        return this.timeSlot.overlaps(timeSlot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", timeSlot=" + timeSlot +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}

