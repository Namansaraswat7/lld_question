package org.lld.ridesharingapplication.service;

import org.lld.ridesharingapplication.domain.User;
import org.lld.ridesharingapplication.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        this.userRepository = userRepository;
    }

    public User createUser(String name, String gender, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        
        if (userRepository.exists(name)) {
            throw new IllegalArgumentException("User already exists: " + name);
        }
        
        User user = new User(name, gender, age);
        return userRepository.save(user);
    }

    public User getUser(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + name));
    }

    public boolean userExists(String name) {
        return userRepository.exists(name);
    }
}

