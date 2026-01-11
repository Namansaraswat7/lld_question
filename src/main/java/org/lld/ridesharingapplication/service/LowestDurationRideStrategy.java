package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.Ride;
import java.util.List;

public class LowestDurationRideStrategy implements RideStrategy {
    @Override
    public Ride selectRide(List<Ride> rides) {
        return rides.stream()
                .min((r1, r2) -> r1.getDurationHours().compareTo(r2.getDurationHours()))
                .orElse(null);
    }
}

