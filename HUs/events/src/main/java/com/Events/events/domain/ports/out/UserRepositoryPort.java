package com.Events.events.domain.ports.out;

import com.Events.events.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findById(Long id); // Usamos el optional para manejar nulos de forma segura.
    User save(User user);
    List<User> findAll();
    void deleteById(Long id);

}
