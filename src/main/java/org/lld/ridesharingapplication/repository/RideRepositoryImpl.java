package org.lld.ridesharingapplication.repository;

import org.lld.ridesharingapplication.domain.Ride;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class RideRepositoryImpl implements RideRepository {
    private final ConcurrentMap<String, Ride> ridesById = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Ride>> ridesByDriver = new ConcurrentHashMap<>();

    @Override
    public Ride save(Ride ride) {
        if (ride == null) {
            throw new IllegalArgumentException("Ride cannot be null");
        }
        ridesById.put(ride.getRide_id(), ride);
        ridesByDriver.computeIfAbsent(ride.getRideUser().getName(), k -> new ArrayList<>()).add(ride);
        return ride;
    }

    @Override
    public Optional<Ride> findById(String rideId) {
        if (rideId == null || rideId.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(ridesById.get(rideId));
    }

    @Override
    public List<Ride> findAll() {
        return new ArrayList<>(ridesById.values());
    }

    @Override
    public List<Ride> findByDriverName(String driverName) {
        if (driverName == null || driverName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(ridesByDriver.getOrDefault(driverName, new ArrayList<>()));
    }

    @Override
    public List<Ride> findByOriginAndDestination(String origin, String destination) {
        return findAll().stream()
                .filter(ride -> ride.getOrigin().equalsIgnoreCase(origin) 
                        && ride.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }
}

