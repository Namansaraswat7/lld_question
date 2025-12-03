package org.lld.meetingreservationsystem;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {

    ConcurrentHashMap<Integer,Booking> bookings;

    public BookingService(ConcurrentHashMap<Integer, Booking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(Booking booking){
        bookings.put(booking.getBookingId(),booking);
    }

    public Booking cancelBooking(Integer bookingId) {
        Booking booking = bookings.get(bookingId);
        if(booking != null)
             booking.setBookingStatus(BookingStatus.CANCELLED);
        return booking;
    }



}
