package org.lld.meetingreservationsystem;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoom {
    private int meetingRoomId;
    private String roomName;
    List<Interval> timeSlotBookedList;
    List<Booking> roomBookingList;

    public MeetingRoom(int meetingRoomId, String roomName) {
        this.meetingRoomId = meetingRoomId;
        this.roomName = roomName;
        this.timeSlotBookedList = new ArrayList<>();
        this.roomBookingList = new ArrayList<>();
    }

    public int getMeetingRoomId() {
        return meetingRoomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Interval> getTimeSlotBookedList() {
        return timeSlotBookedList;
    }

    public List<Booking> getRoomBookingList() {
        return roomBookingList;
    }

    public void setTimeSlotBookedList(List<Interval> timeSlotBookedList) {
        this.timeSlotBookedList = timeSlotBookedList;
    }

    public void setRoomBookingList(List<Booking> roomBookingList) {
        this.roomBookingList = roomBookingList;
    }

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "meetingRoomId=" + meetingRoomId +
                ", roomName='" + roomName + '\'' +
                ", timeSlotBookedList=" + timeSlotBookedList +
                '}';
    }
}
