package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.Ride;
import java.util.List;

public interface RideStrategy {
    Ride selectRide(List<Ride> rides);
}

