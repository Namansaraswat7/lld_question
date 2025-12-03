package org.lld.meetingroomreservation.service;

import org.lld.meetingroomreservation.domain.Employee;
import org.lld.meetingroomreservation.repository.EmployeeRepository;

/**
 * Thread-safe implementation of EmployeeService.
 */
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        if (employeeRepository == null) {
            throw new IllegalArgumentException("EmployeeRepository cannot be null");
        }
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployee(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        return employeeRepository.findById(employeeId).orElse(null);
    }
}

