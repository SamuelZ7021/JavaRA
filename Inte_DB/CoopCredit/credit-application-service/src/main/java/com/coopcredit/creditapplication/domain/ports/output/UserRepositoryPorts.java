package com.coopcredit.creditapplication.domain.ports.output;

import com.coopcredit.creditapplication.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPorts {
    User save(User user);
    Optional<User> findByUsername(String  username);
}
