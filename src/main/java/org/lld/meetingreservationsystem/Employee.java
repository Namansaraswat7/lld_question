package org.lld.meetingreservationsystem;

import java.util.ArrayList;
import java.util.List;

public class Employee {

    private String name;
    private Integer empId;
    private List<Booking> bookedMeetings;

    public Employee(String name, int empId) {
        this.name = name;
        this.empId = empId;
        this.bookedMeetings = new ArrayList<>();
    }

    public List<Booking> getBookedMeetings() {
        return bookedMeetings;
    }

    public String getName() {
        return name;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void addBooking(Booking booking){
        this.bookedMeetings.add(booking);
    }

    public void removeBooking(Booking booking) {
        this.bookedMeetings.remove(booking);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", empId=" + empId +
                '}';
    }
}
