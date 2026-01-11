package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.Ride;
import org.lld.ridesharingapplication.domain.Vehicle;
import java.util.List;
import java.util.Optional;

public class PreferredVehicleRideStrategy implements RideStrategy {
    private String preferredVehicleName;
    public PreferredVehicleRideStrategy(String preferredVehicleName) {
        this.preferredVehicleName = preferredVehicleName;
    }
    @Override
    public Ride selectRide(List<Ride> rides) {
        Optional<Ride> pref = rides.stream().filter(r -> r.getVehicleForRide().getVehicleName().equalsIgnoreCase(preferredVehicleName)).findFirst();
        return pref.orElse(rides.isEmpty() ? null : rides.get(0));
    }
}

