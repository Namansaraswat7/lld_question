package org.lld.ridesharingapplication.repository;

import org.lld.ridesharingapplication.domain.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findByOwnerName(String ownerName);
    Optional<Vehicle> findByOwnerAndVehicleName(String ownerName, String vehicleName);
}

