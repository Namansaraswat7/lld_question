package org.lld.ridesharingapplication.repository;

import org.lld.ridesharingapplication.domain.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {
    private final ConcurrentMap<String, List<Vehicle>> vehiclesByOwner = new ConcurrentHashMap<>();

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        vehiclesByOwner.computeIfAbsent(vehicle.getVehicleOwnerName(), k -> new ArrayList<>()).add(vehicle);
        return vehicle;
    }

    @Override
    public List<Vehicle> findByOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(vehiclesByOwner.getOrDefault(ownerName, new ArrayList<>()));
    }

    @Override
    public Optional<Vehicle> findByOwnerAndVehicleName(String ownerName, String vehicleName) {
        List<Vehicle> vehicles = findByOwnerName(ownerName);
        return vehicles.stream()
                .filter(v -> v.getVehicleName().equals(vehicleName))
                .findFirst();
    }
}

