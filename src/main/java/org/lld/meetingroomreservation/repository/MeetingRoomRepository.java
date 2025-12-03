package org.lld.meetingroomreservation.repository;

import org.lld.meetingroomreservation.domain.MeetingRoom;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing meeting rooms.
 */
public interface MeetingRoomRepository {
    /**
     * Saves a meeting room to the repository.
     * 
     * @param room The meeting room to save
     * @return The saved meeting room
     */
    MeetingRoom save(MeetingRoom room);

    /**
     * Finds a meeting room by ID.
     * 
     * @param roomId The room ID
     * @return Optional containing the room if found
     */
    Optional<MeetingRoom> findById(String roomId);

    /**
     * Finds all meeting rooms.
     * 
     * @return List of all meeting rooms
     */
    List<MeetingRoom> findAll();
}

