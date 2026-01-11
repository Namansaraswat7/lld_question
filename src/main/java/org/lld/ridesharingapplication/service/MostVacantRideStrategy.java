package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.Ride;
import java.util.List;

public class MostVacantRideStrategy implements RideStrategy {
    @Override
    public Ride selectRide(List<Ride> rides) {
        return rides.stream()
            .max((r1, r2) -> r1.getTotalSeatsForRide().compareTo(r2.getTotalSeatsForRide()))
            .orElse(null);
    }
}

