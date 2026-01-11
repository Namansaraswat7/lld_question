package org.lld.ridesharingapplication;

import org.lld.ridesharingapplication.domain.Ride;
import org.lld.ridesharingapplication.domain.User;
import org.lld.ridesharingapplication.repository.RideRepository;
import org.lld.ridesharingapplication.repository.RideRepositoryImpl;
import org.lld.ridesharingapplication.repository.UserRepository;
import org.lld.ridesharingapplication.repository.UserRepositoryImpl;
import org.lld.ridesharingapplication.repository.VehicleRepository;
import org.lld.ridesharingapplication.repository.VehicleRepositoryImpl;
import org.lld.ridesharingapplication.service.RideService;
import org.lld.ridesharingapplication.service.RideStrategy;
import org.lld.ridesharingapplication.service.UserService;
import org.lld.ridesharingapplication.service.VehicleService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Facade class that provides a simplified interface to the ride sharing system.
 * Follows Facade pattern to hide complexity of multiple services.
 * Ensures thread-safe operations with proper validation.
 */
public class RideSharingFacade {
    private final UserService userService;
    private final VehicleService vehicleService;
    private final RideService rideService;

    public RideSharingFacade() {
        // Initialize repositories
        UserRepository userRepository = new UserRepositoryImpl();
        VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
        RideRepository rideRepository = new RideRepositoryImpl();

        // Initialize services with dependencies
        this.userService = new UserService(userRepository);
        this.vehicleService = new VehicleService(vehicleRepository, userRepository);
        this.rideService = new RideService(rideRepository, userRepository, vehicleRepository);
    }

    /**
     * Create a new user in the system.
     *
     * @param name User's name
     * @param gender User's gender (M/F)
     * @param age User's age
     * @throws IllegalArgumentException if parameters are invalid or user already exists
     */
    public void create_user(String name, String gender, int age) {
        userService.createUser(name, gender, age);
    }

    /**
     * Add a vehicle for a user.
     *
     * @param ownerName Name of the vehicle owner
     * @param vehicleName Name/model of the vehicle
     * @param regNo Registration number of the vehicle
     * @throws IllegalArgumentException if parameters are invalid, user not found, or vehicle already exists
     */
    public void create_vehicle(String ownerName, String vehicleName, String regNo) {
        vehicleService.createVehicle(ownerName, vehicleName, regNo);
    }

    /**
     * Offer a ride to other users.
     *
     * @param name Driver's name
     * @param vehicleName Name of the vehicle to use
     * @param seats_available Number of seats available
     * @param origin Starting location
     * @param destination Ending location
     * @param start Start time of the ride
     * @param durationHours Duration of the ride in hours
     * @throws IllegalArgumentException if parameters are invalid, user not found, or vehicle not found
     */
    public void offer_ride(String name, String vehicleName, Integer seats_available,
                          String origin, String destination, LocalDateTime start, int durationHours) {
        rideService.offerRide(name, vehicleName, seats_available, origin, destination, start, durationHours);
    }

    /**
     * Select a ride based on origin, destination, and selection preference.
     *
     * @param name Passenger's name
     * @param origin Starting location
     * @param destination Ending location
     * @param seats_required Number of seats required
     * @param rideStrategy Selection strategy (earliest ending, lowest duration, preferred vehicle, most vacant)
     * @return Selected Ride object, or null if no eligible rides found
     * @throws IllegalArgumentException if parameters are invalid or user not found
     */
    public Ride select_ride(String name, String origin, String destination, 
                           Integer seats_required, RideStrategy rideStrategy) {
        return rideService.selectRide(name, origin, destination, seats_required, rideStrategy);
    }

    /**
     * Get all rides offered by a user (as driver).
     *
     * @param name User's name
     * @return List of rides offered by the user
     * @throws IllegalArgumentException if name is null or empty
     */
    public List<Ride> getRidesOfferedByUser(String name) {
        return rideService.getRidesOfferedByUser(name);
    }

    /**
     * Get all rides taken by a user (as passenger).
     *
     * @param name User's name
     * @return List of rides taken by the user
     * @throws IllegalArgumentException if name is null or empty
     */
    public List<Ride> getRidesTakenByUser(String name) {
        return rideService.getRidesTakenByUser(name);
    }
}

