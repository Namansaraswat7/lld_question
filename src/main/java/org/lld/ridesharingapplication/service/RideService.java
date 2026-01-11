package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.Ride;
import org.lld.ridesharingapplication.domain.RideStatus;
import org.lld.ridesharingapplication.domain.User;
import org.lld.ridesharingapplication.domain.Vehicle;
import org.lld.ridesharingapplication.repository.RideRepository;
import org.lld.ridesharingapplication.repository.UserRepository;
import org.lld.ridesharingapplication.repository.VehicleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RideService {
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ConcurrentMap<String, List<Ride>> ridesTakenByPassenger = new ConcurrentHashMap<>();

    public RideService(RideRepository rideRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        if (rideRepository == null) {
            throw new IllegalArgumentException("RideRepository cannot be null");
        }
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        if (vehicleRepository == null) {
            throw new IllegalArgumentException("VehicleRepository cannot be null");
        }
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Ride offerRide(String name, String vehicleName, Integer seatsAvailable, 
                         String origin, String destination, LocalDateTime start, int durationHours) {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be null or empty");
        }
        if (vehicleName == null || vehicleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle name cannot be null or empty");
        }
        if (seatsAvailable == null || seatsAvailable <= 0) {
            throw new IllegalArgumentException("Seats available must be positive");
        }
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (durationHours <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        // Check if user exists
        User driver = userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + name));

        // Check if vehicle exists and belongs to user
        Vehicle vehicle = vehicleRepository.findByOwnerAndVehicleName(name, vehicleName)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle " + vehicleName + " not found for user " + name));

        // Create and save ride
        Ride ride = new Ride(UUID.randomUUID().toString(), origin, destination, vehicle, 
                           seatsAvailable, RideStatus.START, driver, durationHours, start);
        return rideRepository.save(ride);
    }

    public Ride selectRide(String passengerName, String origin, String destination, 
                          Integer seatsRequired, RideStrategy rideStrategy) {
        // Validation
        if (passengerName == null || passengerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be null or empty");
        }
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (seatsRequired == null || seatsRequired <= 0) {
            throw new IllegalArgumentException("Seats required must be positive");
        }
        if (rideStrategy == null) {
            throw new IllegalArgumentException("Ride strategy cannot be null");
        }

        // Check if user exists
        if (!userRepository.exists(passengerName)) {
            throw new IllegalArgumentException("User not found: " + passengerName);
        }

        // Find eligible rides
        List<Ride> eligibleRides = rideRepository.findByOriginAndDestination(origin, destination);
        eligibleRides = eligibleRides.stream()
                .filter(ride -> ride.getTotalSeatsForRide() >= seatsRequired 
                        && ride.getRideStatus() == RideStatus.START
                        && !ride.getRideUser().getName().equals(passengerName)) // Passenger cannot select their own ride
                .collect(java.util.stream.Collectors.toList());

        if (eligibleRides.isEmpty()) {
            return null;
        }

        // Select ride based on strategy
        Ride selectedRide = rideStrategy.selectRide(eligibleRides);
        if (selectedRide != null) {
            // Update available seats
            selectedRide.setTotalSeatsForRide(selectedRide.getTotalSeatsForRide() - seatsRequired);
            // 4 -> seats  -> booked 2 seats -> remaining seats would be 2
            ridesTakenByPassenger.computeIfAbsent(passengerName, k -> new ArrayList<>()).add(selectedRide);
        }

        return selectedRide;
    }

    public List<Ride> getRidesOfferedByUser(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return rideRepository.findByDriverName(name);
    }

    public List<Ride> getRidesTakenByUser(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return new ArrayList<>(ridesTakenByPassenger.getOrDefault(name, new ArrayList<>()));
    }
}

