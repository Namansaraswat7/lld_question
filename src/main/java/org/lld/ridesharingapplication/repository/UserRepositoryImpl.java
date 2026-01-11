package org.lld.ridesharingapplication.repository;

import org.lld.ridesharingapplication.domain.User;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserRepositoryImpl implements UserRepository {
    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        users.put(user.getName(), user);
        return user;
    }

    @Override
    public Optional<User> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(name));
    }

    @Override
    public boolean exists(String name) {
        return name != null && users.containsKey(name);
    }
}

