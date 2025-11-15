package org.lld.eventmanagement;

import java.util.List;

public interface IEventManager {

    boolean addEvent(IEventInfo event);

    boolean register(String eventName, IPerson person);

    boolean attend(String eventName, IPerson person);

    List<String> getEventCountByYears();

    List<String> getEventRegistrationCountByYears();

    List<String> getEventAttendeesCountByYears();
}


