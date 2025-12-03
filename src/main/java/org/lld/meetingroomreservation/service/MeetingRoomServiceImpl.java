package org.lld.meetingroomreservation.service;

import org.lld.meetingroomreservation.domain.Booking;
import org.lld.meetingroomreservation.domain.MeetingRoom;
import org.lld.meetingroomreservation.domain.TimeSlot;
import org.lld.meetingroomreservation.repository.BookingRepository;
import org.lld.meetingroomreservation.repository.MeetingRoomRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe implementation of MeetingRoomService.
 * Uses per-room locks to prevent race conditions during booking operations.
 */
public class MeetingRoomServiceImpl implements MeetingRoomService {
    private final MeetingRoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    
    // Per-room locks to prevent race conditions when booking the same room
    private final java.util.concurrent.ConcurrentMap<String, Lock> roomLocks = new java.util.concurrent.ConcurrentHashMap<>();

    public MeetingRoomServiceImpl(MeetingRoomRepository roomRepository, BookingRepository bookingRepository) {
        if (roomRepository == null) {
            throw new IllegalArgumentException("MeetingRoomRepository cannot be null");
        }
        if (bookingRepository == null) {
            throw new IllegalArgumentException("BookingRepository cannot be null");
        }
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void addRoom(MeetingRoom room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        roomRepository.save(room);
        // Initialize lock for the room
        roomLocks.putIfAbsent(room.getRoomId(), new ReentrantLock());
    }

    @Override
    public MeetingRoom getRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        return roomRepository.findById(roomId).orElse(null);
    }

    @Override
    public List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        TimeSlot requestedTimeSlot = new TimeSlot(startTime, endTime);
        List<MeetingRoom> availableRooms = new ArrayList<>();

        for (MeetingRoom room : roomRepository.findAll()) {
            if (isRoomAvailable(room.getRoomId(), requestedTimeSlot)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    @Override
    public boolean isRoomAvailable(String roomId, TimeSlot timeSlot) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (timeSlot == null) {
            throw new IllegalArgumentException("Time slot cannot be null");
        }

        List<Booking> roomBookings = bookingRepository.findByRoomId(roomId);
        if (roomBookings == null || roomBookings.isEmpty()) {
            return true;
        }

        // Check for overlaps with active bookings
        for (Booking booking : roomBookings) {
            if (booking.overlaps(timeSlot)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the lock for a specific room.
     * Used by the facade for thread-safe booking operations.
     * 
     * @param roomId The room ID
     * @return The lock for the room
     */
    public Lock getRoomLock(String roomId) {
        return roomLocks.computeIfAbsent(roomId, k -> new ReentrantLock());
    }
}

