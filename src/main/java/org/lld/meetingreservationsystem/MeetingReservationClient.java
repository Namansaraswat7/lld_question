package org.lld.meetingreservationsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MeetingReservationClient {

    public static void main(String[] args) throws Exception {

        Interval interval = new Interval(LocalDateTime.of(2025, 11, 30, 12, 0),
                LocalDateTime.of(2025, 11, 30, 12, 30));


        Employee emp1 = new Employee("Naman", 1);

        Employee emp2 = new Employee("Rick" , 2);

        Employee emp3 = new Employee("Harry" , 3);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);


        HashMap<Integer, Employee> employeeMap = new HashMap<>();
        employeeMap.put(emp1.getEmpId(),emp1);
        employeeMap.put(emp2.getEmpId(),emp2);
        employeeMap.put(emp3.getEmpId(),emp3);


        EmployeeService employeeService = new EmployeeService(employeeMap);

        ConcurrentHashMap<Integer, Booking> bookingsMap = new ConcurrentHashMap<>();

        BookingService bookingService = new BookingService(bookingsMap);

        ConcurrentHashMap<Integer, MeetingRoom> meetingRoomMap = new ConcurrentHashMap<>();

        MeetingRoom meetingRoom1 = new MeetingRoom(1, "Room1");
        MeetingRoom meetingRoom2 = new MeetingRoom(2, "Room2");
        MeetingRoom meetingRoom3 = new MeetingRoom(3, "Room3");
        MeetingRoom meetingRoom4 = new MeetingRoom(4, "Room4");
        MeetingRoom meetingRoom5 = new MeetingRoom(5, "Room5");
        meetingRoomMap.put(1,meetingRoom1);
        meetingRoomMap.put(2,meetingRoom2);
        meetingRoomMap.put(3,meetingRoom3);
        meetingRoomMap.put(4,meetingRoom4);
        meetingRoomMap.put(5,meetingRoom5);


        MeetingRoomService meetingRoomService = new MeetingRoomService(meetingRoomMap);

        MeetingReservationFacade meetingReservationSystem = new MeetingReservationFacade(bookingService,employeeService,meetingRoomService);

        List<MeetingRoom> meetingRoomList1 = meetingReservationSystem.getAvailableRooms(LocalDateTime.of(2025,11,30,10,0),
                LocalDateTime.of(2025,11,30,11,0));

        System.out.println(" Meeting rooms list initially :" + meetingRoomList1);

        // Booking room 1 for 10 - 11 am on 30/11/2025

        meetingReservationSystem.bookRoom(1,1,LocalDateTime.of(2025,11,30,10,0),
                LocalDateTime.of(2025,11,30,11,0));

        // lets again try to book meeting for same time for different employeeId and room

        meetingReservationSystem.bookRoom(2,2,LocalDateTime.of(2025,11,30,10,0),
                LocalDateTime.of(2025,11,30,11,0));

        // lets  try to book meeting for same time for different employeeId but same room

        meetingReservationSystem.bookRoom(3,2,LocalDateTime.of(2025,11,30,10,0),
                LocalDateTime.of(2025,11,30,11,0));

        List<MeetingRoom> meetingRoomList2 = meetingReservationSystem.getAvailableRooms(LocalDateTime.of(2025,11,30,10,0),
                LocalDateTime.of(2025,11,30,11,0));


        System.out.println(" Meeting rooms list after booking 2 meetings  :" + meetingRoomList2);


        System.out.println("List bookings for room 1 " + meetingReservationSystem.listBookingsForRoom(1));

        meetingReservationSystem.cancelBooking(1);



        System.out.println("List bookings for room 1 after cancelling " + meetingReservationSystem.listBookingsForRoom(1));

        System.out.println("List bookings for employee 1  " + meetingReservationSystem.listBookingsForEmployees(1));

        System.out.println("List bookings for employee 2  " + meetingReservationSystem.listBookingsForEmployees(2));


        meetingReservationSystem.bookRoom(1,1,LocalDateTime.of(2025,11,30,11,0),
                LocalDateTime.of(2025,12,30,11,0));

        List<MeetingRoom> meetingRoomList3 = meetingReservationSystem.getAvailableRooms(LocalDateTime.of(2025,11,30,10,0),
                LocalDateTime.of(2025,11,30,11,0));

        System.out.println(" Meeting rooms list after booking 2 meetings and 1 cancelled  :" + meetingRoomList3);














    }
}
