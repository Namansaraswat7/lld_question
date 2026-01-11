package org.lld.ridesharingapplication.repository;

import org.lld.ridesharingapplication.domain.Ride;
import java.util.List;
import java.util.Optional;

public interface RideRepository {
    Ride save(Ride ride);
    Optional<Ride> findById(String rideId);
    List<Ride> findAll();
    List<Ride> findByDriverName(String driverName);
    List<Ride> findByOriginAndDestination(String origin, String destination);
}

