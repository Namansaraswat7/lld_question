package org.lld.meetingreservationsystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EmployeeService {

    HashMap<Integer, Employee> employeeList ;

    public EmployeeService(HashMap<Integer, Employee> employeeList) {
        this.employeeList = employeeList;
    }

    void addEmployee(Employee employee) {
        employeeList.put(employee.getEmpId(),employee);
    }

    Employee getEmployeeById(Integer empId) {
        return employeeList.get(empId);
    }

    void addMeetingForEmployee(Integer empId, Booking booking) throws Exception {
        Employee employee = employeeList.get(empId);

        if(employee == null) {
            throw new Exception("Employee Not found");
        }

        employee.addBooking(booking);
    }


    List<Booking> getBookingsForEmployee(Integer empId) throws Exception {
        Employee employee = employeeList.get(empId);

        if(employee == null) {
            throw new Exception("Employee Not found");
        }
        List<Booking> bookings = Collections.unmodifiableList(employee.getBookedMeetings());
        return bookings;
    }

}
