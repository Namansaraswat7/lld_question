package org.lld.meetingreservationsystem;

public class Booking {

    private int BookingId;
    private String bookingName;
    private Employee bookedBy;
    private MeetingRoom bookedRoom;
    private Interval timeslot;
    private BookingStatus bookingStatus;

    public Booking(int bookingId, String bookingName, Employee bookedBy, MeetingRoom bookedRoom, Interval timeslot) {
        BookingId = bookingId;
        this.bookingName = bookingName;
        this.bookedBy = bookedBy;
        this.bookedRoom = bookedRoom;
        this.timeslot = timeslot;
        this.bookingStatus = BookingStatus.ACTIVE;
    }

    public int getBookingId() {
        return BookingId;
    }

    public String getBookingName() {
        return bookingName;
    }

    public Employee getBookedBy() {
        return bookedBy;
    }

    public MeetingRoom getBookedRoom() {
        return bookedRoom;
    }

    public Interval getTimeslot() {
        return timeslot;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "BookingId=" + BookingId +
                ", bookingName='" + bookingName + '\'' +
                ", bookedBy=" + bookedBy +
                ", bookedRoom=" + bookedRoom +
                ", timeslot=" + timeslot +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
