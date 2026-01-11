package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.Vehicle;
import org.lld.ridesharingapplication.repository.UserRepository;
import org.lld.ridesharingapplication.repository.VehicleRepository;

public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository) {
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("VehicleRepository cannot be null");
        }
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public Vehicle createVehicle(String ownerName, String vehicleName, String regNo) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }
        if (vehicleName == null || vehicleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle name cannot be null or empty");
        }
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be null or empty");
        }

        if (!userRepository.exists(ownerName)) {
            throw new IllegalArgumentException("User not found: " + ownerName);
        }

        // Check if vehicle already exists for this owner
        if (vehicleRepository.findByOwnerAndVehicleName(ownerName, vehicleName).isPresent()) {
            throw new IllegalArgumentException("Vehicle " + vehicleName + " already exists for owner " + ownerName);
        }

        Vehicle vehicle = new Vehicle(regNo, vehicleName, ownerName);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicle(String ownerName, String vehicleName) {
        return vehicleRepository.findByOwnerAndVehicleName(ownerName, vehicleName)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleName + " for owner " + ownerName));
    }
}

