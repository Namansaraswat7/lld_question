package org.lld.ridesharingapplication.repository;

import org.lld.ridesharingapplication.domain.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByName(String name);
    boolean exists(String name);
}

