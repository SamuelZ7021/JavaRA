package com.Events.events.application.usecase;

import com.Events.events.domain.model.User;
import com.Events.events.domain.ports.in.UserUseCase;
import com.Events.events.domain.ports.out.UserRepositoryPort;

import java.util.List;

public class UserUseCaseImpl implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User create(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User update(Long id, User user) {
        // 1. Verificamos si el usuario existe
        User currentUser = findById(id);

        // 2. Actualizamos campos(Lógica de negocios: mantener el ID original
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());

        // 4. Guardamos los cambios
        return userRepositoryPort.save(currentUser);
    }

    @Override
    public void dalete(Long id) {
        // Verifica la existencia del ID para eliminar
        findById(id);
        userRepositoryPort.deleteById(id);

    }
}
