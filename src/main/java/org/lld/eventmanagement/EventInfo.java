package org.lld.eventmanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EventInfo implements IEventInfo {

    private final String eventName;
    private final LocalDate eventDate;
    private final int capacity;
    private final boolean canceled;
    private final List<IPerson> registrations = new ArrayList<>();
    private final List<IPerson> attendees = new ArrayList<>();

    public EventInfo(String eventName, LocalDate eventDate, int capacity, boolean canceled) {
        this.eventName = Objects.requireNonNull(eventName, "eventName cannot be null");
        this.eventDate = Objects.requireNonNull(eventDate, "eventDate cannot be null");
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity cannot be negative");
        }
        this.capacity = capacity;
        this.canceled = canceled;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public LocalDate getEventDate() {
        return eventDate;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public List<IPerson> getRegistrations() {
        return Collections.unmodifiableList(registrations);
    }

    @Override
    public List<IPerson> getAttendees() {
        return Collections.unmodifiableList(attendees);
    }

    @Override
    public synchronized boolean register(IPerson person) {
        Objects.requireNonNull(person, "person cannot be null");
        if (canceled || registrations.size() >= capacity || registrations.contains(person)) {
            return false;
        }
        registrations.add(person);
        return true;
    }

    @Override
    public synchronized boolean attend(IPerson person) {
        Objects.requireNonNull(person, "person cannot be null");
        if (canceled || !registrations.contains(person) || attendees.contains(person)) {
            return false;
        }
        attendees.add(person);
        return true;
    }
}


