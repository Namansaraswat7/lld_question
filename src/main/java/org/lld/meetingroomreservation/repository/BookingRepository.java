package org.lld.meetingroomreservation.repository;

import org.lld.meetingroomreservation.domain.Booking;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing bookings.
 * Follows Repository pattern for data access abstraction.
 */
public interface BookingRepository {
    /**
     * Saves a booking to the repository.
     * 
     * @param booking The booking to save
     * @return The saved booking
     */
    Booking save(Booking booking);

    /**
     * Finds a booking by its ID.
     * 
     * @param bookingId The booking ID
     * @return Optional containing the booking if found
     */
    Optional<Booking> findById(String bookingId);

    /**
     * Finds all bookings for a specific room.
     * 
     * @param roomId The room ID
     * @return List of bookings for the room
     */
    List<Booking> findByRoomId(String roomId);

    /**
     * Finds all bookings for a specific employee.
     * 
     * @param employeeId The employee ID
     * @return List of bookings for the employee
     */
    List<Booking> findByEmployeeId(String employeeId);

    /**
     * Updates a booking in the repository.
     * 
     * @param booking The booking to update
     * @return The updated booking
     */
    Booking update(Booking booking);
}

