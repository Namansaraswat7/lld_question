package org.lld.ridesharingapplication.domain;

import java.time.LocalDateTime;

public class Ride {
    private String ride_id;
    private String origin;
    private String destination;
    private Vehicle vehicleForRide;
    private Integer totalSeatsForRide;
    private RideStatus rideStatus;
    private User rideUser;
    private Integer rideFare;
    private LocalDateTime startTime;
    private Integer durationHours;

    public Ride(String ride_id, String origin, String destination, Vehicle vehicleForRide, Integer totalSeatsForRide, RideStatus rideStatus, User rideUser, Integer durationHours, LocalDateTime startTime) {
        this.ride_id = ride_id;
        this.origin = origin;
        this.destination = destination;
        this.vehicleForRide = vehicleForRide;
        this.totalSeatsForRide = totalSeatsForRide;
        this.rideStatus = rideStatus;
        this.rideUser = rideUser;
        this.durationHours = durationHours;
        this.startTime = startTime;
    }

    public String getRide_id() { return ride_id; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public Vehicle getVehicleForRide() { return vehicleForRide; }
    public Integer getTotalSeatsForRide() { return totalSeatsForRide; }
    public void setTotalSeatsForRide(Integer totalSeatsForRide) { this.totalSeatsForRide = totalSeatsForRide; }
    public RideStatus getRideStatus() { return rideStatus; }
    public User getRideUser() { return rideUser; }
    public Integer getRideFare() { return rideFare; }
    public LocalDateTime getStartTime() { return startTime; }
    public Integer getDurationHours() { return durationHours; }
    public LocalDateTime getEndTime() { return this.startTime.plusHours(this.durationHours); }

    @Override
    public String toString() {
        return "Ride{" +
                "ride_id='" + ride_id + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", vehicleForRide=" + vehicleForRide +
                ", totalSeatsForRide=" + totalSeatsForRide +
                ", rideUser=" + rideUser +
                ", durationHours=" + durationHours +
                '}';
    }
}
