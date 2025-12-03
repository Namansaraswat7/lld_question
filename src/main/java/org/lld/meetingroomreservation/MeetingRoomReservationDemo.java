package org.lld.meetingroomreservation;

import org.lld.meetingroomreservation.domain.Booking;
import org.lld.meetingroomreservation.domain.Employee;
import org.lld.meetingroomreservation.domain.MeetingRoom;
import org.lld.meetingroomreservation.repository.BookingRepository;
import org.lld.meetingroomreservation.repository.BookingRepositoryImpl;
import org.lld.meetingroomreservation.repository.EmployeeRepository;
import org.lld.meetingroomreservation.repository.EmployeeRepositoryImpl;
import org.lld.meetingroomreservation.repository.MeetingRoomRepository;
import org.lld.meetingroomreservation.repository.MeetingRoomRepositoryImpl;
import org.lld.meetingroomreservation.service.BookingService;
import org.lld.meetingroomreservation.service.BookingServiceImpl;
import org.lld.meetingroomreservation.service.EmployeeService;
import org.lld.meetingroomreservation.service.EmployeeServiceImpl;
import org.lld.meetingroomreservation.service.MeetingRoomService;
import org.lld.meetingroomreservation.service.MeetingRoomServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo class demonstrating the Meeting Room Reservation System.
 * Shows the example scenario from requirements and additional features.
 */
public class MeetingRoomReservationDemo {

    public static void main(String[] args) {
        // Initialize repositories
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
        MeetingRoomRepository roomRepository = new MeetingRoomRepositoryImpl();

        // Initialize services
        BookingService bookingService = new BookingServiceImpl(bookingRepository);
        EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
        MeetingRoomService meetingRoomService = new MeetingRoomServiceImpl(roomRepository, bookingRepository);

        // Create facade
        MeetingRoomReservationFacade system = new MeetingRoomReservationFacade(
                bookingService, employeeService, meetingRoomService);

        // Setup: Add rooms and employees
        MeetingRoom roomA = new MeetingRoom("R001", "Conference Room A", 10, "Floor 1");
        MeetingRoom roomB = new MeetingRoom("R002", "Conference Room B", 15, "Floor 2");
        MeetingRoom roomC = new MeetingRoom("R003", "Board Room", 20, "Floor 3");

        system.addRoom(roomA);
        system.addRoom(roomB);
        system.addRoom(roomC);

        Employee userX = new Employee("E001", "User X", "userx@company.com");
        Employee userY = new Employee("E002", "User Y", "usery@company.com");
        Employee userZ = new Employee("E003", "User Z", "userz@company.com");

        system.addEmployee(userX);
        system.addEmployee(userY);
        system.addEmployee(userZ);

        System.out.println("=== Meeting Room Reservation System Demo ===\n");

        // Example Scenario from Requirements
        LocalDateTime baseTime = LocalDateTime.of(2024, 1, 15, 10, 0);

        // Room A is booked from 10:00 AM to 11:00 AM
        LocalDateTime start1 = baseTime; // 10:00 AM
        LocalDateTime end1 = baseTime.plusHours(1); // 11:00 AM
        Booking booking1 = system.bookRoom(userX.getEmployeeId(), roomA.getRoomId(), start1, end1);
        System.out.println("1. User X books Room A from 10:00 AM to 11:00 AM");
        System.out.println("   Result: " + (booking1 != null ? "SUCCESS - " + booking1.getBookingId() : "FAILED"));
        System.out.println();

        // User X tries to book Room A from 10:30 AM to 11:30 AM → Should fail due to overlap
        LocalDateTime start2 = baseTime.plusMinutes(30); // 10:30 AM
        LocalDateTime end2 = baseTime.plusHours(1).plusMinutes(30); // 11:30 AM
        Booking booking2 = system.bookRoom(userX.getEmployeeId(), roomA.getRoomId(), start2, end2);
        System.out.println("2. User X tries to book Room A from 10:30 AM to 11:30 AM (overlaps with existing)");
        System.out.println("   Result: " + (booking2 != null ? "SUCCESS - " + booking2.getBookingId() : "FAILED - Overlap detected"));
        System.out.println();

        // User Y books Room B from 10:30 AM to 11:30 AM → Should succeed
        Booking booking3 = system.bookRoom(userY.getEmployeeId(), roomB.getRoomId(), start2, end2);
        System.out.println("3. User Y books Room B from 10:30 AM to 11:30 AM");
        System.out.println("   Result: " + (booking3 != null ? "SUCCESS - " + booking3.getBookingId() : "FAILED"));
        System.out.println();

        // Additional scenarios
        System.out.println("=== Additional Scenarios ===\n");

        // Check available rooms
        LocalDateTime checkStart = baseTime.plusHours(2); // 12:00 PM
        LocalDateTime checkEnd = baseTime.plusHours(3); // 1:00 PM
        List<MeetingRoom> availableRooms = system.getAvailableRooms(checkStart, checkEnd);
        System.out.println("4. Available rooms from 12:00 PM to 1:00 PM:");
        availableRooms.forEach(room -> System.out.println("   - " + room.getName() + " (ID: " + room.getRoomId() + ")"));
        System.out.println();

        // List bookings for Room A
        System.out.println("5. All bookings for Room A:");
        List<Booking> roomABookings = system.listBookingsForRoom(roomA.getRoomId());
        roomABookings.forEach(booking -> {
            System.out.println("   - " + booking.getTimeSlot().getStartTime() + " to " + 
                             booking.getTimeSlot().getEndTime() + " by Employee " + booking.getEmployeeId() +
                             " [" + booking.getStatus() + "]");
        });
        System.out.println();

        // List bookings for User X
        System.out.println("6. All bookings for User X:");
        List<Booking> userXBookings = system.listBookingsForEmployee(userX.getEmployeeId());
        userXBookings.forEach(booking -> {
            System.out.println("   - Room " + booking.getRoomId() + ": " + 
                             booking.getTimeSlot().getStartTime() + " to " + 
                             booking.getTimeSlot().getEndTime() +
                             " [" + booking.getStatus() + "]");
        });
        System.out.println();

        // Cancel a booking
        if (booking1 != null) {
            boolean canceled = system.cancelBooking(booking1.getBookingId());
            System.out.println("7. Cancel booking " + booking1.getBookingId());
            System.out.println("   Result: " + (canceled ? "SUCCESS" : "FAILED"));
            System.out.println();

            // Try to book Room A again after cancellation
            Booking booking4 = system.bookRoom(userZ.getEmployeeId(), roomA.getRoomId(), start1, end1);
            System.out.println("8. User Z books Room A from 10:00 AM to 11:00 AM (after previous booking was canceled)");
            System.out.println("   Result: " + (booking4 != null ? "SUCCESS - " + booking4.getBookingId() : "FAILED"));
            System.out.println();
        }

        // Edge case: Invalid time range
        System.out.println("9. Edge case: Try to book with invalid time range (end before start)");
        try {
            system.bookRoom(userX.getEmployeeId(), roomA.getRoomId(), end1, start1);
        } catch (IllegalArgumentException e) {
            System.out.println("   Result: Correctly rejected - " + e.getMessage());
        }
        System.out.println();

        // Edge case: Non-existent room
        System.out.println("10. Edge case: Try to book non-existent room");
        try {
            system.bookRoom(userX.getEmployeeId(), "INVALID", start1, end1);
        } catch (IllegalArgumentException e) {
            System.out.println("   Result: Correctly rejected - " + e.getMessage());
        }
        System.out.println();

    }
}

