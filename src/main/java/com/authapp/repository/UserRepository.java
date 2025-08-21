package com.authapp.repository;

import com.authapp.model.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findByEmail(String email);
       
}