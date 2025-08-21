package com.authapp.repository;

import com.authapp.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private static final Map<String, User> userStore = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());

        }
        userStore.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userStore.get(email));
    }
}