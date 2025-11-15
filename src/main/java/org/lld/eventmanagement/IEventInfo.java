package org.lld.eventmanagement;

import java.time.LocalDate;
import java.util.List;

public interface IEventInfo {
    String getEventName();

    LocalDate getEventDate();

    int getCapacity();

    boolean isCanceled();

    List<IPerson> getRegistrations();

    List<IPerson> getAttendees();

    boolean register(IPerson person);

    boolean attend(IPerson person);
}


