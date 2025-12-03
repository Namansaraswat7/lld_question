package org.lld.meetingroomreservation;

import org.lld.meetingroomreservation.domain.Booking;
import org.lld.meetingroomreservation.domain.MeetingRoom;
import org.lld.meetingroomreservation.domain.TimeSlot;
import org.lld.meetingroomreservation.service.BookingService;
import org.lld.meetingroomreservation.service.EmployeeService;
import org.lld.meetingroomreservation.service.MeetingRoomService;
import org.lld.meetingroomreservation.service.MeetingRoomServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * Facade class that provides a simplified interface to the meeting room reservation system.
 * Follows Facade pattern to hide complexity of multiple services.
 * Ensures thread-safe operations with proper locking mechanisms.
 */
public class MeetingRoomReservationFacade {
    private final BookingService bookingService;
    private final EmployeeService employeeService;
    private final MeetingRoomService meetingRoomService;

    public MeetingRoomReservationFacade(BookingService bookingService,
                                       EmployeeService employeeService,
                                       MeetingRoomService meetingRoomService) {
        if (bookingService == null || employeeService == null || meetingRoomService == null) {
            throw new IllegalArgumentException("All services must be non-null");
        }
        this.bookingService = bookingService;
        this.employeeService = employeeService;
        this.meetingRoomService = meetingRoomService;
    }

    /**
     * Books a meeting room for an employee during the specified time interval.
     * Thread-safe operation that prevents double-booking using per-room locks.
     *
     * @param employeeId The ID of the employee making the booking
     * @param roomId The ID of the room to book
     * @param startTime The start time of the meeting
     * @param endTime The end time of the meeting
     * @return The created Booking object, or null if booking failed due to conflict
     * @throws IllegalArgumentException if parameters are invalid or entities not found
     */
    public Booking bookRoom(String employeeId, String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        // Validate inputs
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Validate entities exist
        if (employeeService.getEmployee(employeeId) == null) {
            throw new IllegalArgumentException("Employee not found: " + employeeId);
        }
        if (meetingRoomService.getRoom(roomId) == null) {
            throw new IllegalArgumentException("Room not found: " + roomId);
        }

        TimeSlot requestedTimeSlot = new TimeSlot(startTime, endTime);
        
        // Get per-room lock for thread-safe booking
        Lock roomLock = ((MeetingRoomServiceImpl) meetingRoomService).getRoomLock(roomId);
        
        // Acquire lock for this specific room to prevent concurrent bookings
        roomLock.lock();
        try {
            // Double-check availability after acquiring lock
            if (!meetingRoomService.isRoomAvailable(roomId, requestedTimeSlot)) {
                return null; // Room not available - overlap detected
            }

            // Create and save the booking
            Booking booking = new Booking(employeeId, roomId, requestedTimeSlot);
            return bookingService.createBooking(booking);
        } finally {
            roomLock.unlock();
        }
    }

    /**
     * Gets all available rooms for the given time interval.
     *
     * @param startTime The start time of the requested interval
     * @param endTime The end time of the requested interval
     * @return List of available meeting rooms
     */
    public List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return meetingRoomService.getAvailableRooms(startTime, endTime);
    }

    /**
     * Cancels an existing booking.
     * Thread-safe operation using per-room locks.
     *
     * @param bookingId The ID of the booking to cancel
     * @return true if booking was found and canceled, false otherwise
     */
    public boolean cancelBooking(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            return false;
        }

        // Acquire lock for the room to ensure thread-safe cancellation
        Lock roomLock = ((MeetingRoomServiceImpl) meetingRoomService).getRoomLock(booking.getRoomId());
        roomLock.lock();
        try {
            return bookingService.cancelBooking(bookingId);
        } finally {
            roomLock.unlock();
        }
    }

    /**
     * Lists all bookings for a specific room (including canceled ones).
     *
     * @param roomId The ID of the room
     * @return List of bookings for the room, sorted by start time
     */
    public List<Booking> listBookingsForRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        return bookingService.getBookingsForRoom(roomId);
    }

    /**
     * Lists all bookings for a specific employee (including canceled ones).
     *
     * @param employeeId The ID of the employee
     * @return List of bookings for the employee, sorted by start time
     */
    public List<Booking> listBookingsForEmployee(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        return bookingService.getBookingsForEmployee(employeeId);
    }

    /**
     * Adds a meeting room to the system.
     *
     * @param room The meeting room to add
     */
    public void addRoom(MeetingRoom room) {
        meetingRoomService.addRoom(room);
    }

    /**
     * Adds an employee to the system.
     *
     * @param employee The employee to add
     */
    public void addEmployee(org.lld.meetingroomreservation.domain.Employee employee) {
        employeeService.addEmployee(employee);
    }
}

