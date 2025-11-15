package org.lld.eventmanagement;

import java.time.LocalDate;

public class EventManagementDemo {

    public static void main(String[] args) {
        IEventManager manager = new EventManager();

        IPerson jane = new Person("Jane", "Smith");
        IPerson john = new Person("John", "Doe");
        IPerson richard = new Person("Richard", "Roe");

        IEventInfo event1 = new EventInfo("Event1", LocalDate.of(2021, 6, 15), 3, false);
        IEventInfo event2 = new EventInfo("Event2", LocalDate.of(2020, 9, 10), 2, false);

        manager.addEvent(event1);
        manager.addEvent(event2);

        manager.register("Event1", john);
        manager.register("Event2", john);
        manager.register("Event1", richard);
        manager.register("Event1", jane);
        manager.register("Event2", jane);

        manager.attend("Event1", john);
        manager.attend("Event1", richard);
        manager.attend("Event2", jane);

        System.out.println("Event Count:");
        manager.getEventCountByYears().forEach(System.out::println);

        System.out.println("\nRegistrations:");
        manager.getEventRegistrationCountByYears().forEach(System.out::println);

        System.out.println("\nAttendees:");
        manager.getEventAttendeesCountByYears().forEach(System.out::println);
    }
}


