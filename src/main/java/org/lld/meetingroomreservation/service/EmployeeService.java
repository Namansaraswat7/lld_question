package org.lld.meetingroomreservation.service;

import org.lld.meetingroomreservation.domain.Employee;

/**
 * Service interface for employee operations.
 */
public interface EmployeeService {
    /**
     * Adds an employee to the system.
     * 
     * @param employee The employee to add
     */
    void addEmployee(Employee employee);

    /**
     * Gets an employee by ID.
     * 
     * @param employeeId The employee ID
     * @return The employee if found, null otherwise
     */
    Employee getEmployee(String employeeId);
}

