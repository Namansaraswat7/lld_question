package org.lld.eventmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class EventManager implements IEventManager {

    private final List<IEventInfo> events = new ArrayList<>();

    @Override
    public synchronized boolean addEvent(IEventInfo event) {
        Objects.requireNonNull(event, "event cannot be null");
        if (findEvent(event.getEventName()) != null) {
            return false;
        }
        events.add(event);
        return true;
    }

    @Override
    public boolean register(String eventName, IPerson person) {
        IEventInfo event = findEvent(eventName);
        if (event == null) {
            return false;
        }
        return event.register(person);
    }

    @Override
    public boolean attend(String eventName, IPerson person) {
        IEventInfo event = findEvent(eventName);
        if (event == null) {
            return false;
        }
        return event.attend(person);
    }

    @Override
    public List<String> getEventCountByYears() {
        Map<Integer, Integer> counter = new TreeMap<>();
        synchronized (this) {
            for (IEventInfo event : events) {
                int year = event.getEventDate().getYear();
                // Traditional approach: get current count (or 0 if not present) and add 1
                int currentCount = counter.getOrDefault(year, 0);
                counter.put(year, currentCount + 1);
            }
        }
        return formatCounter(counter);
    }

    @Override
    public List<String> getEventRegistrationCountByYears() {
        Map<Integer, Integer> counter = new TreeMap<>();
        synchronized (this) {
            for (IEventInfo event : events) {
                int year = event.getEventDate().getYear();
                int registrationCount = event.getRegistrations().size();
                // Traditional approach: get current total (or 0 if not present) and add registration count
                int currentTotal = counter.getOrDefault(year, 0);
                counter.put(year, currentTotal + registrationCount);
            }
        }
        return formatCounter(counter);
    }

    @Override
    public List<String> getEventAttendeesCountByYears() {
        Map<Integer, Integer> counter = new TreeMap<>();
        synchronized (this) {
            for (IEventInfo event : events) {
                int year = event.getEventDate().getYear();
                int attendeeCount = event.getAttendees().size();
                // Traditional approach: get current total (or 0 if not present) and add attendee count
                int currentTotal = counter.getOrDefault(year, 0);
                counter.put(year, currentTotal + attendeeCount);
            }
        }
        return formatCounter(counter);
    }

    private IEventInfo findEvent(String eventName) {
        Objects.requireNonNull(eventName, "eventName cannot be null");
        synchronized (this) {
            return events.stream()
                    .filter(event -> event.getEventName().equalsIgnoreCase(eventName))
                    .findFirst()
                    .orElse(null);
        }
    }

    private List<String> formatCounter(Map<Integer, Integer> counter) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            result.add(entry.getKey() + " - " + entry.getValue());
        }
        return result;
    }
}


