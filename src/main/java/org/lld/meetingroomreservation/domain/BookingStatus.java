package org.lld.meetingroomreservation.domain;

/**
 * Enum representing the status of a booking.
 */
public enum BookingStatus {
    ACTIVE,      // Booking is active and confirmed
    CANCELLED,   // Booking has been cancelled
    COMPLETED    // Booking time has passed
}

