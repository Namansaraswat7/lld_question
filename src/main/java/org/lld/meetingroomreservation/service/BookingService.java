package org.lld.meetingroomreservation.service;

import org.lld.meetingroomreservation.domain.Booking;

import java.util.List;

/**
 * Service interface for booking operations.
 * Follows Service Layer pattern for business logic abstraction.
 */
public interface BookingService {
    /**
     * Creates a new booking.
     * 
     * @param booking The booking to create
     * @return The created booking
     */
    Booking createBooking(Booking booking);

    /**
     * Cancels a booking by ID.
     * 
     * @param bookingId The booking ID to cancel
     * @return true if cancellation was successful, false otherwise
     */
    boolean cancelBooking(String bookingId);

    /**
     * Gets a booking by ID.
     * 
     * @param bookingId The booking ID
     * @return The booking if found, null otherwise
     */
    Booking getBooking(String bookingId);

    /**
     * Gets all bookings for a specific room.
     * 
     * @param roomId The room ID
     * @return List of bookings for the room, sorted by start time
     */
    List<Booking> getBookingsForRoom(String roomId);

    /**
     * Gets all bookings for a specific employee.
     * 
     * @param employeeId The employee ID
     * @return List of bookings for the employee, sorted by start time
     */
    List<Booking> getBookingsForEmployee(String employeeId);
}

