package org.lld.meetingroomreservation.repository;

import org.lld.meetingroomreservation.domain.Booking;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe implementation of BookingRepository.
 * Uses ConcurrentHashMap for thread safety and maintains indexes for efficient queries.
 */
public class BookingRepositoryImpl implements BookingRepository {
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    
    // Indexes for efficient queries
    private final Map<String, List<Booking>> bookingsByRoom = new ConcurrentHashMap<>();
    private final Map<String, List<Booking>> bookingsByEmployee = new ConcurrentHashMap<>();

    @Override
    public Booking save(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        
        bookings.put(booking.getBookingId(), booking);
        
        // Update indexes
        bookingsByRoom.computeIfAbsent(booking.getRoomId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(booking);
        bookingsByEmployee.computeIfAbsent(booking.getEmployeeId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(booking);
        
        return booking;
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(bookings.get(bookingId));
    }

    @Override
    public List<Booking> findByRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Booking> roomBookings = bookingsByRoom.get(roomId);
        if (roomBookings == null) {
            return Collections.emptyList();
        }
        
        synchronized (roomBookings) {
            return new ArrayList<>(roomBookings);
        }
    }

    @Override
    public List<Booking> findByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Booking> employeeBookings = bookingsByEmployee.get(employeeId);
        if (employeeBookings == null) {
            return Collections.emptyList();
        }
        
        synchronized (employeeBookings) {
            return new ArrayList<>(employeeBookings);
        }
    }

    @Override
    public Booking update(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        
        if (!bookings.containsKey(booking.getBookingId())) {
            throw new IllegalArgumentException("Booking not found: " + booking.getBookingId());
        }
        
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }
}

