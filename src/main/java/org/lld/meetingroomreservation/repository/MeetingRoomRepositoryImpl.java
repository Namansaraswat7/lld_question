package org.lld.meetingroomreservation.repository;

import org.lld.meetingroomreservation.domain.MeetingRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe implementation of MeetingRoomRepository.
 */
public class MeetingRoomRepositoryImpl implements MeetingRoomRepository {
    private final ConcurrentMap<String, MeetingRoom> rooms = new ConcurrentHashMap<>();

    @Override
    public MeetingRoom save(MeetingRoom room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        rooms.put(room.getRoomId(), room);
        return room;
    }

    @Override
    public Optional<MeetingRoom> findById(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(rooms.get(roomId));
    }

    @Override
    public List<MeetingRoom> findAll() {
        return new ArrayList<>(rooms.values());
    }
}

