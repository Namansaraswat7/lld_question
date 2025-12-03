package org.lld.meetingreservationsystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MeetingRoomService {

    ConcurrentHashMap<Integer, MeetingRoom> meetingRoomList;

    public MeetingRoomService(ConcurrentHashMap<Integer, MeetingRoom> meetingRoomList) {
        this.meetingRoomList = meetingRoomList;
    }

    public MeetingRoom getMeetingRoomById(Integer roomId) {
        return meetingRoomList.get(roomId);
    }

    public void addMeetingRoomById(Integer roomId) {
        meetingRoomList.put(roomId, getMeetingRoomById(roomId));
    }

    public void addMeetingRoom(MeetingRoom room) {
        meetingRoomList.put(room.getMeetingRoomId(),room);
    }

    public List<Booking> getBookingsForRoom(Integer roomId) {
        MeetingRoom meetingRoom = getMeetingRoomById(roomId);
        return meetingRoom.getRoomBookingList();
    }

    public List<MeetingRoom> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {

        Interval interval = new Interval(startTime,endTime);


        Set<Map.Entry<Integer, MeetingRoom>> meetingRooms = meetingRoomList.entrySet().stream().filter(roomEntry-> {

            Optional<Interval> intervalMatch = roomEntry.getValue().getTimeSlotBookedList().stream().filter(roomInterval -> roomInterval.getStartDateTime().equals(startTime)
                    && roomInterval.getEndDateTime().equals(endTime)).findFirst();
            if(intervalMatch.isPresent())
                return false;
            return true;
        }).collect(Collectors.toSet());


        List<MeetingRoom> list = meetingRooms.stream().map(m -> m.getValue()).toList();
        return list;
    }


    public boolean getBookingStatusForGivenInterval(Integer roomId, LocalDateTime startTime, LocalDateTime endTime) {
        MeetingRoom meetingRoom = getMeetingRoomById(roomId);

        Optional<Interval> intervalAvailable = meetingRoom.getTimeSlotBookedList().stream().filter(interval -> {
//            System.out.println("Interval starTime stored = " + interval.getStartDateTime());
//            System.out.println("Interval endTime stored = " + interval.getEndDateTime());
//            System.out.println("Interval starTime given = " + startTime);
//            System.out.println("Interval endTime given = " + endTime);

            return interval.getStartDateTime().equals(startTime) &&
                interval.getEndDateTime().equals(endTime);

        }).findFirst();

//        System.out.println("Interval Available = " +  intervalAvailable);

        return intervalAvailable.isEmpty();
    }

    public void addIntervalAndBookingToMeetingRoom(Integer meetingRoomId, Interval interval, Booking booking) {
        MeetingRoom meetingRoom = getMeetingRoomById(meetingRoomId);
        List<Interval> intervalList =  meetingRoom.getTimeSlotBookedList();
        intervalList.add(interval);
        meetingRoom.setTimeSlotBookedList(intervalList);

        List<Booking> bookingList = meetingRoom.getRoomBookingList();
        bookingList.add(booking);
        meetingRoom.setRoomBookingList(bookingList);
    }


}
