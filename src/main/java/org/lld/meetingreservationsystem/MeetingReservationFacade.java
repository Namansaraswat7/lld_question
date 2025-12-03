package org.lld.meetingreservationsystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MeetingReservationFacade {


// bookRoom(employeeId, roomId, startTime, endTime)
// getAvailableRooms(startTime, endTime)
// cancelBooking(bookingId)
// listBookingsForRoom(roomId)
// listBookingsForEmployee(employeeId)

    BookingService bookingService;

    AtomicInteger bookingIdCounter = new AtomicInteger(0);

    EmployeeService employeeService;

    MeetingRoomService meetingRoomService;

    public MeetingReservationFacade(BookingService bookingService, EmployeeService employeeService, MeetingRoomService meetingRoomService) {
        this.bookingService = bookingService;
        this.employeeService = employeeService;
        this.meetingRoomService = meetingRoomService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public MeetingRoomService getMeetingRoomService() {
        return meetingRoomService;
    }

    public void setMeetingRoomService(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    // required methods to be implemented

    void bookRoom(Integer employeeId, Integer roomId, LocalDateTime startTime, LocalDateTime endTime) throws Exception {

        // meetingroomservice will get involved
        // booking service
        // this method needs to be thread safe

        boolean roomBookingStatus =  meetingRoomService.getBookingStatusForGivenInterval(roomId,startTime,endTime);

        if(roomBookingStatus) {
            Employee employee = employeeService.getEmployeeById(employeeId);

            MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomById(roomId);

            Interval interval = new Interval(startTime, endTime);

            Booking booking = new Booking(bookingIdCounter.incrementAndGet(),"Booking No : " + bookingIdCounter.get(),employee, meetingRoom,interval);

            bookingService.addBooking(booking);
            employeeService.addMeetingForEmployee(employeeId,booking);
            meetingRoomService.addIntervalAndBookingToMeetingRoom(roomId,interval,booking);
        }

        else {
            System.out.println(" This room is already booked by someone else for given time interval");
        }

    }

    List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {

        return meetingRoomService.getAvailableRooms(startTime,endTime);

    }

    void cancelBooking(Integer bookingId) {
        // booking service
        // meeting room time will get release
        Booking cancelledBooking = bookingService.cancelBooking(bookingId);

        // we can remove booking from employee or from Meeting room otherwise we need to compare  booking status
        // inside a meeting room
        Employee employee = cancelledBooking.getBookedBy();

        employee.removeBooking(cancelledBooking);

        MeetingRoom meetingRoom = cancelledBooking.getBookedRoom();

        Interval intervalBooked = cancelledBooking.getTimeslot();

        meetingRoom.getTimeSlotBookedList().remove(intervalBooked);


    }

    List<Booking> listBookingsForRoom(Integer roomId) {
        // meeting room service will provide room bookings
        return meetingRoomService.getBookingsForRoom(roomId);
    }

    List<Booking> listBookingsForEmployees(Integer empId) throws Exception {
        // employee service will provide employee bookings
        return employeeService.getBookingsForEmployee(empId);
    }







}
