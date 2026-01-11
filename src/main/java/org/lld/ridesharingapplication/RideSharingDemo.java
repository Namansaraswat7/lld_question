package org.lld.ridesharingapplication;

import org.lld.ridesharingapplication.domain.Ride;
import org.lld.ridesharingapplication.service.*;

import java.time.LocalDateTime;
import java.util.List;

public class RideSharingDemo {
    public static void main(String[] args) {
        RideSharingFacade facade = new RideSharingFacade();

        try {
            // Register users
            facade.create_user("John", "M", 26);
            facade.create_user("Smith", "M", 30);
            facade.create_user("Alice", "F", 28);
            facade.create_user("Bob", "M", 34);
            System.out.println("Users created successfully");

            // Add vehicles
            facade.create_vehicle("John", "Swift", "KA-09-32321");
            facade.create_vehicle("Alice", "Activa", "KA-01-00001");
            facade.create_vehicle("Smith", "Polo", "KA-03-30303");
            facade.create_vehicle("Bob", "XUV", "KA-05-50505");
            System.out.println("Vehicles added successfully");

            // John offers a ride
            facade.offer_ride(
                "John", "Swift", 3,
                "Bangalore", "Mysore",
                LocalDateTime.of(2024, 3, 21, 10, 0),
                3
            );

            // Alice offers a ride
            facade.offer_ride(
                "Alice", "Activa", 1,
                "Bangalore", "Mysore",
                LocalDateTime.of(2024, 3, 21, 10, 30),
                1
            );

            // Bob offers a ride
            facade.offer_ride(
                "Bob", "XUV", 6,
                "Bangalore", "Mysore",
                LocalDateTime.of(2024, 3, 21, 9, 45),
                2
            );

            // Smith offers a ride
            facade.offer_ride(
                "Smith", "Polo", 2,
                "Bangalore", "Mysore",
                LocalDateTime.of(2024, 3, 21, 8, 0),
                3
            );
            System.out.println("Rides offered successfully");

            // Smith (as passenger) selects earliest ending ride
            Ride smithRide = facade.select_ride(
                "Smith", "Bangalore", "Mysore", 2,
                new EarliestEndingRideStrategy()
            );
            System.out.println("Smith selected (earliest ending): " + smithRide);

            // Alice (as passenger) selects most vacant ride
            Ride aliceRide = facade.select_ride(
                "Alice", "Bangalore", "Mysore", 1,
                new MostVacantRideStrategy()
            );
            System.out.println("Alice selected (most vacant): " + aliceRide);

            // Bob (as passenger) selects preferred vehicle (Activa)
            Ride bobRide = facade.select_ride(
                "Bob", "Bangalore", "Mysore", 1,
                new PreferredVehicleRideStrategy("Activa")
            );
            System.out.println("Bob selected (preferred vehicle): " + bobRide);

            // List rides offered and taken by John
            List<Ride> ridesByJohn = facade.getRidesOfferedByUser("John");
            System.out.println("\nRides offered by John: " + ridesByJohn.size());
            ridesByJohn.forEach(ride -> System.out.println("  - " + ride));

            List<Ride> ridesChosenBySmith = facade.getRidesTakenByUser("Smith");
            System.out.println("\nRides taken by Smith: " + ridesChosenBySmith.size());
            ridesChosenBySmith.forEach(ride -> System.out.println("  - " + ride));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

