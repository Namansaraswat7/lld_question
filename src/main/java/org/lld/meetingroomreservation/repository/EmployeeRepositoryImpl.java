package org.lld.meetingroomreservation.repository;

import org.lld.meetingroomreservation.domain.Employee;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe implementation of EmployeeRepository.
 */
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final ConcurrentMap<String, Employee> employees = new ConcurrentHashMap<>();

    @Override
    public Employee save(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        employees.put(employee.getEmployeeId(), employee);
        return employee;
    }

    @Override
    public Optional<Employee> findById(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(employees.get(employeeId));
    }
}

