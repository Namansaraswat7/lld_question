package org.lld.meetingroomreservation.repository;

import org.lld.meetingroomreservation.domain.Employee;

import java.util.Optional;

/**
 * Repository interface for managing employees.
 */
public interface EmployeeRepository {
    /**
     * Saves an employee to the repository.
     * 
     * @param employee The employee to save
     * @return The saved employee
     */
    Employee save(Employee employee);

    /**
     * Finds an employee by ID.
     * 
     * @param employeeId The employee ID
     * @return Optional containing the employee if found
     */
    Optional<Employee> findById(String employeeId);
}

