package org.lld.meetingroomreservation.service;

import org.lld.meetingroomreservation.domain.Booking;
import org.lld.meetingroomreservation.domain.BookingStatus;
import org.lld.meetingroomreservation.repository.BookingRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Thread-safe implementation of BookingService.
 */
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        if (bookingRepository == null) {
            throw new IllegalArgumentException("BookingRepository cannot be null");
        }
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        return bookingRepository.findById(bookingId)
                .map(booking -> {
                    if (booking.isActive()) {
                        booking.setStatus(BookingStatus.CANCELLED);
                        bookingRepository.update(booking);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public Booking getBooking(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    public List<Booking> getBookingsForRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        return bookingRepository.findByRoomId(roomId).stream()
                .sorted(Comparator.comparing(b -> b.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsForEmployee(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        return bookingRepository.findByEmployeeId(employeeId).stream()
                .sorted(Comparator.comparing(b -> b.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());
    }
}

