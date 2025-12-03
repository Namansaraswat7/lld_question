package org.lld.meetingroomreservation.service;

import org.lld.meetingroomreservation.domain.MeetingRoom;
import org.lld.meetingroomreservation.domain.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for meeting room operations.
 */
public interface MeetingRoomService {
    /**
     * Adds a meeting room to the system.
     * 
     * @param room The meeting room to add
     */
    void addRoom(MeetingRoom room);

    /**
     * Gets a meeting room by ID.
     * 
     * @param roomId The room ID
     * @return The meeting room if found, null otherwise
     */
    MeetingRoom getRoom(String roomId);

    /**
     * Gets all available rooms for a given time interval.
     * 
     * @param startTime The start time
     * @param endTime The end time
     * @return List of available meeting rooms
     */
    List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Checks if a room is available during the given time slot.
     * Thread-safe operation with proper locking.
     * 
     * @param roomId The room ID
     * @param timeSlot The time slot to check
     * @return true if room is available, false otherwise
     */
    boolean isRoomAvailable(String roomId, TimeSlot timeSlot);
}

