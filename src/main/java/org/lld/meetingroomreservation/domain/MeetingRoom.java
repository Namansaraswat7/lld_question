package org.lld.meetingroomreservation.domain;

import java.util.Objects;

/**
 * Represents a meeting room in the organization.
 */
public class MeetingRoom {
    private final String roomId;
    private final String name;
    private final int capacity;
    private final String location;

    public MeetingRoom(String roomId, String name, int capacity, String location) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be null or empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive");
        }
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeetingRoom that = (MeetingRoom) o;
        return Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "roomId='" + roomId + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                '}';
    }
}

